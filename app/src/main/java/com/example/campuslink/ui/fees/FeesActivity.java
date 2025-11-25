package com.example.campuslink.ui.fees;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuslink.R;
import com.example.campuslink.data.local.CampusDatabase;
import com.example.campuslink.data.local.Student;
import com.example.campuslink.ui.adapter.FeeAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeesActivity extends AppCompatActivity implements FeeAdapter.OnFeeStatusChangedListener {

    private FeeAdapter adapter;
    private CampusDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        RecyclerView rvFees = findViewById(R.id.rvFees);
        rvFees.setLayoutManager(new LinearLayoutManager(this));

        db = CampusDatabase.getDatabase(this);

        executorService.execute(() -> {
            studentList = db.studentDao().getAll();
            // Initialize fee status for dummy data
            for(Student s : studentList) {
                if(s.feeStatus == null) {
                    s.feeStatus = "Pending";
                    db.studentDao().update(s);
                }
            }
            studentList = db.studentDao().getAll();
            runOnUiThread(() -> {
                adapter = new FeeAdapter(studentList, this);
                rvFees.setAdapter(adapter);
            });
        });
    }

    @Override
    public void onFeeStatusChanged(Student student) {
        executorService.execute(() -> {
            if ("Paid".equals(student.feeStatus)) {
                student.feeStatus = "Pending";
            } else {
                student.feeStatus = "Paid";
            }
            db.studentDao().update(student);
            List<Student> updatedList = db.studentDao().getAll();
            runOnUiThread(() -> {
                adapter.setStudents(updatedList);
            });
        });
    }
}
