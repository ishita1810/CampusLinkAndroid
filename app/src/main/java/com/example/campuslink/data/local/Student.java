package com.example.campuslink.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class Student {
    @PrimaryKey
    @NonNull
    public String id; // uid
    public String name;
    public String email;
    public String role; // Admin, Faculty, Student
    public String feeStatus; // Primarily for Students

    public Student(@NonNull String id, String name, String email, String role, String feeStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.feeStatus = feeStatus;
    }
}
