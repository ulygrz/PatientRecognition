package com.example.patientrecognition.ui.Patient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class EditPatientViewModel extends ViewModel {
    private String userNames;
    private String userVorName;
    private String userNachName;
    private String userGend;
    private MutableLiveData<String> userID = new MutableLiveData<>();
    private MutableLiveData<String> userFirstName = new MutableLiveData<>();
    private MutableLiveData<String> userLastName = new MutableLiveData<>();
    private MutableLiveData<String> userDescription = new MutableLiveData<>();
    private MutableLiveData<String> userGender = new MutableLiveData<>();
    private MutableLiveData<Long> created = new MutableLiveData<>();


    public void setUserID(String input){
        userID.setValue(input);
    }

    public void setUserFirstName(String input) {
        userFirstName.setValue(input);
        userVorName = input;
    }

    public void setUserLastName(String input) {
        userLastName.setValue(input);
        userNachName= input;
    }

    public void setUserDescription(String input) {
        userDescription.setValue(input);
    }

    public void setUserGender(String input) {
        userGender.setValue(input);
        userGend = input;
    }

    public void setCreated(Long input) {
        created.setValue(input);
    }

    public LiveData<String> getUserId(){
        return userID;
    }

    public LiveData<String> getUserFirstName(){
        return userFirstName;
    }

    public LiveData<String> getUserLastName(){
        return userLastName;
    }

    public LiveData<String> getUserDescription(){
        return userDescription;
    }

    public LiveData<String> getUserGender(){
        return userGender;
    }

    public LiveData<Long> getCreated(){
        return created;
    }

    public String getUserData(){
        userNames = userVorName+"+"+userNachName+"+"+userGend;
        return userNames;
    }
}
