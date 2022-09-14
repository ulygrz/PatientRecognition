package com.example.patientrecognition.ui.Admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;
import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNotesRepository;

public class CreateNewPatientNoteViewModel extends AndroidViewModel {

    private PatientNotesRepository repository;

    public CreateNewPatientNoteViewModel(@NonNull Application application) {
        super(application);
        repository = new PatientNotesRepository(application);
    }

    public void insert(PatientNote patientNote){
        repository.insert(patientNote);
    }
}
