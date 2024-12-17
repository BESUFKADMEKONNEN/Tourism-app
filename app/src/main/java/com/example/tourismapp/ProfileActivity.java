package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private String USER_ID;
    private DrawerLayout drawerLayout;
    private EditText firstNameEditText, lastNameEditText, usernameEditText;
    private Spinner genderSpinner;
    private Button updateButton, uploadImageButton,cancelButton,confirmButton;
    private ImageView profileImageView;
    private TextView usernameView;
    private TextInputLayout confirmPasswordEditLayout;
    private TextInputEditText passwordEditText,confirmPasswordEditText;
    AuthUser authUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         authUser=new AuthUser();


        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        updateButton = findViewById(R.id.updateButton);
        cancelButton = findViewById(R.id.btnCancel);
        confirmButton = findViewById(R.id.btnConfirm);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        profileImageView = findViewById(R.id.profileImageView);
        usernameView=findViewById(R.id.usernameView);
        confirmPasswordEditLayout=findViewById(R.id.confirmPasswordInputLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);


        USER_ID=AuthUser.userId;
        firstNameEditText.setText(AuthUser.firstName);
        lastNameEditText.setText(AuthUser.lastName);
        if (AuthUser.gender != null) {
            int genderPos = AuthUser.gender.equals("Male") ? 1 : AuthUser.gender.equals("Female") ? 2 : 0;
            genderSpinner.setSelection(genderPos); // Default to "Sex" if gender is null or invalid
        } else {
            genderSpinner.setSelection(0);
        }
         usernameEditText.setText(AuthUser.username);
         usernameView.setText(AuthUser.username);
         usernameView.setHint("You Can't Change The Username!");
        passwordEditText.setText(AuthUser.password);
        confirmPasswordEditText.setText(AuthUser.password);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeComponents(true);
                confirmButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                updateButton.setVisibility(View.GONE);
                confirmPasswordEditText.setVisibility(View.VISIBLE);
                confirmPasswordEditLayout.setVisibility(View.VISIBLE);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeComponents(false);
                confirmButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                updateButton.setVisibility((View.VISIBLE));
                confirmPasswordEditText.setVisibility(View.INVISIBLE);
            confirmPasswordEditLayout.setVisibility(View.GONE);
            }
        });




        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String gender = genderSpinner.getSelectedItem().toString();
                String username=usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (firstName.isEmpty() || lastName.isEmpty() || gender.equals("Select Gender")) {
                    Toast.makeText(ProfileActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gender.equals("Sex")) {
                    Toast.makeText(ProfileActivity.this, "Sex cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(ProfileActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Assuming you have a method to update user details in the AuthUser class or backend
                boolean updateSuccessful =AuthUser.updateUserDetails(USER_ID, firstName, lastName, gender,username, password);

                if (updateSuccessful) {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    changeComponents(false);
                    confirmButton.setVisibility(View.INVISIBLE);
                    cancelButton.setVisibility(View.INVISIBLE);
                    updateButton.setVisibility((View.VISIBLE));
                    confirmPasswordEditText.setVisibility(View.INVISIBLE);
                    confirmPasswordEditLayout.setVisibility(View.GONE);

                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout and ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                } else if (id == R.id.nav_booked_places) {
                    startActivity(new Intent(ProfileActivity.this, BookedPlacesActivity.class));

                } else if (id == R.id.nav_about) {
                    Toast.makeText(ProfileActivity.this, "You are already in About", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_help) {
                    Toast.makeText(ProfileActivity.this, "Help is on development", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                } else if (id == R.id.nav_exit_app) {
                    Toast.makeText(ProfileActivity.this, "Thank you for being with us.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    private void changeComponents(boolean value) {
        firstNameEditText.setEnabled(value);
        lastNameEditText.setEnabled(value);
        genderSpinner.setEnabled(value);
        passwordEditText.setEnabled(value);
        confirmPasswordEditText.setEnabled(value);
        uploadImageButton.setEnabled(value);
    }



}
