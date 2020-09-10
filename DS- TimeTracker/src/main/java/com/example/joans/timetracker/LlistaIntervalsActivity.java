package com.example.joans.timetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Mostra la llista d'intervals d'alguna tasca. De cada interval mostra les
 * dates inicial i final en format dd-MM-aa hh:mm. Si la tasca està sent
 * cronometrada, veiem com el temps i data final va canviant.
 * <p>
 * Aquesta Activity es torna la activa quan, navegant per l'arbre a
 * {@link LlistaActivitatsActivity}, fem un click normal sobre una tasca. En prémer la
 * tecla o botó 'back', 'pugem' un nivell a l'arbre i tornem a veure la tasca i
 * les seves activitats germanes.
 *
 * @author joans
 * @version 6 febrer 2012
 */
public class LlistaIntervalsActivity extends AppCompatActivity {

    /**
     * Llista de dades dels intervals de la tasca però havent fet un cast a la
     * classe abstracta <code>List</code> per tal de fer servir aquest atribut
     * conjuntament amb un <code>Adapter</code> d'Android.
     */
    private List<DadesInterval> llistaDadesIntervals;

    /**
     * Grup de vistes (controls de la interfase gràfica) que consisteix en un
     * <code>TextView</code> per a cada interval a mostrar.
     */
    private ListView intervalsListView;

    /**
     * Adaptador necessari per connectar les dades de la llista d'intervals de
     * la tasca pare actual, amb la interfase, segons el mecanisme estàndard de
     * <code>ListView</code> d'Android.
     * <p>
     * Per tal de fer-lo servir, he hagut d'afegir a la classe
     * <code>DadesInterval</code> tingui un mètode <code>toString</code> que
     * retornarà l'string a mostrar en els <code>TextView</code> (controls de
     * text) de la llista <code>ListView</code>.
     */
    private ArrayAdapter<DadesInterval> aaAct;

    /**
     * Identificador del View les propietats del qual (establertes amb l'editor
     * XML de la interfase gràfica) estableixen com es mostra cada un els items
     * o elements de la llista d'intervals referenciada per l'adaptador
     * {@link #aaAct}.
     *
     * @see LlistaActivitatsActivity#layoutID
     */
    private int layoutID = R.layout.textview_llista_intervals;

    /**
     * Nom de la classe per fer aparèixer als missatges de logging del LogCat.
     *
     * @see Log
     */
    private final String tag = this.getClass().getSimpleName();

    //Creem una variable local per fer-la servir quan sigui necessària
    private String nomActivitatPare;

