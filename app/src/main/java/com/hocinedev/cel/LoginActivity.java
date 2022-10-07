package com.hocinedev.cel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.hocinedev.cel.databinding.ActivityLoginBinding;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private TextInputEditText textEmail;
    private TextInputEditText textPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView textViewSignUp;

    private String email;
    private String password;
    private ArrayList<UserObject> usersList;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        unit();
        onClick();
    }

    private void unit() {
        mAuth = FirebaseAuth.getInstance();
        textEmail = binding.editTextTextEmailAddress;
        textPassword = binding.editTextTextPassword;
        btnLogin = binding.btnLogin;
        progressBar = binding.progressCircular;
        textViewSignUp = binding.textViewSignUp;
    }

    private void onClick() {
        textViewSignUp.setOnClickListener(v -> {
            textViewSignUp.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(this, SignUpActivity.class));
        });

        binding.btnLogin.setOnClickListener(v -> {
            btnLogin.setEnabled(false);
            getValues();
            // check if email exists
            if (checkFields())
                // User signIn
                signIn(email, password);

        });
    }

    private void getValues() {
        email = textEmail.getText().toString();
        password = textPassword.getText().toString();
    }

    private boolean checkFields() {
        if (email.equals("")) {
            binding.textInputLayoutEmail.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnLogin.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutEmail.setError(null);
        }
        if (password.equals("")) {
            binding.textInputLayoutPassword.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnLogin.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutPassword.setError(null);
        }
        if (password.length() < 6) {
            binding.textInputLayoutPassword.setError(getString(R.string.txt_password_length));
            showSnackBar(getString(R.string.txt_password_length));
            btnLogin.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutPassword.setError(null);
        }
        return true;
    }

    private void signIn(String email, String password) {
        // [START create_user_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "createUserWithEmail:success");
                            showSnackBar(getString(R.string.txt_success_signup));
                            Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            showSnackBar(getString(R.string.txt_fail_to_signup));
                            btnLogin.setEnabled(true);
                        }
                    }

                });
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            btnLogin.setEnabled(true);
            setTheme(R.style.FullScreen);
            setContentView(binding.getRoot());
        }
    }


    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else
            showSnackBar(getString(R.string.txt_press_again));
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        textViewSignUp.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}