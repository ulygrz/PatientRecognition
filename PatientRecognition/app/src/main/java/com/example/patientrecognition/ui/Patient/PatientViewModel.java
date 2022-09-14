package com.example.patientrecognition.ui.Patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;
import com.example.patientrecognition.ui.Database.Users.User;
import com.example.patientrecognition.ui.Database.Users.UserRepository;

import java.util.List;

public class PatientViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<User>> allUsers;

    public PatientViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void update(User user) { repository.update(user); }

    public void delete (User user) { repository.delete(user); }

    public void insert(User user) { repository.insert(user); }

    public void deleteAllPatients() { repository.deleteAll(); }
}