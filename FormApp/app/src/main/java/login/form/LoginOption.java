package login.form;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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

public class LoginOption extends AppCompatActivity {

    ImageView googleAuth;
    ImageView facebookAuth;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1000;
//    private ProgressDialog progressDialog;
    ProgressBar progressBar2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar2 = findViewById(R.id.progressBar);
        progressBar2.setVisibility(View.INVISIBLE);
//        progressBar2.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        Button registerButton = findViewById(R.id.registerButton);

//        progressDialog = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch MainActivity
                Intent intent = new Intent(LoginOption.this, MainActivity.class);
                startActivity(intent);
            }
        });

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

        facebookAuth = findViewById(R.id.btnFbAuth);
        facebookAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOption.this, FaceBookActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

    }

    private void googleSignIn() {
//        progressDialog.show();
        progressBar2.setVisibility(View.VISIBLE);
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firbaseAuth(account.getIdToken(), account.getPhotoUrl().toString()); // Pass the photo URL
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

    private void firbaseAuth(String idToken, final String photoUrl) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Dismiss the loading dialog when authentication is complete
//                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            LoadImage(photoUrl);
                            saveUserDetails();
                            int delayMillis = 1000;
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(LoginOption.this, DisplayDetailsActivity.class);
                                    startActivity(intent);
                                }
                            }, delayMillis);
                        } else {
                            Toast.makeText(LoginOption.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void LoadImage(String imageUrl) {
        Log.d("LoadImage", "Loading image from URL: " + imageUrl);
        // Use Glide to load and cache the image from the URL
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        // The image is loaded successfully into 'resource'
                        // Save the loaded Bitmap to SharedPreferences
                        saveBitmapToSharedPreferences(resource);
                    }
                });
    }

    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        Log.d("saveBitmapToSharedPreferences", "Saving bitmap to SharedPreferences");
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the bitmap to a Base64 string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String imageBase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        editor.putString("ProfileImage", imageBase64);

        editor.apply();
    }

    private void saveUserDetails() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        FirebaseUser user = auth.getCurrentUser();
        // Store user details in SharedPreferences
        editor.putString("FirstName", user.getDisplayName());
        editor.putString("LastName", user.getDisplayName());
        editor.putString("Email", user.getEmail());
        editor.putString("Age", "No Access");
        editor.putString("Gender", "No Access");
        editor.putString("DOB", "No Access");
        editor.putString("Password", "No Access");
        editor.putString("Phone", "No Access");
        editor.putString("Address", "No Access");
        editor.putString("Pincode", "No Access");
        editor.putString("State", "No Access");

        editor.apply();
    }
}
