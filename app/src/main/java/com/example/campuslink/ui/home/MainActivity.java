package com.example.campuslink.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campuslink.R;
import com.example.campuslink.ui.attendance.AttendanceActivity;
import com.example.campuslink.ui.fees.FeesActivity;
import com.example.campuslink.ui.events.EventsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAttendance = findViewById(R.id.btnAttendance);
        Button btnFees = findViewById(R.id.btnFees);
        Button btnEvents = findViewById(R.id.btnEvents);

        btnAttendance.setOnClickListener(this);
        btnFees.setOnClickListener(this);
        btnEvents.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnAttendance) {
            Intent intent = new Intent(this, AttendanceActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnFees) {
            Intent intent = new Intent(this, FeesActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnEvents) {
            Intent intent = new Intent(this, EventsActivity.class);
            startActivity(intent);
        }
    }
}
