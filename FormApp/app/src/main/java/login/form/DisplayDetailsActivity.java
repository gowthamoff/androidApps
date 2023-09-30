package login.form;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class DisplayDetailsActivity extends AppCompatActivity {
    Button button3;
    Dialog dialog;
    private ImageView profileImageView;

    // Add a reference to the GoogleSignInClient
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);

        // Initialize GoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        String firstName = sharedPreferences.getString("FirstName", "");
        String lastName = sharedPreferences.getString("LastName", "");
        String age = sharedPreferences.getString("Age", "");
        String gender = sharedPreferences.getString("Gender", "");
        String dob = sharedPreferences.getString("DOB", "");
        String email = sharedPreferences.getString("Email", "");
        String password = sharedPreferences.getString("Password", "");
        String phone = sharedPreferences.getString("Phone", "");
        String pincode = sharedPreferences.getString("Pincode", "");
        String address = sharedPreferences.getString("Address", "");
        String state = sharedPreferences.getString("State", "");

        Button logoutButton = findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Firebase Authentication
                FirebaseAuth.getInstance().signOut();

                // Sign out from Google Sign-In
                mGoogleSignInClient.signOut().addOnCompleteListener(DisplayDetailsActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        // Redirect to the LoginOption page
                        Intent intent = new Intent(DisplayDetailsActivity.this, LoginOption.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

//        button3 = findViewById(R.id.button3);
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog = new Dialog(DisplayDetailsActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.progress_bar_3);
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
//
//                new CountDownTimer(5000, 1000) {
//                    @Override
//                    public void onTick(long l) {
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        dialog.dismiss();
//                    }
//                }.start();
//            }
//        });

        TextView firstNameTextView = findViewById(R.id.firstNameTextView);
        TextView lastNameTextView = findViewById(R.id.lastNameTextView);
        TextView ageTextView = findViewById(R.id.ageTextView);
        TextView genderTextView = findViewById(R.id.genderTextView);
        TextView dobTextView = findViewById(R.id.dobTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView passwordTextView = findViewById(R.id.passwordTextView);
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        TextView pincodeTextView = findViewById(R.id.pincodeTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView stateTextView = findViewById(R.id.stateTextView);

        firstNameTextView.setText("First Name: " + firstName);
        lastNameTextView.setText("Last Name: " + lastName);
        ageTextView.setText("Age: " + age);
        genderTextView.setText("Gender: " + gender);
        dobTextView.setText("Date of Birth: " + dob);
        emailTextView.setText("Email: " + email);
        passwordTextView.setText("Password: " + password);
        phoneTextView.setText("Phone Number: " + phone);
        pincodeTextView.setText("Pincode: " + pincode);
        addressTextView.setText("Address: " + address);
        stateTextView.setText("State: " + state);

        // Retrieve and set the profile image from SharedPreferences
        Bitmap profileImage = getBitmapFromSharedPreferences();
        profileImageView = findViewById(R.id.profile);

        if (profileImage != null) {
            // If the user has uploaded a profile image, set it
            profileImageView.setImageBitmap(profileImage);
        } else {
            // If there's no user-uploaded image, set the default profile image
            profileImageView.setImageResource(R.drawable.profile);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private Bitmap getBitmapFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String encodedBitmap = sharedPreferences.getString("ProfileImage", "");

        if (!encodedBitmap.isEmpty()) {
            byte[] decodedBytes = Base64.decode(encodedBitmap, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } else {
            return null;
        }
    }
}
