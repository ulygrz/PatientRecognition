/*
  The app has to be selected as KioskApp in order to start the face recognition functions
 */

package com.example.patientrecognition.ui.home;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Admin.AdminViewModel;
import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.constants.Mode;
import com.robotemi.sdk.face.ContactModel;
import com.robotemi.sdk.face.OnContinuousFaceRecognizedListener;
import com.robotemi.sdk.face.OnFaceRecognizedListener;
import com.robotemi.sdk.permission.OnRequestPermissionResultListener;
import com.robotemi.sdk.permission.Permission;



import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.hardware.Camera.getNumberOfCameras;

public class HomeFragment extends Fragment implements OnContinuousFaceRecognizedListener, /* OnFaceRecognizedListener ,*/ OnRequestPermissionResultListener {

    private AdminViewModel adminViewModel;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    private Robot robot;
    private static final int REQUEST_CODE_NORMAL = 0;

    private static final int FACE_RECOGNITION_START = 1;
    private static final int FACE_RECOGNITION_STOP = 2;
    private List<PatientNote> patientNotesList;
    private List<PatientNote> recognizedPatientNotesList = new ArrayList<>();
    private String detectedPatient = "";
    private String newDetectedPatient = "";

    private RecyclerView recyclerView;
    private TextView currentPatientFirstName;
    private TextView currentPatientLastName;
    private Button logoutButton;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        robot = Robot.getInstance();
        robot.addOnRequestPermissionResultListener(this);
        //robot.requestToBeKioskApp();
        if (!robot.isSelectedKioskApp()){
            robot.setKioskModeOn(true);
            robot.requestToBeKioskApp();
            Log.d("onCreate", "KioskMode On: "+robot.isKioskModeOn());
            Log.d("onCreate", "Selected as KioskApp: "+robot.isSelectedKioskApp());
        }


        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart", "Adding Listeners");
        //robot.addOnFaceRecognizedListener(this);
        robot.addOnContinuousFaceRecognizedListener(this);
        /*if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            return;
        }

         */

