package com.example.patientrecognition.ui.Database.PatientenNotes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PatientNote.class}, version = 2, exportSchema = false)
public abstract class PatientNoteDatabase extends RoomDatabase{

    private static volatile PatientNoteDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public abstract PatientNoteDao patientNoteDao();

     static PatientNoteDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (PatientNoteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PatientNoteDatabase.class, "patientnote_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
       @Override
       public void onCreate(@NonNull SupportSQLiteDatabase db){
           super.onCreate(db);
           new PopulateDbAsyncPatientNote(INSTANCE).execute();
       }
    };

    private static class PopulateDbAsyncPatientNote extends AsyncTask<Void, Void, Void>{
        private PatientNoteDao patientNoteDao;

        private PopulateDbAsyncPatientNote(PatientNoteDatabase db) {
            patientNoteDao = db.patientNoteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
