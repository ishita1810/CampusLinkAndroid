package com.example.campuslink.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Student s);

    @Query("SELECT * FROM users")
    List<Student> getAll();

    @Query("SELECT * FROM users WHERE id = :userId")
    Student getStudentById(String userId);

    @Query("DELETE FROM users")
    void deleteAll();

    @Update
    void update(Student student);
}
