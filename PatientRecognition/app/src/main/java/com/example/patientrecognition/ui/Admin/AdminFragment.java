package com.example.patientrecognition.ui.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientrecognition.R;
import com.example.patientrecognition.ui.Database.PatientenNotes.PatientNote;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AdminFragment extends Fragment {

    private AdminViewModel adminViewModel;
    private AdminNoteRecyclerViewAdapter adminRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton addNewPatientNote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adminRecyclerViewAdapter = new AdminNoteRecyclerViewAdapter();
        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        adminViewModel.getAllPatientNotes().observe(this, new Observer<List<PatientNote>>() {
            @Override
            public void onChanged(List<PatientNote> patientNotes) {
                try {
                    adminRecyclerViewAdapter.setPatientNote(patientNotes);
                    Log.d("onChanged", "Patienten Notes: "+patientNotes);
                    Toast.makeText(getContext(),String.format("New Note added"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("adminViewModel", "Exception: "+e);
                }
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        addNewPatientNote = view.findViewById(R.id.add_new_patientNote_floatingActionButton);

        recyclerView = view.findViewById(R.id.fragment_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adminRecyclerViewAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                adminViewModel.delete(adminRecyclerViewAdapter.getPatientNoteAt(viewHolder.getBindingAdapterPosition()));


            }
        }).attachToRecyclerView(recyclerView);

        addNewPatientNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_admin_to_navigation_new_note);
            }
        });

        return view;
    }

}