package com.example.reminder.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.reminder.R;
import com.example.reminder.model.Game;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

private EditText nieuweTitel;
private EditText nieuwePlatform;
private Spinner spinner;
private String status;
private int statusIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Spinner
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Init local variables
        nieuweTitel = findViewById(R.id.titelText);
        nieuwePlatform = findViewById(R.id.platformText);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titel = nieuweTitel.getText().toString();
                String platform = nieuwePlatform.getText().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String datum = formatter.format(date);

                if ((!TextUtils.isEmpty(titel)) && (!TextUtils.isEmpty(platform)) && (!TextUtils.isEmpty(status))) {

                    Game nieuweGame = new Game(titel, platform, datum, status, statusIndex);
                    //Prepare the return parameter and return
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.GAME_TOEVOEGEN, nieuweGame);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AddActivity.this, "Voer een titel, platform en status in", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        status = parent.getItemAtPosition(position).toString();
        statusIndex = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
