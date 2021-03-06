package com.dodsoneng.falldetector.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.dodsoneng.falldetector.R;
import com.dodsoneng.falldetector.adapters.SymptomBaseAdapter;
import com.dodsoneng.falldetector.timers.Configuration;

import java.util.ArrayList;

/**
 * Created by sergio.eng on 10/5/17.
 */

public class SymptomFragment extends DialogFragment  {

    private static String           TAG = "FD.SymptomFragment";

    private ArrayList mSymptomList = new ArrayList();
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d (TAG, "onCreateView()");
        return v;
    }
*/
    @Override
    public void onStart() {
        super.onStart();
        Log.d (TAG, "onStart()");

        Dialog d = getDialog();

        ListView lvDetail = d.findViewById(R.id.lvSymptomList);

        Configuration conf = Configuration.initiate(getContext());
        ArrayList symptomList = conf.getSymptomList();
        lvDetail.setAdapter(new SymptomBaseAdapter(d.getContext(), symptomList));

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d (TAG, "oncCreateDialog()");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

            builder.setView(inflater.inflate(R.layout.symptom_dialog, null))
                    .setMessage("SINTOMAS")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    });

        Dialog d = builder.create();
        return d;

    }




/*
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Get the layout inflater
    LayoutInflater inflater = getActivity().getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    builder.setView(inflater.inflate(R.layout.dialog_signin, null))
            // Add action buttons
            .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            // sign in the user ...
        }
    })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            LoginDialogFragment.this.getDialog().cancel();
        }
    });
    return builder.create();
*/

}
