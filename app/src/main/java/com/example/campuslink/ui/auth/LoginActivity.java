package com.example.campuslink.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campuslink.FirebaseManager;
import com.example.campuslink.data.local.CampusDatabase;
import com.example.campuslink.data.local.Student;
import com.example.campuslink.databinding.ActivityLoginBinding;
import com.example.campuslink.ui.admin.AdminActivity;
import com.example.campuslink.ui.faculty.FacultyActivity;
import com.example.campuslink.ui.home.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private CampusDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseManager.initialize(this);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        db = CampusDatabase.getDatabase(this);

        // Auto-login check
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            fetchUserRoleAndNavigate(currentUser.getUid());
        }

        binding.btnLogin.setOnClickListener(v -> loginUser());
        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            fetchUserRoleAndNavigate(user.getUid());
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Auth Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserRoleAndNavigate(String uid) {
        // Try local cache first for instant experience (Requirement: Room Offline support)
        executorService.execute(() -> {
            Student localUser = db.studentDao().getStudentById(uid);
            if (localUser != null) {
                runOnUiThread(() -> {
                    binding.progressBar.setVisibility(View.GONE);
                    navigateToHome(localUser.role);
                });
            } else {
                // If not in local DB, fetch from Firestore (Requirement: Robust Backend)
                fStore.collection("users").document(uid).get()
                        .addOnCompleteListener(task -> {
                            binding.progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot doc = task.getResult();
                                String role = doc.getString("role");
                                String name = doc.getString("name");
                                String email = doc.getString("email");
                                String feeStatus = doc.getString("feeStatus");

                                // Save to local for next time
                                executorService.execute(() -> {
                                    db.studentDao().insert(new Student(uid, name, email, role, feeStatus));
                                });

                                navigateToHome(role);
                            } else {
                                Toast.makeText(LoginActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void navigateToHome(String role) {
        Intent intent;
        if ("Admin".equals(role)) {
            intent = new Intent(this, AdminActivity.class);
        } else if ("Faculty".equals(role)) {
            intent = new Intent(this, FacultyActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
