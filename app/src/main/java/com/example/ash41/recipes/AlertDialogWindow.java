package com.example.ash41.recipes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
                    MainActivity.mDatabaseAdapter.connectToDatabase();
                } catch (DatabaseAdapter.DatabaseAdapterSQLException ex){
                    int connectionCounter = MainActivity.mDatabaseAdapter.getConnectionCounter();
                    if (connectionCounter < MainActivity.mDatabaseAdapter.MAX_CONNECTION_COUNT){
                        MainActivity.mDatabaseAdapter.setConnectionCounter(connectionCounter + 1);
                        DialogFragment dialogFragment = new AlertDialogWindow();
                        dialogFragment.show(getFragmentManager(), "dlg");
                    }
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
