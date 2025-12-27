package com.example.campuslink.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuslink.R;
import com.example.campuslink.data.local.CampusDatabase;
import com.example.campuslink.data.local.Student;
import com.example.campuslink.model.HomeGridItem;
import com.example.campuslink.ui.adapter.HomeGridAdapter;
import com.example.campuslink.ui.attendance.AttendanceActivity;
import com.example.campuslink.ui.auth.LoginActivity;
import com.example.campuslink.ui.events.EventsActivity;
import com.example.campuslink.ui.fees.FeesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements HomeGridAdapter.OnItemClickListener {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private CampusDatabase db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = CampusDatabase.getDatabase(this);
        mAuth = FirebaseAuth.getInstance();

        ImageButton btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> signOut());

        RecyclerView rvFrequentlyUsed = findViewById(R.id.rvFrequentlyUsed);
        rvFrequentlyUsed.setLayoutManager(new GridLayoutManager(this, 3));

        List<HomeGridItem> itemList = new ArrayList<>();
        itemList.add(new HomeGridItem("Attendance", R.drawable.ic_attendance));
        itemList.add(new HomeGridItem("Events", R.drawable.ic_events));
        itemList.add(new HomeGridItem("Fees", R.drawable.ic_fees));
        itemList.add(new HomeGridItem("Performance", R.drawable.ic_performance));
        itemList.add(new HomeGridItem("Report Card", R.drawable.ic_report_card));
        itemList.add(new HomeGridItem("Timetable", R.drawable.ic_timetable));

        HomeGridAdapter adapter = new HomeGridAdapter(itemList, this);
        rvFrequentlyUsed.setAdapter(adapter);

        loadUserName();
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loadUserName() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            executorService.execute(() -> {
                Student student = db.studentDao().getStudentById(userId);
                runOnUiThread(() -> {
                    if (student != null) {
                        TextView tvWelcome = findViewById(R.id.tvWelcome);
                        tvWelcome.setText("Welcome, " + student.name);
                    }
                });
            });
        }
    }

    @Override
    public void onItemClick(HomeGridItem item) {
        switch (item.getName()) {
            case "Attendance":
                startActivity(new Intent(this, AttendanceActivity.class));
                break;
            case "Fees":
                startActivity(new Intent(this, FeesActivity.class));
                break;
            case "Events":
                startActivity(new Intent(this, EventsActivity.class));
                break;
            // Add other cases here
        }
    }
}
