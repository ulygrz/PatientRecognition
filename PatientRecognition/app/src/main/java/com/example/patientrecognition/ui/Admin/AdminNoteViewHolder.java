package com.example.patientrecognition.ui.Admin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;

import java.util.List;

public class AdminNoteViewHolder extends RecyclerView.ViewHolder {

    private TextView noteTitle;
    private TextView noteContent;

    public AdminNoteViewHolder(@NonNull View itemView, List<PatientNote> notes) {
        super(itemView);

        noteTitle = itemView.findViewById(R.id.item_note_title);
        noteContent = itemView.findViewById(R.id.item_note_content);

    }

    public void bind(PatientNote patientNote){
        String noteTitleString = patientNote.getNoteTitle();
        String noteContentString = patientNote.getNote();

        noteTitle.setText(noteTitleString);
        noteContent.setText(noteContentString);
    }
}
