package com.example.tourismapp;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private static final int IMAGE_REQUEST_CODE = 100;
    private String USER_ID;
    private DrawerLayout drawerLayout;
    private EditText firstNameEditText, lastNameEditText, usernameEditText;
    private Spinner genderSpinner;
    private Button updateButton, uploadImageButton,cancelButton,confirmButton;
    private ImageView profileImageView;
    private TextView usernameView;
    private TextInputLayout confirmPasswordEditLayout;
    private TextInputEditText passwordEditText,confirmPasswordEditText;
    private Uri imageUri;
    private boolean isImageUploaded = false;
    AuthUser authUser;
    private ImageView profileImage;
    private  TextView userEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         authUser=new AuthUser();

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        genderSpinner = findViewById(R.id.genderSpinnerUpdate);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        updateButton = findViewById(R.id.updateButton);
        cancelButton = findViewById(R.id.btnCancel);
        confirmButton = findViewById(R.id.btnConfirm);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        profileImageView = findViewById(R.id.profile_image_view);
        usernameView=findViewById(R.id.usernameView);
        confirmPasswordEditLayout=findViewById(R.id.confirmPasswordInputLayout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        // Get header view to access header elements
        View headerView = navigationView.getHeaderView(0);
        profileImage = headerView.findViewById(R.id.profile_image);
        userEmail = headerView.findViewById(R.id.user_email);
        userEmail.setText(AuthUser.username);

        if (AuthUser.profileImage != null && !AuthUser.profileImage.trim().isEmpty()) {
            profileImageView.setImageBitmap(decodeBase64Image());
            profileImage.setImageBitmap(decodeBase64Image());

        } else {
            if (AuthUser.gender.equals("Male")) {
                profileImageView.setImageResource(R.drawable.ic_male);  // Replace with your default image
                profileImage.setImageResource(R.drawable.ic_male);  // Replace with your default image
            } else if (AuthUser.gender.equals("Female")) {
                profileImageView.setImageResource(R.drawable.ic_female);  // Replace with your default image
                profileImage.setImageResource(R.drawable.ic_female);  // Replace with your default image
            }
        }


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
                uploadImageButton.setVisibility(View.VISIBLE);
            }
        });


        uploadImageButton.setOnClickListener(v->openImageSelector());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeComponents(false);
                confirmButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                updateButton.setVisibility((View.VISIBLE));
                confirmPasswordEditText.setVisibility(View.INVISIBLE);
                confirmPasswordEditLayout.setVisibility(View.GONE);
                uploadImageButton.setVisibility(View.GONE);

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String gender = genderSpinner.getSelectedItem().toString();
                String username = usernameEditText.getText().toString().trim();
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
                String encodedImage=null;
                if (imageUri != null) {
                    encodedImage = encodeImageToBase64(imageUri);
            }


                boolean updateSuccessful =AuthUser.updateUserDetails(USER_ID, firstName, lastName, gender,username, password,encodedImage);

                if (updateSuccessful) {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    changeComponents(false);
                    confirmButton.setVisibility(View.INVISIBLE);
                    cancelButton.setVisibility(View.INVISIBLE);
                    updateButton.setVisibility((View.VISIBLE));
                    confirmPasswordEditText.setVisibility(View.INVISIBLE);
                    confirmPasswordEditLayout.setVisibility(View.GONE);
                    uploadImageButton.setVisibility(View.GONE);

//                    NavigationView navigationView = findViewById(R.id.nav_view);
//
//                    // Get header view to access header elements
//                    View headerView = navigationView.getHeaderView(0);
//                    profileImage = headerView.findViewById(R.id.profile_image);
//
//                    if (AuthUser.profileImage != null && !AuthUser.profileImage.trim().isEmpty()) {
//                        profileImageView.setImageBitmap(decodeBase64Image());
//                        profileImage.setImageBitmap(decodeBase64Image());
//                    } else {
//                        if (AuthUser.gender.equals("Male")) {
//                            profileImageView.setImageResource(R.drawable.ic_male);  // Replace with your default image
//                            profileImage.setImageResource(R.drawable.ic_male);  // Replace with your default image
//                        } else if (AuthUser.gender.equals("Female")) {
//                            profileImageView.setImageResource(R.drawable.ic_female);  // Replace with your default image
//                            profileImage.setImageResource(R.drawable.ic_female);  // Replace with your default image
//                        }
//                    }
                        refreshPage();
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

    private void refreshPage() {
        finish();
        startActivity(getIntent());
    }

    private void changeComponents(boolean value) {
        firstNameEditText.setEnabled(value);
        lastNameEditText.setEnabled(value);
        genderSpinner.setEnabled(value);
        genderSpinner.setFocusable(value);
        genderSpinner.setFocusableInTouchMode(value);
        passwordEditText.setEnabled(value);
        confirmPasswordEditText.setEnabled(value);
        uploadImageButton.setEnabled(value);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri); // Display the selected image
            isImageUploaded = true;
        } else {
            Toast.makeText(this, "Image selection failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    private String encodeImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap decodeBase64Image(){

        String base64Image = AuthUser.profileImage;
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                // Decode the base64 string into bytes
                byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);

                // Convert bytes into a Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                Bitmap circularBitmap = getCircularBitmap(bitmap);
                return circularBitmap;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid image format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No profile image found", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        // Creating a new Bitmap with the width and height of the smallest dimension (for the circle)
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        // Creating a Canvas to draw a circular shape on the new Bitmap
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        // Draw a circle
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);

        // Set the Bitmap as the source for the canvas
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, (size - bitmap.getWidth()) / 2, (size - bitmap.getHeight()) / 2, paint);

        return output;
    }

}
