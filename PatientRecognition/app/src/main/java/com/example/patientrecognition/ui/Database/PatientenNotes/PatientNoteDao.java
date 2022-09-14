package com.example.patientrecognition.ui.Database.PatientenNotes;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PatientNoteDao {

    @Insert
    void insert(PatientNote patientNote);

    @Query("SELECT * FROM patientnote_table ORDER BY created ASC")
    LiveData<List<PatientNote>> getAllNotes();

    @Query("DELETE FROM patientNote_table")
    void deleteAllNotes();

    @Delete
    void delete(PatientNote patientNote);

    @Update
    void update(PatientNote patientNote);

}
