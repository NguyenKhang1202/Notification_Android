package vn.edu.hust.sis.khangnv.notifyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginBtn;
    private TextView forgotPassword, signUp, notificationTxt;
    private static final String LOG_TAG = SignInActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPasswordSignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        // init UI
        initUI();

        // init listener
        initLister();

    }
    private void initUI() {
        emailField = (EditText) findViewById(R.id.emailEdt);
        passwordField = (EditText) findViewById(R.id.passwordEdt);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        forgotPassword = (TextView) findViewById(R.id.forgotpass);
        signUp = (TextView) findViewById(R.id.signUp);
        notificationTxt = (TextView) findViewById(R.id.txtNotification);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void initLister() {
        loginBtn.setOnClickListener(view -> {
            notificationTxt.setVisibility(View.GONE);

            String emailValue = emailField.getText().toString().trim();
            String passwordValue = passwordField.getText().toString().trim();

            // validate input
            boolean isValid = validate(emailValue, passwordValue);

            if(isValid) {
                signInWithEmailAndPassword(emailValue, passwordValue);
            }
        });

        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(view -> Log.d("SignIn activity", "Forgot password"));
    }

    // validation
    private boolean validate(String emailValue, String passwordValue) {
        if(emailValue.isEmpty() || passwordValue.isEmpty()){
            notificationTxt.setText("R.string.warning_enter_all_fields");
            notificationTxt.setVisibility(View.VISIBLE);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            notificationTxt.setText("R.string.warning_email_invalid");
            notificationTxt.setVisibility(View.VISIBLE);
        } else {
            return true;
        };
        return false;
    }
    private void reload() { }

    private void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Name, email address, and profile photo Url
                                String name = user.getDisplayName();
                                String email = user.getEmail();



                                // Check if user's email is verified
                                boolean emailVerified = user.isEmailVerified();

                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getIdToken() instead.
                                String uid = user.getUid();
                            }

                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

    }
}