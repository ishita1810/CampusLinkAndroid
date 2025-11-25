package com.example.campuslink.ui.events;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campuslink.R;
import com.example.campuslink.data.local.CampusDatabase;
import com.example.campuslink.model.Event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateEventActivity extends AppCompatActivity {

    private EditText etEventName, etEventDescription, etEventDate;
    private CampusDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
        etEventDate = findViewById(R.id.etEventDate);
        Button btnCreateEvent = findViewById(R.id.btnCreateEvent);

        db = CampusDatabase.getDatabase(this);

        btnCreateEvent.setOnClickListener(v -> createEvent());
    }

    private void createEvent() {
        String name = etEventName.getText().toString().trim();
        String description = etEventDescription.getText().toString().trim();
        String date = etEventDate.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            db.eventDao().insert(new Event(name, description, date));
            runOnUiThread(() -> {
                Toast.makeText(this, "Event Created", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
