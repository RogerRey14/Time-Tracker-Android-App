package com.example.joans.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CrearProjecteActivity extends AppCompatActivity {

    public static final String AFEGEIX_PROJECTE = "Afegeix_projecte";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_projectes);

        final EditText textNom = (EditText) findViewById(R.id.nomProjecte);
        final EditText textDescripcio = (EditText) findViewById(R.id.descripcioProjecte);
        Button acceptar = (Button) findViewById(R.id.afegirProjecte);

        acceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nom = textNom.getText().toString();
                String descripcio = textDescripcio.getText().toString();

                //Si no s'omplen els camps mínims per crear el projecte mostrem un avís
                if(nom.equals("") || descripcio.equals("")){
                    Toast.makeText(CrearProjecteActivity.this, "Falten dades del projecte", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Enviem per broadcast les dades que ha introduït l'usuari
                    Intent intent = new Intent(CrearProjecteActivity.AFEGEIX_PROJECTE);
                    intent.putExtra("nomProjecte", nom);
                    intent.putExtra("descripcióProjecte", descripcio);

                    sendBroadcast(intent);

                    startActivity(new Intent(CrearProjecteActivity.this, LlistaActivitatsActivity.class));
                }
            }
        });



    }
}
