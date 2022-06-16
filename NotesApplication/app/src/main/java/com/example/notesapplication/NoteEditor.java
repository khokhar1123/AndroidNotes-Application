package com.example.notesapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class NoteEditor extends AppCompatActivity {

    private EditText heading;
    private EditText data;
    private Note NOTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noteeditor);
        heading = findViewById(R.id.editHeading);
        data = findViewById(R.id.editData);
        Intent noteeditor = getIntent();
        if (noteeditor.hasExtra("Note")) {
            NOTE = (Note) noteeditor.getSerializableExtra("Note");
            if (NOTE != null) {
                heading.setText(NOTE.getHeading());
                data.setText(NOTE.getNoteData());
            }
        }

    }
    public void noHeadDialog() {
        AlertDialog.Builder bdr = new AlertDialog.Builder(this);
        bdr.setTitle("A note without a title is not allowed to be saved");
        bdr.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent();
                intent.putExtra("Note", NOTE);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        bdr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });


        AlertDialog dialog = bdr.create();
        dialog.show();

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.noteeditomenu, menu);
        return true;
    }
    public void doReturn(){
        String head = heading.getText().toString();
        String rdata = data.getText().toString();

        if (NOTE != null) {
            if (!NOTE.getHeading().equals(head) || !NOTE.getNoteData().equals(rdata)) {
                NOTE.setHeading(head);
                NOTE.setDate(new Date());
                NOTE.setNoteData(rdata);
//                Intent intent = new Intent();
//                intent.putExtra("Note", NOTE);
//                setResult(2, intent);
//                finish();
            } else
            finish();
        } else {
            NOTE = new Note(head, rdata);
            NOTE.setDate(new Date());
//            Intent intent = new Intent();
//            intent.putExtra("Note", NOTE);
//            setResult(RESULT_OK, intent);
//            finish();
        }
        Intent intent = new Intent();
        intent.putExtra("Note", NOTE);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onBackPressed() {
        if ((!(heading.getText().toString().isEmpty())) && NOTE.getHeading().equals(heading.getText().toString()) && NOTE.getNoteData().equals(data.getText().toString())) {
//            Intent intent = new Intent();
//            intent.putExtra("Note", NOTE);
//            setResult(RESULT_OK, intent);
            finish();
        } else {
            AlertDialog.Builder bdr = new AlertDialog.Builder(this);

            if (heading.getText().toString().isEmpty()) {
                Toast.makeText(this, "Notes without title are not saved", Toast.LENGTH_SHORT).show();
                bdr.setTitle("A note without a title is not allowed to be saved");
                bdr.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra("Note", NOTE);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                });
                bdr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
            } else {
                bdr.setTitle("Do you want to save your note ?");

                bdr.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        doReturn();
                    }
                });
                bdr.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

                AlertDialog dialog = bdr.create();
                dialog.show();
            }
            AlertDialog dialog = bdr.create();
            dialog.show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.savenote) {
            if (!heading.getText().toString().isEmpty()) {
                doReturn();
            } else{
                noHeadDialog();
                Toast.makeText(this, "Notes without title are not saved", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

}
