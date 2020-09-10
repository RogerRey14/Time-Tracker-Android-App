package com.example.joans.timetracker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

//Aquesta classe serveix per poder escollir
public class EscollirHora extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Creem objecte calendari
        Calendar calendari = Calendar.getInstance();
        int hour = calendari.get(Calendar.HOUR_OF_DAY);
        int minute = calendari.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}
