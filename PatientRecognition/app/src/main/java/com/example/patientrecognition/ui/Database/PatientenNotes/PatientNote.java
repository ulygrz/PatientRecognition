package com.example.patientrecognition.ui.Database.PatientenNotes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import androidx.annotation.NonNull;

@Entity (tableName = "patientnote_table")
public class PatientNote {
    @PrimaryKey
    @NonNull
    private  int noteId;
    private String noteTitle;
    private String note;
    private String location;    //Location or room of the patient
    private String userId;
    private String userName;
    private int action;
    private long created;
    public static final int NO_ACTION = 0;
    public static final int START_TELEPRESENCE = 1;
    public static final int START_EXTERN_APP = 2;
    public static final int START_MEDISANA_APP = 3;

    public PatientNote(int noteId, String noteTitle, String note, String location, String userId, String userName, int action, long created){
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.note = note;
        this.location = location;
        this.userId = userId;
        this.userName = userName;
        this.action = action;
        this.created = created;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNote() {
        return note;
    }

    public String getLocation() {
        return location;
    }

    public String getUserId() {
        return userId;
    }

    public int getAction() {
        return action;
    }

    public long getCreated() {
        return created;
    }

    public String getUserName() { return userName; }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAction(int action) {
        this.action = action;
    }

}
