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

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ProfileActivity extends NavParent {
    private static final int IMAGE_REQUEST_CODE = 100;
    private String USER_ID;
//    private DrawerLayout drawerLayout;
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
    private  TextView userEmail;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_profile;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
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


        if (AuthUser.profileImageUrl != null && !AuthUser.profileImageUrl.trim().isEmpty()) {
            profileImageView.setImageBitmap(decodeBase64Image());

        } else {
            if (AuthUser.gender.equals("Male")) {
                profileImageView.setImageResource(R.drawable.ic_male);  // Replace with your default image
            } else if (AuthUser.gender.equals("Female")) {
                profileImageView.setImageResource(R.drawable.ic_female);  // Replace with your default image
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

        uploadImageButton.setOnClickListener(v -> openImageSelector());

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

                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                String encodedImage = null;
                if (imageUri != null) {
                    encodedImage = encodeImageToBase64(imageUri);

                }

                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                String userId = AuthUser.userId;
                if (userId != null) {
                    AuthUser authUser = new AuthUser(firstName,lastName,gender,username,password,userId,encodedImage);
                    User newUser = new User(firstName, lastName, gender, username, hashedPassword, encodedImage);
                    database.child("users").child(userId).setValue(newUser)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                                    changeComponents(false);
                                    confirmButton.setVisibility(View.INVISIBLE);
                                    cancelButton.setVisibility(View.INVISIBLE);
                                    updateButton.setVisibility((View.VISIBLE));
                                    confirmPasswordEditText.setVisibility(View.INVISIBLE);
                                    confirmPasswordEditLayout.setVisibility(View.GONE);
                                    uploadImageButton.setVisibility(View.GONE);
                                    refreshPage();
                                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }


        });}



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
            if (imageUri != null) {
                try {
                    // Load the image into a Bitmap
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);

                    // Display the image in the ImageView
                    profileImageView.setImageBitmap(selectedImage);

                    // Optionally encode to Base64
                    String encodedImage = encodeImageToBase64(imageUri);
                    isImageUploaded = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    private Bitmap decodeBase64Image() {
                String base64Image = AuthUser.profileImageUrl;
                if (base64Image != null && !base64Image.isEmpty()) {
                    try {
                        byte[] decodedBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                        return getCircularBitmap(bitmap);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        Toast.makeText(ProfileActivity.this, "Invalid image format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "No profile image found", Toast.LENGTH_SHORT).show();
                }
                return null;
            }


    private Bitmap getCircularBitmap(Bitmap bitmap) {
                int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
                Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                paint.setDither(true);
                canvas.drawCircle(size / 2, size / 2, size / 2, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(bitmap, (size - bitmap.getWidth()) / 2, (size - bitmap.getHeight()) / 2, paint);
                return output;
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


}
