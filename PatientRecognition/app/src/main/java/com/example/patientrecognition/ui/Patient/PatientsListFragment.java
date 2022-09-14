package com.example.patientrecognition.ui.Patient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Admin.AdminViewModel;
import com.example.patientrecognition.ui.Database.Users.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PatientsListFragment extends Fragment {

    private PatientViewModel patientViewModel;
    private EditPatientViewModel editPatientViewModel;
    private PatientRecyclerViewAdapter patientRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<User> currentContacts;
    private List<UserInfo> temiUserInfoList;
    private Robot robot;
    private AdminViewModel adminViewModel;
    private FloatingActionButton updateUsersButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        robot = Robot.getInstance();
        patientRecyclerViewAdapter = new PatientRecyclerViewAdapter();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient, container, false);

        updateUsersButton = view.findViewById(R.id.update_patients_floatingActionButton);

        recyclerView = view.findViewById(R.id.fragment_patientList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(patientRecyclerViewAdapter);

        patientViewModel = new ViewModelProvider(this).get(PatientViewModel.class);
        patientViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                try {
                    currentContacts = new ArrayList<>();
                    patientRecyclerViewAdapter.setUser(users);
                    currentContacts = users;
                    Log.d("onChanged", "Users: "+users);

                    if (!currentContacts.isEmpty()){
                        for (User newUsers : currentContacts){
                            //Log.d("onChanged", "currentContacts: "+newUsers.getFirstName());
                            Log.d("onChanged", "currentContacts: "+ Objects.requireNonNull(newUsers.getFirstName()));
                        }
                    }

                    Toast.makeText(getContext(),String.format("Patients added"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("patientViewModel", "Exception: "+e);
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                patientViewModel.delete(patientRecyclerViewAdapter.getUserAt(viewHolder.getBindingAdapterPosition()));


            }
        }).attachToRecyclerView(recyclerView);

        patientRecyclerViewAdapter.setOnItemClickListener(new PatientRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {
                Log.d("onItemClick", "Item Clicked");
                //TODO: create AlertDialog to ask confirm if the userInformation want to be edited. If accepted then open the new fragment

                editPatientViewModel.setUserID(user.getUserId());
                editPatientViewModel.setUserFirstName(user.getFirstName());
                editPatientViewModel.setUserLastName(user.getLastName());
                editPatientViewModel.setUserDescription(user.getDescription());
                editPatientViewModel.setUserGender(user.getGender());
                editPatientViewModel.setCreated(user.getCreated());

                Log.d("EditPatient", "Patient Info: "+user.getFirstName()+" , "+user.getLastName()+" , "+user.getGender());
                Log.d("EditPatient",  "Patient Info II: "+editPatientViewModel.getUserData());
                //Fragment createNewPatient = new CreateNewPatient();
                //FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                Navigation.findNavController(view).navigate(R.id.action_navigation_patient_to_navigation_edit_user);
            }
        });

        updateUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!robot.getAllContact().isEmpty()){
                    try {
                        syncNewUsers();
                    } catch (Exception e){
                        Log.d("updateUsersButton", "Exception: "+e);
                    }
                } else {
                    new AlertDialog.Builder(getContext()).setTitle("Keine Kontakte gespeichert").setMessage("Prüfen Sie die gespeicherte Kontakte im Temi Center ").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                }
            }
        });

        return view;
    }


    public void syncNewUsers(){
        temiUserInfoList = new ArrayList<>();
        temiUserInfoList = robot.getAllContact();
        //temiUserInfoList = createUserInfoList();
        Log.d("updateClickListener", "All Contacts: "+temiUserInfoList);
        Log.d("Contacts", "number of Contacts:"+currentContacts.size());


        if (currentContacts.size() != 0){
            //if there are more than 0 users saved in the database
            //just the new contacts will be saved in the db
            //currentContacts are the users saved in the db
            Log.d("Contacts", "number of Contacts:"+currentContacts.size());
            for (User newUsers : currentContacts){
                Log.d("onChanged", "currentContacts: "+newUsers.getFirstName());

            }

            for (UserInfo newTemiUserInfo : temiUserInfoList){
                int patientRecognitionUserSize = currentContacts.size();
                Log.d("Index", "CurrentContacts List length: "+ patientRecognitionUserSize);
                for (User currentAppUser : currentContacts){
                    Log.d("Index", "robotUSerInfo: "+ newTemiUserInfo.getName()+" CurrentContact: "+currentAppUser.getFirstName());
                    Log.d("Index", "robotUSerInfo: "+ newTemiUserInfo.getUserId()+" CurrentContact: "+currentAppUser.getUserId());
                    if (!newTemiUserInfo.getUserId().equals(currentAppUser.getUserId())){
                        patientRecognitionUserSize--;

                        Log.d("Index", "CurrentContacts List length: "+ patientRecognitionUserSize);

                    }
                }
                if (patientRecognitionUserSize == 0){
                    Log.d("Temi new Contact", "ID: "+newTemiUserInfo.getUserId()+" ,Name: "+newTemiUserInfo.getName());
                    createNewUser(newTemiUserInfo.getUserId(), newTemiUserInfo.getName());
                    //TODO: create AlertDialog to advise that the userInfo isn't completed and has to be edited with one click on it
                }
            }
        } else if (currentContacts.isEmpty()) {
            //If there is no users saved in the Database, all contacts will be synchronized
            for (UserInfo newUserInfo : temiUserInfoList){
                createNewUser(newUserInfo.getUserId(), newUserInfo.getName());
            }
            Log.d("TemiContacts", "ID: "+temiUserInfoList.get(0).getUserId()+" ,Name: "+temiUserInfoList.get(0).getName());
            //createNewUser(temiUserInfoList.get(1).getUserId(),temiUserInfoList.get(1).getName());
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editPatientViewModel = new ViewModelProvider(getActivity()).get(EditPatientViewModel.class);
    }

    private void createNewUser(String userId, String name) {
        User user = new User(
                userId,
                name,
                "",
                "",
                "",
                System.currentTimeMillis()
        );
        Log.d("newUSer", "new UserInfo: "+user);
        patientViewModel.insert(user);
    }
}