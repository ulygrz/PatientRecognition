package com.example.patientrecognition.ui.Patient;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Database.Users.User;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CreateNewPatient extends Fragment {

    private EditPatientViewModel userViewModel;
    private PatientViewModel patientViewModel;

    private EditText userFirstName;
    private EditText userLastName;
    private TextView userID;
    private EditText userDescription;
    private Spinner userGenderSpinner;
    private Button saveUserButton;
    private List<String> genderList;
    private ArrayAdapter<String> genderSpinnerAdapter;
    private Long created;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genderList = new ArrayList<>();
        genderList.add("Mann");
        genderList.add("Frau");
        genderList.add("Divers");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createnewpatient,container,false);

        userFirstName = view.findViewById(R.id.fragment_firstname);
        userLastName = view.findViewById(R.id.fragment_lastname);
        userGenderSpinner = view.findViewById(R.id.patient_gender_spinner);
        userID = view.findViewById(R.id.fragment_patientId);
        userDescription = view.findViewById(R.id.fragment_patient_description);
        saveUserButton = view.findViewById(R.id.save_patient_button);

        genderSpinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,genderList);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userGenderSpinner.setAdapter(genderSpinnerAdapter);

        view.findViewById(R.id.edit_userInfo).setOnTouchListener(new View.OnTouchListener() {
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


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            userViewModel = new ViewModelProvider(getActivity()).get(EditPatientViewModel.class);
            patientViewModel = new ViewModelProvider(getActivity()).get(PatientViewModel.class);
        } catch (Exception e){
            Log.d("onViewCreated", "Exception: "+e);
            return;
        }
        //userFirstName.setText(userViewModel.getUserFirstName().getValue());TODO: see if this works, just for fun!

        userViewModel.getUserFirstName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                CharSequence firstName = s;
                userFirstName.setText(firstName, TextView.BufferType.EDITABLE);
            }
        });

        userViewModel.getUserLastName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                CharSequence lastName = s;
                userLastName.setText(lastName, TextView.BufferType.EDITABLE);
            }
        });

        userViewModel.getUserDescription().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                CharSequence description = s;
                userDescription.setText(description, TextView.BufferType.EDITABLE);
            }
        });

        userViewModel.getUserId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                CharSequence iD = s;
                userID.setText(iD, TextView.BufferType.EDITABLE);
            }
        });

        userViewModel.getUserGender().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                CharSequence gender = s;
                for(int i = 0; i< genderList.size(); i++){
                    if (genderList.get(i).equals(gender)){
                        userGenderSpinner.setSelection(i);
                    }
                }

            }
        });

        userViewModel.getCreated().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                created = aLong;
            }
        });

        saveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser(userID.getText().toString(),
                        userFirstName.getText().toString(),
                        userLastName.getText().toString(),
                        userDescription.getText().toString(),
                        userGenderSpinner.getSelectedItem().toString(),
                        created, v);

            }
        });

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void saveUser(String userId, String firstName, String lastName, String description, String gender, Long created, View view) {
        User user = new User(
                userId,
                firstName,
                lastName,
                description,
                gender,
                created
        );
        patientViewModel.update(user);
        Navigation.findNavController(view).navigate(R.id.action_navigation_edit_user_to_navigation_patient);
    }


}
