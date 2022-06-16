package com.example.notesapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>{
    private ArrayList<Note> nList = new ArrayList<>();
    private final MainActivity mainActivity;

    public NoteAdapter(ArrayList<Note> nList, MainActivity mainActivity) {
        this.nList = nList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notelist_entry, parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note n = nList.get(position);
        holder.nheading.setText(n.getHeading());
        if (n.getNoteData().length() > 80 ){
            String todisplay = n.getNoteData().substring(0,80) + "...";
            holder.ndata.setText(todisplay);
        } else holder.ndata.setText(n.getNoteData());

        holder.ndate.setText(n.getDate());
    }


    @Override
    public int getItemCount() {
        return nList.size();
    }
}
