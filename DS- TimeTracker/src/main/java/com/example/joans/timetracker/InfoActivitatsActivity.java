package com.example.joans.timetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;



//Aquesta classe serveix per representar la informació detallada de cada activitat
public class InfoActivitatsActivity extends AppCompatActivity {

    //Hem copiat la classe Receptor
    private class Receptor extends BroadcastReceiver {
        /**
         * Nom de la classe per fer aparèixer als missatges de logging del
         * LogCat.
         *
         * @see Log
         */
        private final String tag = this.getClass().getCanonicalName();

        /**
         * Gestiona tots els intents enviats, de moment només el de la
         * acció TE_FILLS. La gestió consisteix en actualitzar la llista
         * de dades que s'està mostrant mitjançant el seu adaptador.
         *
         * @param context
         * @param intent
         * objecte Intent que arriba per "broadcast" i del qual en fem
         * servir l'atribut "action" per saber quina mena de intent és
         * i els extres per obtenir les dades a mostrar i si el projecte
         * actual és l'arrel de tot l'arbre o no
         *
         */
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.i(tag, "onReceive");
            if (intent.getAction().equals(GestorArbreActivitats.TE_FILLS)) {

                //Recollim la informació detallada de l'activitat actual que s'ha enviat per broadcast
                String nomActivitatPare = intent.getStringExtra("nom_actual");
                String descripcioActivitatPare = intent.getStringExtra("descripció_actual");
                String dataIniciActivitatPare = intent.getStringExtra("data_inici_actual");
                String dataFiActivitatPare = intent.getStringExtra("data_fi_actual");
                String duradaActivitatPare = intent.getStringExtra("durada_actual");

                TextView nom = (TextView) findViewById(R.id.nomActivitat);
                TextView descripcio = (TextView) findViewById(R.id.descripcioActivitat);
                TextView dataInici = (TextView) findViewById(R.id.dataIniciActivitat);
                TextView dataFi = (TextView) findViewById(R.id.dataFiActivitat);
                TextView durada = (TextView) findViewById(R.id.duradaActivitat);

                //Mostrem les dades que hem recollit
                nom.setText(nomActivitatPare);
                descripcio.setText(descripcioActivitatPare);
                dataInici.setText(dataIniciActivitatPare);
                dataFi.setText(dataFiActivitatPare);
                durada.setText(duradaActivitatPare);

            }else{
                // no pot ser
                assert false : "intent d'acció no prevista";
            }
        }
    }


    private Receptor receptor;

    @Override
    public final void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Quan aquesta Activity es mostra per primer cop o després d'haver estat
     * ocultada per alguna altra Activity cal tornar a fer receptor i el seu
     * filtre per que atengui als intents que es redifonen (broadcast). I
     * després, demanar la llista de dades d'interval a mostrar.
     */
    @Override
    public final void onResume() {

        IntentFilter filter;
        filter = new IntentFilter();
        filter.addAction(GestorArbreActivitats.TE_FILLS);
        receptor = new Receptor();
        registerReceiver(receptor, filter);

        startService(new Intent(this, GestorArbreActivitats.class));

        super.onResume();
    }

    /**
     * Just abans de quedar "oculta" aquesta Activity per una altra, anul·lem el
     * receptor de intents.
     */
    @Override
    public final void onPause() {

        unregisterReceiver(receptor);

        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_activitats);


        Button tancar = (Button) findViewById(R.id.tancar);

        //Associem l'acció de tancar la Activity al botó
        tancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
