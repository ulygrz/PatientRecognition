package com.example.patientrecognition.ui.Admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;
import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNotesRepository;

import java.util.List;

public class AdminViewModel extends AndroidViewModel {

    private PatientNotesRepository repository;
    private LiveData<List<PatientNote>> allPatientNotes;

    public AdminViewModel(@NonNull Application application) {
        super(application);
        repository = new PatientNotesRepository(application);
        allPatientNotes = repository.getAllPatientNotes();
    }

    public LiveData<List<PatientNote>> getAllPatientNotes() {
        return allPatientNotes;
    }

    public void update(PatientNote patientNote) { repository.update(patientNote); }

    public void delete (PatientNote patientNote) { repository.delete(patientNote); }

    public void insert(PatientNote patientNote) { repository.insert(patientNote); }

    public void deleteAllPatientNotes() { repository.deleteAll(); }
}