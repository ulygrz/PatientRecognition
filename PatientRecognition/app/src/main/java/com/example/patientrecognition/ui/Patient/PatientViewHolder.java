package com.example.patientrecognition.ui.Patient;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Database.Users.User;

import java.util.List;

public class PatientViewHolder extends RecyclerView.ViewHolder {

    private TextView patientName;
    private TextView patientID;
    private PatientRecyclerViewAdapter.OnItemClickListener listener;

    public PatientViewHolder(@NonNull View itemView, List<User> users, PatientRecyclerViewAdapter.OnItemClickListener listener) {
        super(itemView);

        patientName = itemView.findViewById(R.id.item_patient_name);
        patientID = itemView.findViewById(R.id.item_patient_id);
        this.listener = listener;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("itemOnClicked", "item clicked");
                int position = getAbsoluteAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    listener.onItemClick(users.get(position));
                }
            }
        });
    }

    public void bind(User user){
        String patientCompleteName = String.format("%s %s",user.getFirstName(),user.getLastName());
        patientID.setText(user.getUserId());
        patientName.setText(patientCompleteName);
    }
}
