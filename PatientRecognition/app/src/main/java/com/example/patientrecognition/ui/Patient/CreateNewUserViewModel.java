package com.example.patientrecognition.ui.Patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.patientrecognition.ui.Database.Users.User;
import com.example.patientrecognition.ui.Database.Users.UserRepository;

public class CreateNewUserViewModel extends AndroidViewModel {

    private UserRepository repository;

    public CreateNewUserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void insert(User user) { repository.insert(user); }
}
