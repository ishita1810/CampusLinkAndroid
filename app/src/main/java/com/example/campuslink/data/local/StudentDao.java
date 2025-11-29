package com.example.campuslink.data.local;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface StudentDao {
    @Insert
    void insert(Student s);


    @Query("SELECT * FROM students")
    List<Student> getAll();

    @Query("SELECT * FROM students WHERE id = :userId")
    Student getStudentById(String userId);

    @Query("DELETE FROM students")
    void deleteAll();

    @Update
    void update(Student student);
}