package com.hocinedev.cel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.hocinedev.cel.Fragments.HomeFragment;
import com.hocinedev.cel.Fragments.ProfileFragment;
import com.hocinedev.cel.Fragments.SearchFragment;
import com.hocinedev.cel.databinding.ActivityDashBoardBinding;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityDashBoardBinding binding;
    private static final String TAG = "DashBoardActivity";

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private TextView textNotification;
    private int notificationCount = 0;
    private String userId;

    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbarMain;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // initialisation
        unit();

        // OnClick methods
        onClick();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                    new HomeFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.home);
        }

    }

    private void unit() {
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navigationView;
        bottomNavigationView = binding.bottomNavigationView;

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void onClick() {
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Drawer navigation click
        bottomNavigationView.setOnItemSelectedListener(item -> {
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i)
                fm.popBackStack();

            switch (item.getItemId()) {
                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            new ProfileFragment()).commit();
                    return true;
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            new HomeFragment()).commit();
                    return true;
                case R.id.search:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            new SearchFragment()).commit();
                    return true;
            }
            return false;
        });
    }

    // Bottom navigation click
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_mail:
                // startActivity(getOpenMailIntent());
                break;
            case R.id.nav_terms:
//                Intent intent = new Intent(ActivityMain.this, ActivityTerms.class);
//                new Handler().postDelayed(() -> {
//                }, 200);
                //openTerms();
                break;
            case R.id.nav_logout:
                logOut();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.txt_logout))
                .setMessage(getString(R.string.txt_logout_confirmation))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.txt_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with exit operation
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.txt_no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else
            showSnackBar(getString(R.string.txt_press_again), true);
        pressedTime = System.currentTimeMillis();
    }

    private void showSnackBar(String message, boolean shortMessage) {
        Snackbar snackbar;
        if (shortMessage)
            snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        else
            snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}