package com.example.reminder.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class UpdateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText titelTekst;
    private EditText platformTekst;
    private Spinner spinner;
    private String nieuweStatus;
    private int nieuweStatusIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Spinner
        spinner = findViewById(R.id.spinnerUpdate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Init local variables
        titelTekst = findViewById(R.id.titelTextUpdate);
        platformTekst = findViewById(R.id.platformTextUpdate);

//Obtain the parameters provided by MainActivity
        final Game gameUpdate = getIntent().getParcelableExtra(MainActivity.GAME_WIJZIGEN);
        titelTekst.setText(gameUpdate.getTitel());
        platformTekst.setText(gameUpdate.getPlatform());
        spinner.setSelection(gameUpdate.getStatusIndex());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nieuweTitel = titelTekst.getText().toString();
                String nieuwePlatform = platformTekst.getText().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                final String nieuweDatum = formatter.format(date);

                if ((!TextUtils.isEmpty(nieuweTitel)) && (!TextUtils.isEmpty(nieuwePlatform)) &&
                        (!TextUtils.isEmpty(nieuweStatus))) {
                    gameUpdate.setTitel(nieuweTitel);
                    gameUpdate.setPlatform(nieuwePlatform);
                    gameUpdate.setDatum(nieuweDatum);
                    gameUpdate.setStatus(nieuweStatus);
                    gameUpdate.setStatusIndex(nieuweStatusIndex);
                    //Prepare the return parameter and return
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.GAME_WIJZIGEN, gameUpdate);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(UpdateActivity.this, "Voer een titel en platform in", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        nieuweStatus = parent.getItemAtPosition(position).toString();
        nieuweStatusIndex = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
