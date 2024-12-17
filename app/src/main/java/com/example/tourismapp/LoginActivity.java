package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnSignup;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    // Static instance to hold authenticated user info
    public static AuthUser authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.usernameEditText);
        edtPassword = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.loginButton);
        btnSignup = findViewById(R.id.signupButton);

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");  // Assuming users are stored under the "users" node

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Perform the login logic
                loginUser(username, password);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void loginUser(final String username, final String password) {
        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        if (user != null && user.password.equals(password)) {
                            // Login successful
                            authUser = new AuthUser(user.firstName,user.lastName,user.gender,user.username,user.password, snapshot.getKey()); // Store authenticated user info
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                            // Proceed to next activity (e.g., MainActivity)
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    // If password does not match
                    Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                } else {
                    // Username not found in Firebase
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error accessing database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
