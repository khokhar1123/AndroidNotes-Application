package com.example.notesapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "mainActivity";
    private int i = -1;
    private RecyclerView recyclerView;
    private final ArrayList<Note> nList = new ArrayList<>();
    private Note NOTE;
    private NoteAdapter nAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<Intent> activityResultLauncher2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::resultHandler);
        activityResultLauncher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::onclickresultHandler);
        recyclerView = findViewById(R.id.recycler);
        nAdapter = new NoteAdapter(nList, this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        nList.clear();
        nList.addAll(fileLoader());
        nAdapter.notifyDataSetChanged();

        setTitle("Android Notes(" + nList.size() + ")");

    }

    private ArrayList<Note> fileLoader() {
        ArrayList<Note> flist = new ArrayList<>();
        String file = getString(R.string.file_name);
        try{
            InputStream in = getApplication().getApplicationContext().openFileInput(file);
            BufferedReader readfrom = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            StringBuilder bdr = new StringBuilder();
            String line;
            while ((line = readfrom.readLine()) != null)
                bdr.append(line);
            JSONArray j_array = new JSONArray(bdr.toString());
            for (int i = 0; i < j_array.length(); i++) {
                JSONObject jsonObj = j_array.getJSONObject(i);
                String heading = jsonObj.getString("heading");
                String data = jsonObj.getString("data");
                String datedatda = jsonObj.getString("datedata");
                Note note = new Note(heading, data, datedatda);
                nList.add(note);
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File not present", Toast.LENGTH_SHORT);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flist;
    }

    @Override
    public void onClick(View view) {
        i = recyclerView.getChildLayoutPosition(view);
        Note NOTE = nList.get(i);
        Intent noteedit = new Intent(this, NoteEditor.class);
        noteedit.putExtra("Note",NOTE);
        activityResultLauncher2.launch(noteedit);
    }

    @Override
    public boolean onLongClick(View view) {
        i = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete note '" + nList.get(i).getHeading()+ "' ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                nList.remove(i);
                nAdapter.notifyDataSetChanged();
                setTitle("Android Notes (" + nList.size() + ")");
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }
    @Override
    protected void onPause() {
        noteSave();
        super.onPause();
    }
    @Override
    protected void onResume() {

        super.onResume();
    }
    private void noteSave() {
        try {
            FileOutputStream noteOut = getApplicationContext()
                    .openFileOutput("Data.json", Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(noteOut);
            printWriter.print(nList);
            printWriter.close();
            noteOut.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
    public void onclickresultHandler(ActivityResult result) {
        if (result == null) {
            return;
        }
//        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK) {
            NOTE = (Note) result.getData().getSerializableExtra("Note");
            if (NOTE != null && i > -1) {
                nList.remove(i);
                nList.add(0, NOTE);
                nAdapter.notifyDataSetChanged();
            }
        }
        setTitle("Android Notes (" + nList.size() + ")");
    }
    public void resultHandler(ActivityResult result) {
        if (result != null) {
            NOTE = (Note) result.getData().getSerializableExtra("Note");
        }
        if (result.getResultCode() == RESULT_OK) {
                if (NOTE != null) {
                    nList.add(0, NOTE);
                    nAdapter.notifyDataSetChanged();
                }
        }
//        if (result.getResultCode() == 2) {
//            if (NOTE != null && i > -1) {
//                nList.remove(i);
//                nList.add(0, NOTE);
//                nAdapter.notifyDataSetChanged();
//            }
//        }

        setTitle("Android Notes (" + nList.size() + ")");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.createNote) {
            Intent createNote = new Intent(this, NoteEditor.class);
            activityResultLauncher.launch(createNote);
        }
        if (item.getItemId() == R.id.appInfo) {
            Intent info = new Intent(this, AboutActivity.class);
            startActivity(info);
        }
        return super.onOptionsItemSelected(item);
    }
}