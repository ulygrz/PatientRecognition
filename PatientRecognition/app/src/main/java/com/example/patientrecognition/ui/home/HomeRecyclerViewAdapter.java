package com.example.patientrecognition.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;

import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeViewHolder>{

    private List<PatientNote> patientNotes;

    public HomeRecyclerViewAdapter(){
        this.patientNotes = new ArrayList<>();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_note_view, parent,false);
        return new HomeViewHolder(itemView,patientNotes);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
            PatientNote currentPatientNote = patientNotes.get(position);
            holder.bind(currentPatientNote);
    }

    @Override
    public int getItemCount() {
        return patientNotes.size();
    }

    @Override
    public void onViewRecycled(@NonNull HomeViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public PatientNote getPatientNoteAt(int position){
        return patientNotes.get(position);
    }

    public void setPatientNote(List<PatientNote> patientNotes){
        this.patientNotes = patientNotes;
        notifyDataSetChanged();
    }
}
