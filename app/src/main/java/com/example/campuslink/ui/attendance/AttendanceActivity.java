package com.example.campuslink.ui.attendance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuslink.R;
import com.example.campuslink.data.local.AttendanceRecord;
import com.example.campuslink.data.local.CampusDatabase;
import com.example.campuslink.data.local.Student;
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

    private StudentAdapter adapter;
    private CampusDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Map<String, Boolean> attendanceMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        RecyclerView rvStudents = findViewById(R.id.rvStudents);
        rvStudents.setLayoutManager(new LinearLayoutManager(this));

        Button btnSaveAttendance = findViewById(R.id.btnSaveAttendance);
        btnSaveAttendance.setOnClickListener(v -> saveAttendance());

        db = CampusDatabase.getDatabase(this);

        executorService.execute(() -> {
            if (db.studentDao().getAll().isEmpty()) {
                db.studentDao().insert(new Student("101", "John Doe", "john.doe@example.com", "Paid"));
                db.studentDao().insert(new Student("102", "Jane Smith", "jane.smith@example.com", "Pending"));
            }
            List<Student> studentList = db.studentDao().getAll();
            runOnUiThread(() -> {
                adapter = new StudentAdapter(studentList, this);
                rvStudents.setAdapter(adapter);
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
}
