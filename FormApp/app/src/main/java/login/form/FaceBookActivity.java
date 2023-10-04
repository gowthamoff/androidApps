package login.form;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class FaceBookActivity extends LoginOption {

    CallbackManager callbackManager;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

//        progressBar = findViewById(R.id.progressBar);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user3 = auth.getCurrentUser();
                            // Fetch the user's Facebook profile picture
                            String profileImageUrl = user3.getPhotoUrl().toString();
                            Log.d("ProfileImageURL", profileImageUrl);

                            // Load the image using a library like Picasso or Glide
                            // For example, using Picasso:
                            Picasso.get().load(profileImageUrl).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    // Save the bitmap to SharedPreferences
                                    saveBitmapToSharedPreferences(bitmap);
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    // Handle image loading failure
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    // Handle image loading
                                }
                            });

                            updateUI(user3);
                        } else {
                            Toast.makeText(FaceBookActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            FirebaseUser user2 = auth.getCurrentUser();
            // Store user details in SharedPreferences
            editor.putString("FirstName", user2.getDisplayName());
            editor.putString("LastName", user2.getDisplayName());
            editor.putString("Email", "No Access");
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
        // Add a delay before moving to the next activity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToDisplayDetailsActivity();
            }
        }, 2000);
//        Intent intent = new Intent(FaceBookActivity.this, DisplayDetailsActivity.class);
//        startActivity(intent);
    }

    private void moveToDisplayDetailsActivity() {
        Intent intent = new Intent(FaceBookActivity.this, DisplayDetailsActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity so the user can't go back to it with the back button
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

}