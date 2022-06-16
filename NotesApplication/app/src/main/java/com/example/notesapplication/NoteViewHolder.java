package com.example.notesapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder{

     TextView nheading;
     TextView ndata;
     TextView ndate;


    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        nheading = itemView.findViewById(R.id.headText);
        ndate = itemView.findViewById(R.id.dateText);
        ndata = itemView.findViewById(R.id.dataText);
    }
}
