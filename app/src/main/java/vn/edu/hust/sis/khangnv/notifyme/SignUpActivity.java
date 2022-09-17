package vn.edu.hust.sis.khangnv.notifyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEdt, password, fullNameEdt, confirmPasswordEdt;
    private Button signUpBtn;
    private TextView notificationTxt, txtSignInBt;
    private ProgressDialog progressDialog;
    private static final String LOG_TAG = SignUpActivity.class.getSimpleName();
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // init UI
        initUI();

        // init listener
        initListener();
    }

    private void initUI() {
        emailEdt = (EditText) findViewById(R.id.email);
        fullNameEdt = (EditText) findViewById(R.id.fullName);
        password = (EditText) findViewById(R.id.password);
        confirmPasswordEdt = (EditText) findViewById(R.id.repassword);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        notificationTxt = (TextView) findViewById(R.id.txtNotificationSignUp);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        txtSignInBt = findViewById(R.id.txtSignInBt);
        mAuth = FirebaseAuth.getInstance();

    }

    private void initListener(){
        signUpBtn.setOnClickListener(view -> {
            String emailValue = emailEdt.getText().toString().trim();
            String usernameValue = fullNameEdt.getText().toString().trim();
            String passwordValue = password.getText().toString().trim();
            String confirmPasswordValue = confirmPasswordEdt.getText().toString().trim();

            // validate
            boolean isValid = validate(emailValue, usernameValue, passwordValue, confirmPasswordValue);

            if(isValid) {
                // call API
                progressDialog.setTitle("Register");
                progressDialog.setMessage("Please đợi");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                createAccount(usernameValue, emailValue, passwordValue);
            }
        });

        txtSignInBt.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    // Validate
    private boolean validate(String emailValue, String fullNameValue, String passwordValue, String confirmPasswordValue) {
        if(emailValue.isEmpty() || passwordValue.isEmpty() || fullNameValue.isEmpty() || confirmPasswordValue.isEmpty() ){
            notificationTxt.setText("R.string.warning_enter_all_fields");
            notificationTxt.setVisibility(View.VISIBLE);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            notificationTxt.setText("R.string.warning_email_invalid");
            notificationTxt.setVisibility(View.VISIBLE);
        } else if (passwordValue.compareTo(confirmPasswordValue) != 0){
            notificationTxt.setText("R.string.warning_confirm_pass_wrong");
            notificationTxt.setVisibility(View.VISIBLE);
        } else if(passwordValue.length() < 6) {
            notificationTxt.setText("R.string.warning_length_pass");
            notificationTxt.setVisibility(View.VISIBLE);
        } else {
            return true;
        }
        return false;
    }

    private void createAccount(String username, String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail: success");
                            // FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);

                            User user = new User(username, email, password);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(task1 -> {
                                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

}