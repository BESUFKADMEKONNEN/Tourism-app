package com.example.tourismapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public abstract class NavParent extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Toolbar
//        setContentView(R.layout.activity_main); // Set a default content view for the parent

        // Set the layout to the one provided by the child activity
        setContentView(getContentLayoutId());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout and ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Get header view to access header elements
        View headerView = navigationView.getHeaderView(0);
        profileImage = headerView.findViewById(R.id.profile_image);
        TextView userEmail = headerView.findViewById(R.id.user_email);

        // Simulating authenticated user data
        String haveProfileImage = AuthUser.profileImageUrl;  // Get the base64 image string
        if (haveProfileImage != null && !haveProfileImage.isEmpty()) {
            profileImage.setImageBitmap(decodeBase64Image());
        } else {
            if (AuthUser.gender!=null&&AuthUser.gender.equals("Male")) {
                profileImage.setImageResource(R.drawable.ic_male);  // Replace with your default image
            } else if (AuthUser.gender.equals("Female")) {
                profileImage.setImageResource(R.drawable.ic_female);  // Replace with your default image
            }
        }
        String authenticatedEmail = AuthUser.firstName;
        userEmail.setText(authenticatedEmail);

        // Set a click listener for the profile image
        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the navigation item selected listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigation(item);
            }
        });


    }

    protected abstract int getContentLayoutId();


    // Abstract method to be implemented by child classes for navigation handling
    protected boolean handleNavigation(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        } else if (id == R.id.nav_booked_places) {
            startActivity(new Intent(this, BookedPlacesActivity.class));
            finish();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
            finish();
        } else if (id == R.id.nav_help) {
            Toast.makeText(this, "Help is on development", Toast.LENGTH_SHORT).show();
            finish();
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_exit_app) {
            Toast.makeText(this, "Thank you for being with us.", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
        drawerLayout.closeDrawers(); // Close the drawer after clicking
        return true;
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
                Toast.makeText(this, "Invalid image format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No profile image found", Toast.LENGTH_SHORT).show();
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

}
