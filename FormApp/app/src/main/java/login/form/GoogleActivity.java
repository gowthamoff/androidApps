//package login.form;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class GoogleActivity extends AppCompatActivity {
//
//    private TextView userNameTextView;
//    private FirebaseAuth auth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_google);
//
//        userNameTextView = findViewById(R.id.userNameTextView);
//        auth = FirebaseAuth.getInstance();
//
//        // Get the current user from Firebase
//        FirebaseUser user = auth.getCurrentUser();
//
//        if (user != null) {
//            String userName = user.getDisplayName();
//            userNameTextView.setText("Welcome, " + userName);
//        }
//    }
//}
