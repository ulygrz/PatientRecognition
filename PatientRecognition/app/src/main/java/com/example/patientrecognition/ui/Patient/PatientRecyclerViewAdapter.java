package com.example.patientrecognition.ui.Patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Database.Users.User;

import java.util.ArrayList;
import java.util.List;

public class PatientRecyclerViewAdapter extends RecyclerView.Adapter<PatientViewHolder> {

    private List<User> users;
    private OnItemClickListener listener;

    public PatientRecyclerViewAdapter(){ this.users = new ArrayList<>(); }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient,parent,false);
        return new PatientViewHolder(itemView, users, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        User currentUser = users.get(position);
        holder.bind(currentUser);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUser(List<User> users){
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull PatientViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public User getUserAt(int position){
        return users.get(position);
    }

    public interface  OnItemClickListener{
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;

    }
}
