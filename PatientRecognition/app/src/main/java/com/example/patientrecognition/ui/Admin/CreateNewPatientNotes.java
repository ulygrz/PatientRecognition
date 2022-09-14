package com.example.patientrecognition.ui.Admin;

import android.accessibilityservice.GestureDescription;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;
import com.example.patientrecognition.ui.Database.Users.User;
import com.example.patientrecognition.ui.Patient.PatientViewModel;
//TODO: here
import com.robotemi.sdk.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;

public class CreateNewPatientNotes extends Fragment {

    private EditText noteTitle;
    private EditText noteContent;
    private Spinner patientSelectedSpinner;
    private Spinner patientLocationSpinner;
    private Spinner optionalAppToStartSpinner;
    private Button saveNoteButton;
    private CreateNewPatientNoteViewModel newPatientNoteViewModel;
    private PatientViewModel patientViewModel;
    private int optionalAction;
    boolean patientSelected = false;
    boolean locationSelected= false;
    boolean actionSelected = false;
    private String patientID, location, patientName;
    private List<User> usersList = new ArrayList<>();
    //private ArrayList<String> patientList = new ArrayList<>();
    private ArrayList<String> patientLocationList = new ArrayList<>();
    private ArrayList<String> optionalActionList =  new ArrayList<>();//TODO: add information to the list for the spinners
    //TODO: here
    private Robot robot;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            newPatientNoteViewModel = new CreateNewPatientNoteViewModel(this.getActivity().getApplication());
        } catch (Exception e){
            Log.d("newPatientNoteViewModel", "HideKeyBoard Exception: " + e);
        }
        robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.
        Log.d("createNewPatientNote","Locations: "+robot.getLocations());
        patientLocationList.addAll(robot.getLocations());
        /*patientLocationList.add("Schlafzimmer");
        patientLocationList.add("Wohnzimmer");
        patientLocationList.add("Schreibtisch");
        patientLocationList.add("Esszimmer");
         */
        Log.d("createNewPatientNote","Locations: "+patientLocationList);

        //TODO: erase // when the robot is active
        //TODO: Get the list of the patients in the database
        getOptionalActions();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createnewpatientnote,container,false);
        ButterKnife.bind(this, view);

        noteTitle = view.findViewById(R.id.fragment_add_noteTitle);
        noteContent = view.findViewById(R.id.fragment_patient_note);
        saveNoteButton = view.findViewById(R.id.save_note_button);
        patientSelectedSpinner = view.findViewById(R.id.patient_spinner);
        patientLocationSpinner = view.findViewById(R.id.patient_locations_spinner);
        optionalAppToStartSpinner = view.findViewById(R.id.extra_actions_option1);


        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpinnerSelectedItems();
                if (patientSelected && locationSelected && actionSelected){
                    saveNote();
                    Navigation.findNavController(v).navigate(R.id.action_navigation_new_note_to_navigation_admin);
                } else {
                    new AlertDialog.Builder(getContext()).setMessage("Bitte alle Felder ausfüllen").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                    //Toast.makeText(getContext(),String.format("Bitte alle Felder ausfüllen!"), Toast.LENGTH_LONG).show();
                }
            }
        });

       // patientList.getOnItemSelectedListener(new View.OnIt)


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patientViewModel = new ViewModelProvider(this).get(PatientViewModel.class);
        patientViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersList.addAll(users);
                setPatientListInSpinner();
                if (!users.isEmpty()){
                    Log.d("CreateNewPatientNote", "Users: "+users.get(0).toString());
                    Log.d("CreateNewPatientNote", "Users: "+users.get(0).getFirstName());

                    Log.d("CreateNewPatientNote", "usersList Names: "+usersList.get(0).getFirstName());

                    Log.d("CreateNewPatientNote", "usersList Size: "+usersList.size());
                }

            }
        });

        view.findViewById(R.id.createNewPatientNote).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    hideKeyboard();
                } catch (Exception e) {
                    Log.d("LocationsFragment", "HideKeyBoard Exception: " + e);
                }
                return true;
            }
        });

        setLocationListInSpinner();
        setOptionalActionInSpinner();

    }

    private void getOptionalActions() {
        optionalActionList.add("Wähle eine Option");
        optionalActionList.add("------------");
        optionalActionList.add("Telepräsenz");
        optionalActionList.add("Medisana App");
        optionalActionList.add("Externe App");
    }

    private void setOptionalActionInSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,optionalActionList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionalAppToStartSpinner.setAdapter(spinnerAdapter);
    }

    private void setLocationListInSpinner() {

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,patientLocationList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientLocationSpinner.setAdapter(spinnerAdapter);
    }

    private void setPatientListInSpinner() {
        ArrayList<String> patientList = new ArrayList<>();
        Log.d("setPatientListInSpinner", "usersList Size: "+usersList.size());
        for(User user : usersList){
            Log.d("setPatientListInSpinner", "usersList Names: "+usersList.get(0).getFirstName());
            Log.d("setPatientListInSpinner", "usersList Names: "+user.getFirstName());
            String string = String.format("%s %s , %s", user.getFirstName(), user.getLastName(), user.getUserId());
            Log.d("setPatientListInSpinner", "usersList Names as string: "+string);

            patientList.add(string);
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,patientList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientSelectedSpinner.setAdapter(spinnerAdapter);
    }

    private void getSpinnerSelectedItems() {
        if(!patientSelectedSpinner.getSelectedItem().toString().equals("Patient")){
            patientSelected = true;
            String[] userStringSplitter = patientSelectedSpinner.getSelectedItem().toString().split(" , ");
            patientName = userStringSplitter[0];
            patientID = userStringSplitter[1];
        } else {patientID = "null";}

        if(!patientLocationSpinner.getSelectedItem().toString().equals("Wähle einen Ort")){
            locationSelected = true;
            location = patientLocationSpinner.getSelectedItem().toString();
        } else {location = "null";}

        switch (optionalAppToStartSpinner.getSelectedItem().toString()){
            case "Telepräsenz":
                actionSelected = true;
                optionalAction = PatientNote.START_TELEPRESENCE;
                break;
            case "Medisana App":
                actionSelected = true;
                optionalAction = PatientNote.START_MEDISANA_APP;
                break;
            case "Externe App":
                actionSelected = true;
                optionalAction = PatientNote.START_EXTERN_APP;
                break;
            case "------------":
                actionSelected = true;
                optionalAction = PatientNote.NO_ACTION;
                break;
            default:
                break;
        }

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void saveNote() {
        int noteId = new Random().nextInt(Integer.MAX_VALUE);
        PatientNote newPatientNote = new PatientNote(
                noteId,
                noteTitle.getText().toString(),
                noteContent.getText().toString(),
                location,
                patientID,
                patientName,
                optionalAction,
                System.currentTimeMillis()
        );

        newPatientNoteViewModel.insert(newPatientNote);
    }
}
