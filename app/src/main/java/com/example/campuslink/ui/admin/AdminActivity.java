package com.example.campuslink.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuslink.R;
import com.example.campuslink.databinding.ActivityAdminBinding;
import com.example.campuslink.ui.auth.LoginActivity;
import com.example.campuslink.ui.fees.FeesActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.btnSendNotice.setOnClickListener(v -> {
            Toast.makeText(this, "Notice sent functionality coming soon", Toast.LENGTH_SHORT).show();
        });

        binding.btnManageFees.setOnClickListener(v -> {
            Intent intent = new Intent(this, FeesActivity.class);
            startActivity(intent);
        });

        binding.btnUpdateTimetable.setOnClickListener(v -> {
            Toast.makeText(this, "Update Timetable functionality coming soon", Toast.LENGTH_SHORT).show();
        });

        binding.btnUploadAdmitCard.setOnClickListener(v -> {
            Toast.makeText(this, "Upload Admit Card functionality coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
