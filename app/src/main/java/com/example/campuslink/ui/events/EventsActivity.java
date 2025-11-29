package com.example.campuslink.ui.events;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuslink.R;
import com.example.campuslink.data.local.CampusDatabase;
import com.example.campuslink.model.Event;
import com.example.campuslink.ui.adapter.EventAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventsActivity extends AppCompatActivity {

    private EventAdapter adapter;
    private CampusDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        RecyclerView rvEvents = findViewById(R.id.rvEvents);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabCreateEvent = findViewById(R.id.fabCreateEvent);
        fabCreateEvent.setOnClickListener(v -> {
            Intent intent = new Intent(EventsActivity.this, CreateEventActivity.class);
            startActivity(intent);
        });

        db = CampusDatabase.getDatabase(this);
        
        executorService.execute(() -> {
            List<Event> eventList = db.eventDao().getAllEvents();
            runOnUiThread(() -> {
                adapter = new EventAdapter(eventList);
                rvEvents.setAdapter(adapter);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEventList();
    }

    private void updateEventList() {
        executorService.execute(() -> {
            List<Event> eventList = db.eventDao().getAllEvents();
            runOnUiThread(() -> {
                if(adapter != null) {
                    // a little messy, but it will work for now
                    // Ideally this would be done with a ViewModel and LiveData
                    RecyclerView rvEvents = findViewById(R.id.rvEvents);
                    adapter = new EventAdapter(eventList);
                    rvEvents.setAdapter(adapter);
                }
            });
        });
    }
}
