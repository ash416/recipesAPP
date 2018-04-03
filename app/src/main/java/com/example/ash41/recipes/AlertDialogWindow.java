package com.example.ash41.recipes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.SQLException;

public class AlertDialogWindow extends DialogFragment {
    @SuppressLint("ResourceType")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_window, null))
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    while (MainActivity.mDatabaseAdapter.getConnectionCounter() < MainActivity.mDatabaseAdapter.MAX_CONNECTION_COUNT && !MainActivity.mDatabaseAdapter.isConnected()){
                        MainActivity.mDatabaseAdapter.connectToDatabase();
                        MainActivity.mDatabaseAdapter.setConnectionCounter(MainActivity.mDatabaseAdapter.getConnectionCounter() + 1);
                    }
                    if (MainActivity.mDatabaseAdapter.getConnectionCounter() >= MainActivity.mDatabaseAdapter.MAX_CONNECTION_COUNT || !MainActivity.mDatabaseAdapter.isConnected()){
                        MainActivity.mDatabaseAdapter.setConnectionCounter(0);
                        DialogFragment dialogFragment = new AlertDialogWindow();
                        dialogFragment.show(getFragmentManager(), "dlg");
                    }
                } catch (DatabaseAdapter.DatabaseAdapterSQLException ex){
                    ex.printStackTrace();
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
