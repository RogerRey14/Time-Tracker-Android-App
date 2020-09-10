package com.example.joans.timetracker;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TimePicker;



public class CrearTascaActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public static final String AFEGEIX_TASCA = "Afegeix_tasca";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tasques);

        final EditText textNom = (EditText) findViewById(R.id.nomTasca);
        final EditText textDescripcio = (EditText) findViewById(R.id.descripcioTasca);
        Button acceptar = (Button) findViewById(R.id.afegirTasca);
        Button escollir = (Button) findViewById(R.id.obrirRellotge);

        //Si es prem el botó d'escollir l'hora s'obre el desplegable del rellotge
        escollir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new EscollirHora();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });


        acceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nom = textNom.getText().toString();
                String descripcio = textDescripcio.getText().toString();

                //Si no s'omplen els camps mínims per crear la tasca mostrem un avís
                if(nom.equals("") || descripcio.equals("")){
                    Toast.makeText(CrearTascaActivity.this, "Falten dades de la tasca", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Enviem per broadcast les dades que ha introduït l'usuari
                    Intent intent = new Intent(CrearTascaActivity.AFEGEIX_TASCA);
                    intent.putExtra("nomTasca", nom);
                    intent.putExtra("descripcióTasca", descripcio);

                    sendBroadcast(intent);

                    finish();
                }
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Button escollir = (Button) findViewById(R.id.obrirRellotge);

        String temps = "";

        //Afegim un zero si l'hora és inferior a 10
        if(hourOfDay < 10){
            temps = temps + "0";
        }

        temps =  temps + hourOfDay + ":";

        //Afegim un zero si els minuts són inferiors a 10
        if(minute < 10){
            temps = temps + "0";
        }

        temps = temps + minute;


        escollir.setText(temps);
    }
}



