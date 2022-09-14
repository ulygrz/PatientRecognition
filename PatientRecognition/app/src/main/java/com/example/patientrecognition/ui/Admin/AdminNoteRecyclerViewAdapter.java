package com.example.patientrecognition.ui.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;

import java.util.ArrayList;
import java.util.List;

public class AdminNoteRecyclerViewAdapter extends RecyclerView.Adapter<AdminNoteViewHolder> {
    private List<PatientNote> patientNotes;

    public AdminNoteRecyclerViewAdapter(){
        this.patientNotes = new ArrayList<>();
    }

    @NonNull
    @Override
    public AdminNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);

        return new AdminNoteViewHolder(itemView, patientNotes);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminNoteViewHolder holder, int position) {
        PatientNote currentPatientNote = patientNotes.get(position);
        holder.bind(currentPatientNote);
    }

    @Override
    public int getItemCount() {
        return patientNotes.size();
    }

    public void setPatientNote(List<PatientNote> patientNotes){
        this.patientNotes = patientNotes;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull AdminNoteViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public PatientNote getPatientNoteAt(int position){
        return patientNotes.get(position);
    }
}
