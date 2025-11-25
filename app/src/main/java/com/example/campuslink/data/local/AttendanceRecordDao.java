package com.example.campuslink.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AttendanceRecordDao {
    @Insert
    void insert(AttendanceRecord attendanceRecord);

    @Query("SELECT * FROM attendance_records WHERE date = :date")
    List<AttendanceRecord> getAttendanceByDate(String date);
}