    /**
     * Estableix com a intervals a visualitzar els de la tasca
     * <code>tascaPare</code>. Aquest mètode és invocat just a l'inici del cicle
     * de vida de la Activity.
     *
     * @param savedInstanceState
     *            de tipus Bundle, però no el fem servir ja que el pas de
     *            paràmetres es fa via l'objecte aplicació
     *            <code>TimeTrackerApplication</code>.
     * @see LlistaActivitatsActivity#onCreate
     */
    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag, "onCreate intervals");

        // Tot aquest mecanisme és anàleg al que trobem al onCreate
        // de LlistaActivitatsActivity.
        setContentView(R.layout.activity_llista_intervals);
        intervalsListView = (ListView) this.findViewById(R.id.listViewIntervals);

        ImageView enrere = (ImageView) findViewById(R.id.enrere);
        ImageView info = (ImageView) findViewById(R.id.info);


        //Li donem la mateixa funcionalitat a la fletxeta que el botó de tirar enrere
        enrere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llistaDadesIntervals = new ArrayList<DadesInterval>();
        aaAct = new ArrayAdapter<DadesInterval>(this, layoutID,
                llistaDadesIntervals){

            @Override
            public View getView(final int position, View convertView, ViewGroup parent){
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                //Cada fila representa un element de la llista, és a dir, un interval
                View fila = layoutInflater.inflate(R.layout.fila_interval, parent, false);

                //Creem objectes de tots els elements del layoutInflater
                TextView dataInici = (TextView) fila.findViewById(R.id.dataInici);
                TextView dataFi = (TextView) fila.findViewById(R.id.dataFi);
                TextView durada = (TextView) fila.findViewById(R.id.duradaInterval);

                //Formategem les dates perquè siguin de tipus String
                String sDataInici = String.format("%1$te-%1$tb-%1$tY %1$tT", llistaDadesIntervals.get(position).getDataInicial());
                String sDataFi = String.format("%1$te-%1$tb-%1$tY %1$tT", llistaDadesIntervals.get(position).getDataFinal());
                String sDurada = llistaDadesIntervals.get(position).toString();

                //Associem a cada interval les seves dades bàsiques
                dataInici.setText(sDataInici);
                dataFi.setText(sDataFi);
                durada.setText(sDurada);

                return fila;
            }

        };

        //Quan es fa click al botó "info" ens porta a l'activitat InfoActivitatsActivity
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LlistaIntervalsActivity.this, InfoActivitatsActivity.class));
            }
        });


            intervalsListView.setAdapter(aaAct);
    }

    // Aquests són els "serveis" que demana aquesta classe
    // a la classe Service GestorArbreActivitats

    /**
     * Sol·licita les dades dels intervals fills d'una tasca que és la activitat
     * pare actual.
     */
    public static final String DONAM_FILLS = "Donam_fills";

    /**
     * Demana que l'activitat pare actual passi a ser el projecte pare de la
     * tasca que és ara l'activitat actual.
     */
    public static final String PUJA_NIVELL = "Puja_nivell";


    /**
     * Rep els "intents" que envia <code>GestorArbreActivitats</code> amb les
     * dades de intervals a mostrar. El receptor els rep tots (no hi ha cap
     * filtre) per que només se'n n'hi envia un, el "TE_FILLS".
     *
     * @author joans
     * @version 6 febrer 2012
     */
    public class Receptor extends BroadcastReceiver {
        /**
         * Nom de la classe per fer aparèixer als missatges de logging del
         * LogCat.
         *
         * @see Log
         */
        private final String tag = this.getClass().getCanonicalName();

        /**
         * Gestiona tots els intents enviats, de moment només el de la acció
         * TE_FILLS. La gestió consisteix en actualitzar la llista de dades que
         * s'està mostrant mitjançant el seu adaptador.
         *
         * @param context
         *            el context (classe) des del qual s'ha llençat l'intent.
         * @param intent
         *            objecte Intent que arriba per "broadcast" i del qual en
         *            fem servir l'atribut "action" per saber quina mena de
         *            intent és i els extres per obtenir les dades a mostrar.
         */
        @Override
        public final void onReceive(final Context context,
                                    final Intent intent) {
            Log.d(tag, "onReceive Receptor LlistaIntervals");
            if (intent.getAction().equals(GestorArbreActivitats.TE_FILLS)) {

                nomActivitatPare = intent.getStringExtra("nom_actual");

                TextView nomActual = (TextView) findViewById(R.id.projecteActual);

                if(nomActivitatPare!=""){
                    nomActual.setText(nomActivitatPare);
                }

                ArrayList<DadesInterval> llistaDadesInter =
                        (ArrayList<DadesInterval>) intent
                                .getSerializableExtra("llista_dades_intervals");
                aaAct.clear();
                for (DadesInterval dadesInter : llistaDadesInter) {
                    aaAct.add(dadesInter);
                }
                aaAct.notifyDataSetChanged();
            }
            Log.i(tag, "final de onReceive LlistaIntervals");
        }
    }

    /**
     * Objecte únic de la classe {@link Receptor}.
     */
    private Receptor receptor;

    @Override
    public final void onBackPressed() {
        Log.i(tag, "onBackPressed");
        sendBroadcast(new Intent(LlistaIntervalsActivity.PUJA_NIVELL));
        Log.d(tag, "enviat intent PUJA_NIVELL");
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
        Log.i(tag, "onResume intervals");

        IntentFilter filter;
        filter = new IntentFilter();
        filter.addAction(GestorArbreActivitats.TE_FILLS);
        receptor = new Receptor();
        registerReceiver(receptor, filter);

        sendBroadcast(new Intent(LlistaIntervalsActivity.DONAM_FILLS));
        Log.d(tag, "enviat intent DONAM_FILLS");

        super.onResume();
    }

    /**
     * Just abans de quedar "oculta" aquesta Activity per una altra, anul·lem el
     * receptor de intents.
     */
    @Override
    public final void onPause() {
        Log.i(tag, "onPause intervals");

        unregisterReceiver(receptor);

        super.onPause();
    }

    // D'aqui en avall els mètodes que apareixen són simplement sobrecàrregues
    // de mètodes de Activity per tal que es mostri un missatge de logging i
    // d'aquesta manera puguem entendre el cicle de vida d'un objecte d'aquesta
    // classe i depurar errors de funcionament de la interfase (on posar què).

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     */
    @Override
    public final void onDestroy() {
        Log.i(tag, "onDestroy intervals");
        super.onDestroy();
    }

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     */
    @Override
    public final void onStart() {
        Log.i(tag, "onStart intervals");
        super.onStart();
    }

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     */
    @Override
    public final void onStop() {
        Log.i(tag, "onStop intervals");
        super.onStop();
    }

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     */
    @Override
    public final void onRestart() {
        Log.i(tag, "onRestart intervals");
        super.onRestart();
    }

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     *
     * @param savedInstanceState
     *            Bundle que de fet no es fa servir.
     */
    @Override
    public final void onSaveInstanceState(final Bundle savedInstanceState) {
        Log.i(tag, "onSaveInstanceState");
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     *
     * @param savedInstanceState
     *            Bundle que de fet no es fa servir.
     */
    @Override
    public final void onRestoreInstanceState(final Bundle savedInstanceState) {
        Log.i(tag, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Mostra un missatge de logging en rotar 90 graus el dispositiu (o
     * simular-ho en l'emulador).
     *
     * @param newConfig
     *            nova configuració {@link Configuration}
     * @see LlistaActivitatsActivity#onConfigurationChanged
     */
    @Override
    public final void onConfigurationChanged(final Configuration newConfig) {
        Log.i(tag, "onConfigurationChanged");
        if (Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, newConfig.toString());
        }
    }

}
