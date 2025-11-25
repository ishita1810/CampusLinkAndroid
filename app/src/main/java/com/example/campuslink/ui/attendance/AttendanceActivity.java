package com.example.campuslink.ui.attendance;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.campuslink.data.local.AttendanceRecord;
import com.example.campuslink.data.local.CampusDatabase;
import com.example.campuslink.data.local.Student;
import com.example.campuslink.databinding.ActivityAttendanceBinding;
import com.example.campuslink.ui.adapter.StudentAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AttendanceActivity extends AppCompatActivity implements StudentAdapter.OnStudentAttendanceChangedListener {

    private ActivityAttendanceBinding binding;
    private StudentAdapter adapter;
    private CampusDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Map<String, Boolean> attendanceMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.rvStudents.setLayoutManager(new LinearLayoutManager(this));
        binding.btnSaveAttendance.setOnClickListener(v -> saveAttendance());

        db = CampusDatabase.getDatabase(this);

        loadStudents();
    }

    private void loadStudents() {
        executorService.execute(() -> {
            List<Student> studentList = db.studentDao().getAll();
            runOnUiThread(() -> {
                adapter = new StudentAdapter(studentList, this);
                binding.rvStudents.setAdapter(adapter);
            });
        });
    }

    @Override
    public void onStudentAttendanceChanged(Student student, boolean isPresent) {
        attendanceMap.put(student.id, isPresent);
    }

    private void saveAttendance() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        executorService.execute(() -> {
            for (Map.Entry<String, Boolean> entry : attendanceMap.entrySet()) {
                AttendanceRecord record = new AttendanceRecord();
                record.studentId = entry.getKey();
                record.isPresent = entry.getValue();
                record.date = currentDate;
                db.attendanceRecordDao().insert(record);
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Attendance Saved", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
