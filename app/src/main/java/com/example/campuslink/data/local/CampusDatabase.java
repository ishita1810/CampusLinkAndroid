package com.example.campuslink.data.local;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.campuslink.model.Event;


@Database(entities = {Student.class, AttendanceRecord.class, Event.class}, version = 3)
public abstract class CampusDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();
    public abstract AttendanceRecordDao attendanceRecordDao();
    public abstract EventDao eventDao();


    private static volatile CampusDatabase INSTANCE;
    public static CampusDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CampusDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CampusDatabase.class, "campus_db")
                            .fallbackToDestructiveMigration() // Not ideal for production
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}