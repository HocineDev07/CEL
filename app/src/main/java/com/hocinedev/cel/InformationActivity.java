package com.hocinedev.cel;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hocinedev.cel.databinding.ActivityInformationBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InformationActivity extends AppCompatActivity {

    private ActivityInformationBinding binding;
    private static final String TAG = "InformationActivity";
    private String id;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        unit();
        loadInfo();
    }

    private void loadInfo() {
        FirebaseFirestore.getInstance().collection(Constants.USERS).document(id)
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                binding.textViewFirstLastName.setText(documentSnapshot.getString(Constants.FIRST_LAST_NAME));
                binding.textViewBirthDate.setText(documentSnapshot.getString(Constants.BIRTH_DATE));
                binding.textViewKaser.setText(documentSnapshot.getString(Constants.KASER));
                binding.textViewPhone.setText(documentSnapshot.getString(Constants.PHONE));
                binding.textViewLevel.setText(documentSnapshot.getString(Constants.MIN_LEVEL));
                binding.textViewSpeciality.setText(documentSnapshot.getString(Constants.SPECIALITY));
                if (documentSnapshot.getBoolean(Constants.GRADUATE)) {
                    binding.textViewStatus.setText(getString(R.string.txt_graduate));
                    binding.textViewStatus.setTextColor(getResources().getColor(R.color.primary));
                    binding.imageGraduate.setImageResource(R.drawable.ic_graduate);
                    binding.layoutUniv.setVisibility(View.VISIBLE);
                    binding.layoutYearGraduate.setVisibility(View.VISIBLE);
                    binding.textViewUniv.setText(documentSnapshot.getString(Constants.UNIV_GRADUATE));
                    binding.textViewYearGraduate.setText(documentSnapshot.getString(Constants.YEAR_GRADUATE));
                }
                binding.textViewOtherSkills.setText(documentSnapshot.getString(Constants.OTHER_SKILLS));
                binding.textViewDateInscription.setText(simpleDateFormat.format(
                        new Date(documentSnapshot.getLong(Constants.SIGNUP_DATE))));

            }
        });
    }

    private void unit() {
        id = getIntent().getStringExtra(Constants.ID);
        simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    }
}