package com.example.campuslink.data.local;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendance_records",
        foreignKeys = @ForeignKey(entity = Student.class,
                                  parentColumns = "id",
                                  childColumns = "studentId",
                                  onDelete = ForeignKey.CASCADE))
public class AttendanceRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String studentId;
    public String date;
    public boolean isPresent;
}
