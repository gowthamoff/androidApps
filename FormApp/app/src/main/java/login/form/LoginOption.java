package login.form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


public class LoginOption extends AppCompatActivity {

    Button googleAuth;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch MainActivity
                Intent intent = new Intent(LoginOption.this,MainActivity.class);
                startActivity(intent);
            }
        });

//        googleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Launch GoogleActivity
//                Intent intent = new Intent(LoginOption.this, GoogleActivity.class);
//                startActivity(intent);
//            }
//        });

        googleAuth = findViewById(R.id.btnGoogleAuth);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
    }

    private void googleSignIn(){
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firbaseAuth(account.getIdToken());
                } else {
                    Log.e("YourTag", "Google Sign-In Error: Account is null");
                    Toast.makeText(this, "Google Sign-In Error: Account is null", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                // Handle specific Google Sign-In API exceptions
                Log.e("YourTag", "Google Sign-In API Exception: " + e.getStatusCode());
                Toast.makeText(this, "Google Sign-In Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Handle other exceptions
                Log.e("YourTag", "Exception: " + e.getMessage(), e);
                Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void firbaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GoogleSignInAccount account = null;
                            try {
                                account = (GoogleSignInAccount) task.getResult(ApiException.class);
                            } catch (ApiException e) {
                                throw new RuntimeException(e);
                            }
                            saveUserDetails(account); // Save user details

                            Intent intent = new Intent(LoginOption.this, DisplayDetailsActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginOption.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Define the saveUserDetails method here
    private void saveUserDetails(GoogleSignInAccount account) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store user details in SharedPreferences
        editor.putString("FirstName", account.getGivenName().toString());
        editor.putString("LastName", account.getFamilyName().toString());
        editor.putString("Email", account.getEmail().toString());
        editor.putString("Age", "ife");
        editor.putString("Gender", "uifer");
        editor.putString("DOB", "ienrf");
        editor.putString("Password","ierf");
        editor.putString("Phone","oeirf");
        editor.putString("Address", "ermfer");
        editor.putString("Pincode", "feifer");
        editor.putString("State", "eiurfer");

//        // Store the profile photo URL if available
//        if (account.getPhotoUrl() != null) {
//            editor.putString("PhotoURL", account.getPhotoUrl().toString());
//        } else {
//            editor.putString("PhotoURL", ""); // Set an empty string if there's no photo URL
//        }

        // Add more attributes as needed

        editor.apply();
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
//                            Intent intent = new Intent(LoginOption.this,DisplayDetailsActivity.class);
//                            startActivity(intent);
//                        }else{
//                            Toast.makeText(LoginOption.this,"something went wrong ",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

//    private void saveUserDetails(String firstName, String lastName, String age, String gender, String dob, String email, String password, String phone, String address, String pincode, String selectedState) {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        // Store user details in SharedPreferences
//        editor.putString("FirstName", firstName);
//        editor.putString("LastName", lastName);
//        editor.putString("Age", age);
//        editor.putString("Gender", gender);
//        editor.putString("DOB", dob);
//        editor.putString("Email", email);
//        editor.putString("Password", password);
//        editor.putString("Phone", phone);
//        editor.putString("Address", address);
//        editor.putString("Pincode", pincode);
//        editor.putString("State", selectedState);
//        editor.apply();
//    }

    // ... Other code

//    private void firbaseAuth(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Retrieve user details
//                            GoogleSignInAccount account = task.getResult(ApiException.class);
//                            String firstName = account.getGivenName();
//                            String lastName = account.getFamilyName();
//                            String email = account.getEmail();
//
//                            // You may need to retrieve other user details from the account object
//
//                            // Call the method to save user details in SharedPreferences
//                            saveUserDetails(firstName, lastName,);
//
//                            // Start the DisplayDetailsActivity
//                            Intent intent = new Intent(LoginOption.this, DisplayDetailsActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(LoginOption.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}
