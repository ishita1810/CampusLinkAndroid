package com.example.campuslink.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campuslink.FirebaseManager;
import com.example.campuslink.data.local.CampusDatabase;
import com.example.campuslink.data.local.Student;
import com.example.campuslink.databinding.ActivityRegisterBinding;
import com.example.campuslink.ui.admin.AdminActivity;
import com.example.campuslink.ui.faculty.FacultyActivity;
import com.example.campuslink.ui.home.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private DatabaseReference mDatabase;
    private CampusDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseManager.initialize(this);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://campuslinkandroid-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        db = CampusDatabase.getDatabase(this);

        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }

        binding.btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (binding.spinnerRole.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            return;
        }
        String role = binding.spinnerRole.getSelectedItem().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            binding.etPassword.setError("Password must be at least 6 characters");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToAllBackends(firebaseUser, name, email, role);
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnRegister.setEnabled(true);
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(RegisterActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToAllBackends(FirebaseUser firebaseUser, String name, String email, String role) {
        String userId = firebaseUser.getUid();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("email", email);
        userInfo.put("role", role);
        userInfo.put("feeStatus", "Pending");

        // Save to Realtime Database and Firestore, then proceed to local DB and navigation
        mDatabase.child("users").child(userId).setValue(userInfo);
        fStore.collection("users").document(userId).set(userInfo)
                .addOnCompleteListener(task -> {
                    // Even if network save is pending, save locally and go to dashboard
                    saveToLocalDB(userId, name, email, role);
                });
    }

    private void saveToLocalDB(String userId, String name, String email, String role) {
        executorService.execute(() -> {
            try {
                // Upsert logic: insert or update the local record
                Student newUser = new Student(userId, name, email, role, "Pending");
                db.studentDao().insert(newUser);
            } catch (Exception e) {
                Log.e(TAG, "Local DB insert failed (probably already exists)", e);
            }

            runOnUiThread(() -> {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();
                navigateToHome(role);
            });
        });
    }

    private void navigateToHome(String role) {
        Intent intent;
        String normalizedRole = role.toLowerCase();

        if (normalizedRole.contains("admin")) {
            intent = new Intent(this, AdminActivity.class);
        } else if (normalizedRole.contains("faculty")) {
            intent = new Intent(this, FacultyActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