        if (robot.checkSelfPermission(Permission.FACE_RECOGNITION) == Permission.GRANTED && robot.isSelectedKioskApp()){
            startFaceRecognition();
        } else {
            List<Permission> permissions = new ArrayList<>();
            permissions.add(Permission.FACE_RECOGNITION);
            Log.d("onStart", "FaceRecognition Requested Permissions: "+permissions);
            robot.requestPermissions(permissions, REQUEST_CODE_NORMAL);
        }


    }

    private boolean requestPermissionIfNeeded(Permission permission, int requestCode) {
        if (robot.checkSelfPermission(permission) == Permission.GRANTED) {
            return false;
        }
        //robot.requestPermissions(listOf(permission), requestCode);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "Fragment resume");
        Log.d("onResume", "Permission Face Recognition: "+robot.checkSelfPermission(Permission.FACE_RECOGNITION));
        Log.d("onResume", "App as KioskApp: "+robot.isSelectedKioskApp());

        if (robot.checkSelfPermission(Permission.FACE_RECOGNITION) == Permission.GRANTED && robot.isSelectedKioskApp()){
            startFaceRecognition();
        } else {
            List<Permission> permissions = new ArrayList<>();
            permissions.add(Permission.FACE_RECOGNITION);
            Log.d("FaceRecognition", "Requested Permissions: "+permissions);
            robot.requestPermissions(permissions, REQUEST_CODE_NORMAL);
        }



        startFaceRecognition();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("onStop", "removing Listeners");

        robot.stopFaceRecognition();
        //robot.removeOnFaceRecognizedListener(this);
        robot.removeOnContinuousFaceRecognizedListener(this);

        if (robot.isSelectedKioskApp()){
            //robot.setKioskModeOn(false);
            Log.d("onDestroy", "KioskMode stoped");

        }


    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("onCreateView", "Face Recognition permission: "+robot.checkSelfPermission(Permission.FACE_RECOGNITION));
        Log.d("onCreateView", "creating View");



        recyclerView = root.findViewById(R.id.fragment_patientList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        logoutButton = root.findViewById(R.id.logoutButton);
        currentPatientFirstName = root.findViewById(R.id.user_firstName);
        currentPatientLastName = root.findViewById(R.id.user_lastName);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("onViewCreated", "View created");



        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        adminViewModel.getAllPatientNotes().observe(getViewLifecycleOwner(), new Observer<List<PatientNote>>() {
            @Override
            public void onChanged(List<PatientNote> patientNotes) {
                patientNotesList = new ArrayList<>();
                try {
                    patientNotesList = patientNotes;
                    Log.d("onViewCreated","Notes for all patients: "+patientNotesList);
                } catch (Exception e){
                    Log.d("onViewCreated", "Exception: "+e);

                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        Handler handler = new Handler();
                        if (!recognizedPatientNotesList.isEmpty()) {
                            recognizedPatientNotesList.clear();
                            homeRecyclerViewAdapter.setPatientNote(recognizedPatientNotesList);
                            recyclerView.setAdapter(homeRecyclerViewAdapter);
                            robot.stopFaceRecognition();

                            handler.postDelayed(new Runnable(){
                                public void run(){
                                    startFaceRecognition();
                                }
                            },5000);
                        }
                    } catch (Exception e){
                        Log.d("logoutButton", "Exception: "+e);
                    }
            }
        });

        startFaceRecognition();
    }

    private void startFaceRecognition() {
        Log.d("Start Face Recognition", "Face recognition started");
        robot.startFaceRecognition();
    }

    /*@Override
    public void onFaceRecognized(@NotNull List<ContactModel> contactModelList) {
        Log.d("onFaceRecognized", "Person detected");
        Log.d("onFaceRecognized", "Contact sizesList:" +contactModelList.size());

        for (ContactModel contactModel : contactModelList) {
            if (!contactModel.getUserId().isEmpty()){
                newDetectedPatient = contactModel.getUserId();
            }
            Log.d("onFaceRecognized", "Contact: "+contactModel.toString());
            //Log.d("onFaceRecognized", "Contact: "+contactModel.toString()+", Contact Name: "+contactModel.getFirstName()+", Contact ID: "+contactModel.getUserId());
            if (!newDetectedPatient.equals(detectedPatient)) {
                detectedPatient = newDetectedPatient;
                for (ContactModel newContactModel : contactModelList) {
                    Log.d("onFaceRecognized", "Contact:" +newContactModel.toString());
                    Log.d("onFaceRecognized", "Contact Name:"+newContactModel.getFirstName());
                    Log.d("onFaceRecognized", "Contact ID:"+newContactModel.getUserId());

                    robot.speak(TtsRequest.create("Hallo " + newContactModel.getFirstName(), true));
                    currentPatientFirstName.setText(newContactModel.getFirstName());
                    currentPatientLastName.setText(newContactModel.getLastName());
                    findNotesForRecognizedPatient(newContactModel.getUserId());
                }//TODO: Normally the faceRecognition will give back one user back, so the length of the list should be 1. In that case uncomment the next line
                //findNotesForRecognizedPatient(contactModelList.get(0).getUserId());
            }
        }
    }

     */

    private void findNotesForRecognizedPatient(String userID) {
        if (!patientNotesList.isEmpty()){

            for (PatientNote newPatientNote : patientNotesList){
                if (newPatientNote.getUserId().equals(userID)){
                    recognizedPatientNotesList.add(newPatientNote);
                }
            }
            try{
                Log.d("findNotesForRecognized","Notes for current patient: "+ recognizedPatientNotesList);
                homeRecyclerViewAdapter.setPatientNote(recognizedPatientNotesList);
                recyclerView.setAdapter(homeRecyclerViewAdapter);
            } catch (Exception e) {
                Log.d("findNotesForRecognized", "Exception: "+e);
            }
        }

    }

    @Override
    public void onRequestPermissionResult(@NotNull Permission permission, int grantResult, int requestCode) {
        Log.d("RequestPermission", "Requesting permission");

        Log.d("RequestPermission", "permission requestCode:"+requestCode);
        Log.d("RequestPermission", "permission grantResult:"+grantResult);
        String log = String.format("Permission: %s, grantResult: %d", permission.getValue(), grantResult);
        Toast.makeText(getContext(), log, Toast.LENGTH_LONG).show();


        if (grantResult == Permission.DENIED) {
            Log.d("RequestPermission", "permission Denied");
            return;
        }
        switch (permission) {
            case FACE_RECOGNITION:
                if (requestCode == FACE_RECOGNITION_START) {
                    startFaceRecognition();
                    // robot.startFaceRecognition();
                } else if (requestCode == FACE_RECOGNITION_STOP) {
                    robot.stopFaceRecognition();
                }
                break;

        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        robot.removeOnRequestPermissionResultListener(this);
    }

    @Override
    public void onContinuousFaceRecognized(@NotNull List<ContactModel> contactModelList) {
        Log.d("onFaceRecognized", "Person detected");
        Log.d("onFaceRecognized", "Contact sizesList:" +contactModelList.size());
        Log.d("onFaceRecognized", "RawData Contact: "+contactModelList.toString());
        for (ContactModel contactModel : contactModelList) {
            if (!contactModel.getUserId().isEmpty()){
                newDetectedPatient = contactModel.getUserId();

                Log.d("onFaceRecognized", "Contact: "+contactModel.toString());

                if (!newDetectedPatient.equals(detectedPatient)) { //to prevent sending an empty ContactModel: ""
                    detectedPatient = newDetectedPatient;
                    for (ContactModel newContactModel : contactModelList) {
                        Log.d("onFaceRecognized", "Contact:" +newContactModel.toString());
                        Log.d("onFaceRecognized", "Contact Name:"+newContactModel.getFirstName());
                        Log.d("onFaceRecognized", "Contact ID:"+newContactModel.getUserId());

                        robot.speak(TtsRequest.create("Hallo " + newContactModel.getFirstName(), true));
                        currentPatientFirstName.setText(newContactModel.getFirstName());
                        currentPatientLastName.setText(newContactModel.getLastName());
                        findNotesForRecognizedPatient(newContactModel.getUserId());
                    }
                }
            }
        }

    }
}