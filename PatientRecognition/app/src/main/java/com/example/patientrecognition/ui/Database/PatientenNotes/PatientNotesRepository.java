package com.example.patientrecognition.ui.Database.PatientenNotes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PatientNotesRepository {
    private PatientNoteDao patientNoteDao;
    private LiveData<List<PatientNote>> patientNotesLiveData;

    public PatientNotesRepository(Application application) {
        PatientNoteDatabase patientNoteDatabase = PatientNoteDatabase.getDatabase(application);
        patientNoteDao = patientNoteDatabase.patientNoteDao();
        patientNotesLiveData = patientNoteDao.getAllNotes();

    }

    public void insert(PatientNote patientNote){
        new InsertTaskAsyncTask(patientNoteDao).execute(patientNote);
    }

    public void update(PatientNote patientNote){
        new UpdatePatientNoteAsyncTask(patientNoteDao).execute(patientNote);
    }

    public void delete(PatientNote patientNote){
        new DeletePatientNoteAsyncTask(patientNoteDao).execute(patientNote);
    }

    public void deleteAll(){
        new DeleteAllPatientNoteAsyncTask(patientNoteDao).execute();
    }

    public LiveData<List<PatientNote>> getAllPatientNotes() { return patientNotesLiveData; }

    private static class InsertTaskAsyncTask extends AsyncTask<PatientNote, Void, Void> {
        private PatientNoteDao patientNoteDao;

        private InsertTaskAsyncTask(PatientNoteDao patientNoteDao){
            this.patientNoteDao = patientNoteDao;
        }

        @Override
        protected Void doInBackground(PatientNote... tasks) {
            patientNoteDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdatePatientNoteAsyncTask extends AsyncTask<PatientNote, Void, Void> {
        private PatientNoteDao patientNoteDao;

        private UpdatePatientNoteAsyncTask(PatientNoteDao patientNoteDao){
            this.patientNoteDao = patientNoteDao;
        }

        @Override
        protected Void doInBackground(PatientNote... tasks) {
            patientNoteDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeletePatientNoteAsyncTask extends AsyncTask<PatientNote, Void, Void> {
        private PatientNoteDao patientNoteDao;

        private DeletePatientNoteAsyncTask(PatientNoteDao patientNoteDao){
            this.patientNoteDao = patientNoteDao;
        }

        @Override
        protected Void doInBackground(PatientNote... tasks) {
            patientNoteDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllPatientNoteAsyncTask extends AsyncTask<PatientNote, Void, Void> {
        private PatientNoteDao patientNoteDao;

        private DeleteAllPatientNoteAsyncTask(PatientNoteDao patientNoteDao){
            this.patientNoteDao = patientNoteDao;
        }

        @Override
        protected Void doInBackground(PatientNote... tasks) {
            patientNoteDao.deleteAllNotes();
            return null;
        }
    }
}
