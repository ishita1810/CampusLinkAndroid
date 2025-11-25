package com.example.campuslink.data.local;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "students")
public class Student {
    @PrimaryKey
    @NonNull
    public String id; // uid
    public String name;
    public String email;
    public String feeStatus;

    public Student(@NonNull String id, String name, String email, String feeStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.feeStatus = feeStatus;
    }
}