package com.hocinedev.cel;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hocinedev.cel.databinding.ActivitySignUpBinding;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    private UserObject userObject;
    private ArrayList<String> arrayListKaser;
    private ArrayList<String> arrayListLevel;
    private DatabaseReference myRef;
    private ArrayList<String> existingEmails;

    private TextInputEditText textFirstLastName;
    private TextInputEditText textBirthDate;
    private AutoCompleteTextView textKaser;
    private TextInputEditText textPhone;
    private AutoCompleteTextView textLevel;
    private TextInputEditText textSpeciality;
    private TextInputEditText textOtherSkills;
    private TextInputEditText textUnivGraduate;
    private TextInputEditText textYearGraduate;
    private TextInputEditText textEmail;
    private TextInputEditText textPassword;
    private Button btnSignUp;
    private String firstLastName, birthDate, kaser, phone, level, speciality, otherSkills, univGraduate, yearGraduate, email, password;
    private long pressedTime;
    private boolean backPressedEnabled;
    private RelativeLayout layoutGraduate;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private boolean graduate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        unit();
        getDataFromFireBase();
        onClick();
    }


    private void unit() {
        textFirstLastName = binding.editTextFirstName;
        textBirthDate = binding.editTextBirthDate;
        textKaser = binding.AutoCompleteTextViewKaser;
        textPhone = binding.editTextPhoneNumber;
        textLevel = binding.AutoCompleteTextViewLevel;
        textSpeciality = binding.editTextSpeciality;
        textOtherSkills = binding.editTextOtherSkills;
        textUnivGraduate = binding.editTextUnivGraduate;
        textYearGraduate = binding.editTextYearGraduate;
        textEmail = binding.editTextTextEmailAddress;
        textPassword = binding.editTextTextPassword;
        btnSignUp = binding.btnSignUp;
        radioGroup = binding.radioGroup;
        layoutGraduate = binding.layoutGraduate;
        mAuth = FirebaseAuth.getInstance();
    }

    private void getDataFromFireBase() {
        getKasers();
        getLevels();
    }

    private void getKasers() {
        //INITIALIZE FB
        myRef = FirebaseDatabase.getInstance().getReference(Constants.KASER).getRef();
        myRef.keepSynced(true);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListKaser = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayListKaser.add(dataSnapshot.child(Constants.KASER_NAME).getValue().toString());
                }
                setUpAutoComplete(arrayListKaser, textKaser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLevels() {
        //INITIALIZE FB
        myRef = FirebaseDatabase.getInstance().getReference(Constants.LEVEL).getRef();
        myRef.keepSynced(true);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListLevel = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayListLevel.add(dataSnapshot.child(Constants.LEVEL).getValue().toString());
                }
                setUpAutoComplete(arrayListLevel, textLevel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void onClick() {
        textKaser.setOnFocusChangeListener((view, b) -> {
            if (b)
                hideKeyboard();
        });

        textLevel.setOnFocusChangeListener((view, b) -> {
            if (b)
                hideKeyboard();
        });

        textPassword.setOnClickListener(view -> binding.textInputLayoutPassword.setError(null));

        btnSignUp.setOnClickListener(view -> {
            btnSignUp.setEnabled(false);
            getValues();
            // check if email exists
            EmailExists();

        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            if (radioButton.getText().toString().equals(getString(R.string.txt_still_learning))) {
                graduate = false;
                layoutGraduate.animate().alpha(0.0f).setDuration(500).setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                layoutGraduate.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        }
                );
            } else if (radioButton.getText().toString().equals(getString(R.string.txt_graduate))) {
                graduate = true;
                layoutGraduate.animate().alpha(1.0f).setDuration(500).setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                layoutGraduate.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        }
                );
            }
        });
    }

    private boolean checkFields() {
        if (firstLastName.equals("")) {
            binding.textInputLayoutFirstLastName.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnSignUp.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutFirstLastName.setError(null);
        }
        if (birthDate.equals("")) {
            binding.textInputLayoutBirthDate.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnSignUp.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutBirthDate.setError(null);
        }
        if (kaser.equals("")) {
            binding.textInputLayoutKaser.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnSignUp.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutKaser.setError(null);
        }
        if (phone.equals("")) {
            binding.textInputLayoutPhoneNumber.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnSignUp.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutPhoneNumber.setError(null);
        }
        if (level.equals("")) {
            binding.textInputLayoutLevel.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnSignUp.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutLevel.setError(null);
        }
        if (speciality.equals("")) {
            binding.textInputLayoutSpeciality.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnSignUp.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutSpeciality.setError(null);
        }
        if (email.equals("")) {
            binding.textInputLayoutEmail.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnSignUp.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutEmail.setError(null);
        }
        if (password.equals("")) {
            binding.textInputLayoutPassword.setError(getString(R.string.txt_fill_in));
            showSnackBar(getString(R.string.txt_fill_field));
            btnSignUp.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutPassword.setError(null);
        }
        if (password.length() < 6) {
            binding.textInputLayoutPassword.setError(getString(R.string.txt_password_length));
            showSnackBar(getString(R.string.txt_password_length));
            btnSignUp.setEnabled(true);
            return false;
        } else {
            binding.textInputLayoutPassword.setError(null);
        }
        if (graduate) {
            if (univGraduate.equals("")) {
                binding.textInputLayoutUnivGraduate.setError(getString(R.string.txt_fill_in));
                showSnackBar(getString(R.string.txt_fill_field));
                btnSignUp.setEnabled(true);
                return false;
            } else {
                binding.textInputLayoutUnivGraduate.setError(null);
            }
            if (yearGraduate.equals("")) {
                binding.textInputLayoutYearGraduate.setError(getString(R.string.txt_fill_in));
                showSnackBar(getString(R.string.txt_fill_field));
                btnSignUp.setEnabled(true);
                return false;
            } else {
                binding.textInputLayoutYearGraduate.setError(null);
            }
        }
        return true;
    }

    private void EmailExists() {
        FirebaseFirestore.getInstance().collection(Constants.USERS)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean emailExists = false;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (email.equals(document.getString(Constants.EMAIL)))
                        if (!emailExists)
                            emailExists = true;
                }
                if (emailExists) {
                    alertDialogMessage(getString(R.string.txt_email_exists));
                    btnSignUp.setEnabled(true);
                } else {
                    if (checkFields())
                        singUP(email, password);
                }

            } else {
                // no user signed in
                if (checkFields())
                    singUP(email, password);
            }
        });
    }

    private void getValues() {
        firstLastName = textFirstLastName.getText().toString();
        birthDate = textBirthDate.getText().toString();
        kaser = textKaser.getText().toString();
        phone = textPhone.getText().toString();
        level = textLevel.getText().toString();
        speciality = textSpeciality.getText().toString();
        otherSkills = textOtherSkills.getText().toString();
        univGraduate = textUnivGraduate.getText().toString();
        yearGraduate = textYearGraduate.getText().toString();
        email = textEmail.getText().toString();
        password = textPassword.getText().toString();
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void setUpAutoComplete(ArrayList<String> arrayList, AutoCompleteTextView autoCompleteTextView) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item,
                R.id.text, arrayList);
        AutoCompleteTextView editTextFilledExposedDropdown = autoCompleteTextView;
        editTextFilledExposedDropdown.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private void singUP(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "createUserWithEmail:success");
                            String id = mAuth.getCurrentUser().getUid();
                            userObject = new UserObject(firstLastName, birthDate, kaser, phone, level, speciality, otherSkills, graduate, univGraduate, yearGraduate, email, password, System.currentTimeMillis());
                            SaveUserInfo(id);
                        } else {
                            // If sign in fails, display a message to the user.
                            //  Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            btnSignUp.setEnabled(true);
                            showSnackBar(getString(R.string.txt_fail_to_signup));
                        }
                    }

                    private void SaveUserInfo(String id) {
                        FirebaseFirestore.getInstance().collection(Constants.USERS)
                                .document(id).set(userObject).addOnSuccessListener(documentReference -> {
                            backPressedEnabled = true;
                            Toast.makeText(SignUpActivity.this, getString(R.string.txt_success_sign_up), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUpActivity.this, DashBoardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(SignUpActivity.this, getString(R.string.txt_fail_to_signup), Toast.LENGTH_LONG).show();
                        });

                        btnSignUp.setEnabled(true);

                    }
                });
    }

    private void alertDialogMessage(String text) {
        final View dialogView = View.inflate(this
                , R.layout.custom_message_dialog, null);
        final AlertDialog alertDialogError = new AlertDialog.Builder(this).create();
        alertDialogError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialogError.setCancelable(false);

        Button btnOk = dialogView.findViewById(R.id.outlinedButtonExit);
        TextView textError = dialogView.findViewById(R.id.textErrorMessage);
        textError.setText(text);

        btnOk.setOnClickListener(view -> {
            alertDialogError.dismiss();
        });
        alertDialogError.setView(dialogView);
        alertDialogError.show();
    }

    @Override
    public void onBackPressed() {
        if (!backPressedEnabled)
            super.onBackPressed();
        else if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else
            showSnackBar(getString(R.string.txt_press_again));
        pressedTime = System.currentTimeMillis();
    }
}