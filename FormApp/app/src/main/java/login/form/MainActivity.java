package login.form;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {



    private TextView dateTextView;
    private ImageView selectedImageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int CAMERA_PERMISSION_CODE = 100;

//    Button googleAuth;
//    FirebaseAuth auth;
//    FirebaseDatabase database;
//    GoogleSignInClient mGoogleSignInClient;
//    private static final int RC_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateTextView = findViewById(R.id.dateTextView);
        selectedImageView = findViewById(R.id.selectedImageView);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

//        googleAuth = findViewById(R.id.btnGoogleAuth);
//        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail().build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        googleAuth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                googleSignIn();
//            }
//        });
        // Set "ProfileImage" to an empty string by default

        editor.putString("ProfileImage", "");
        editor.apply();

    }

//    private void googleSignIn(){
//        Intent intent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(intent,RC_SIGN_IN);
//    }
    private boolean areEditTextsEmpty() {
        EditText[] editTexts = {
                findViewById(R.id.editfirst),
                findViewById(R.id.editlast),
                findViewById(R.id.editage),
                findViewById(R.id.editemail),
                findViewById(R.id.editpassword),
                findViewById(R.id.editcnfpassword),
                findViewById(R.id.editphone),
                findViewById(R.id.editaddress),
                findViewById(R.id.editpincode)
        };

        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                return true; // At least one EditText is empty
            }
        }

        return false;
    }

    private boolean isGenderSelected() {
        RadioButton maleRadioButton = findViewById(R.id.radiomale);
        RadioButton femaleRadioButton = findViewById(R.id.radiofemale);
        return maleRadioButton.isChecked() || femaleRadioButton.isChecked();
    }

    private boolean isAcceptSelected() {
        CheckBox box = findViewById(R.id.allacceptbox);
        return box.isChecked();
    }

    public void register(View view) {
        TextView dateTextView = findViewById(R.id.dateTextView);

        if (areEditTextsEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } else if (dateTextView.getText().toString().equals("Select a Date")) {
            Toast.makeText(this, "Please pick a date", Toast.LENGTH_SHORT).show();
        } else if (!isGenderSelected()) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
        } else if (!isAcceptSelected()) {
            Toast.makeText(this, "Please select Accept Terms", Toast.LENGTH_SHORT).show();
        } else {
            // Save user details to SharedPreferences
            saveUserDetails();
            // Open the new activity to display user details
            Intent intent = new Intent(this, DisplayDetailsActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText firstNameEditText = findViewById(R.id.editfirst);
        EditText lastNameEditText = findViewById(R.id.editlast);
        EditText ageEditText = findViewById(R.id.editage);
        EditText emailEditText = findViewById(R.id.editemail);
        EditText passwordEditText = findViewById(R.id.editpassword);
        EditText phoneEditText = findViewById(R.id.editphone);
        EditText addressEditText = findViewById(R.id.editaddress);
        EditText pincodeEditText = findViewById(R.id.editpincode);


        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String pincode = pincodeEditText.getText().toString();

        // Save gender
        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
        RadioButton selectedGenderRadioButton = findViewById(genderRadioGroup.getCheckedRadioButtonId());
        String gender = selectedGenderRadioButton.getText().toString();

        // Retrieve the selected date of birth from the TextView
        String dob = dateTextView.getText().toString();

        Spinner stateSpinner = findViewById(R.id.state);
        String selectedState = stateSpinner.getSelectedItem().toString();

        // Store user details in SharedPreferences
        editor.putString("FirstName", firstName);
        editor.putString("LastName", lastName);
        editor.putString("Age", age);
        editor.putString("Gender", gender);
        editor.putString("DOB", dob);
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.putString("Phone", phone);
        editor.putString("Address", address);
        editor.putString("Pincode", pincode);
        editor.putString("State", selectedState);
        editor.apply();
    }


    public void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int minYear = 2000;
        int maxYear = year;
        int maxMonth = month;
        int maxDay = day;

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // Update the TextView with the selected date
                        dateTextView.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.getDatePicker().setMinDate(getDateInMillis(minYear, 0, 1));
        datePickerDialog.getDatePicker().setMaxDate(getDateInMillis(maxYear, maxMonth, maxDay));
        datePickerDialog.show();
    }

    private long getDateInMillis(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the bitmap to a Base64 string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String imageBase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        editor.putString("ProfileImage", imageBase64);
        editor.apply();
    }

    public void pickFromGallery(View view) {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    public void takePhoto(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            // Request camera permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                // Get the captured image as a Bitmap
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                if (imageBitmap != null) {
                    selectedImageView.setImageBitmap(imageBitmap);

                    // Save the selected image to SharedPreferences
                    saveBitmapToSharedPreferences(imageBitmap);
                }
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        selectedImageView.setImageBitmap(imageBitmap);

                        // Save the selected image to SharedPreferences
                        saveBitmapToSharedPreferences(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
        }
    }

//    private void firbaseAuth(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task){
//                        if(task.isSuccessful()){
////                            FirebaseUser user = auth.getCurrentUser();
////                            HashMap<String,Object> map = new HashMap<>();
////                            map.put("id",user.getUid());
////                            map.put("name",user.getDisplayName());
////                            map.put("profile",user.getPhotoUrl().toString());
////                            database.getReference().child("users").child(user.getUid()).setValue(map);
//                            Intent intent = new Intent(MainActivity.this,GoogleActivity.class);
//                            startActivity(intent);
//                        }else{
//                            Toast.makeText(MainActivity.this,"something went wrong ",Toast.LENGTH_SHORT).show();
//                        }
//                }
//        });
//    }
}
