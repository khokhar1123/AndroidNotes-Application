package com.example.notesapplication;

import android.icu.util.ULocale;
import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Note implements Serializable {
    private  String heading;
    private  String noteData;
    private  String date;
    private static int count;
    private static final SimpleDateFormat criteria = new SimpleDateFormat("EEE MMM d,  HH:mmaa", Locale.US);
    public Note() {
        this.heading = heading;
        this.noteData = noteData;
        this.date = criteria.format(new Date());

        count++;
    }
    public Note(String heading, String noteData) {
        this.heading = heading;
        this.noteData = noteData;
        date= criteria.format(new Date());
        count++;
    }
    public Note(String heading, String noteData, String date) {
        this.heading = heading;
        this.noteData = noteData;
        this.date = date;
        count++;
    }
    public String getHeading() {
        return heading;

    }

    public String getNoteData() {
        return noteData;
    }

    public String getDate() {
        return date;
    }

    public void setHeading(String heading) {

        this.heading = heading;
    }

    public void setNoteData(String noteData) {

        this.noteData = noteData;
    }
    public void setDate(Date d){
        date = criteria.format(new Date());
    }
    @NonNull
    @Override
    public String toString() {
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("heading").value(getHeading());
            jsonWriter.name("data").value(getNoteData());
            jsonWriter.name("datedata").value(getDate());
            jsonWriter.endObject();
            jsonWriter.close();
            return stringWriter.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
