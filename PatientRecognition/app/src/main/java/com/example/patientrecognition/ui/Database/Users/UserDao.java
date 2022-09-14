package com.example.patientrecognition.ui.Database.Users;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM users_table ORDER BY created ASC")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM users_table")
    void deleteAllUsers();

    @Delete
    void delete(User user);

    @Update
    void update(User user);

}
