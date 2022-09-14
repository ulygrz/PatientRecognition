package com.example.patientrecognition.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;

import java.util.List;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    private TextView patientFirstName;
    private TextView noteTitle;
    private TextView noteContent;

    public HomeViewHolder(@NonNull View itemView, List<PatientNote> patientNotes) {
        super(itemView);

        patientFirstName = itemView.findViewById(R.id.item_user_info);
        noteTitle = itemView.findViewById(R.id.item_note_title);
        noteContent = itemView.findViewById(R.id.item_note_content);

    }

    public void bind(PatientNote patientNote){
        noteTitle.setText(patientNote.getNoteTitle());
        patientFirstName.setText(patientNote.getUserName());
        noteContent.setText(patientNote.getNote());
    }
}
