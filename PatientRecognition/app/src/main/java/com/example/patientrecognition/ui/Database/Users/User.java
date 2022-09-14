package com.example.patientrecognition.ui.Database.Users;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users_table")
public class User {
    @PrimaryKey
    @NonNull

    private String firstName;
    private String lastName;
    private String gender;
    private String userId;
    private String description;
    private long created;

    public User(String userId, String firstName, String lastName, String description, String gender, long created){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.gender = gender;
        this.created = created;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public long getCreated() {
        return created;
    }

}
