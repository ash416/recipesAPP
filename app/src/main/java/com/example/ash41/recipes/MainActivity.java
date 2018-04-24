package com.example.ash41.recipes;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN ACTIVITY";
    public static DatabaseAdapter mDatabaseAdapter;

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title_main);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setButtons(){
        final Button ingredientsRecipesButton = findViewById(R.id.find_recipes_by_ing);
        ingredientsRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        IngredientActivity.class);
                startActivity(intent);
            }
        });

        final Button nameRecipesButton = findViewById(R.id.find_recipes_by_name);
        nameRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecipesNameSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setButtons();
        ImageView imageView = (ImageView) findViewById(R.id.logo);
        imageView.setImageResource(R.drawable.logo2);
        Log.d(TAG, "Connection to database: begins in background");
        ConnectionToDatabaseTask connectionToDatabaseTask = new ConnectionToDatabaseTask();
        connectionToDatabaseTask.execute();
        Log.d(TAG, "Connection to database: established in background");

    }
    class ConnectionToDatabaseTask extends AsyncTask<Void, Void, Void >{
        @Override
        protected Void doInBackground(Void... voids) {
            mDatabaseAdapter =  new DatabaseAdapter();
            try {
                mDatabaseAdapter.connectToDatabase();
            } catch (DatabaseAdapter.DatabaseAdapterSQLException ex){
                int connectionCounter = MainActivity.mDatabaseAdapter.getConnectionCounter();
                if (connectionCounter < MainActivity.mDatabaseAdapter.MAX_CONNECTION_COUNT){
                    MainActivity.mDatabaseAdapter.setConnectionCounter(connectionCounter + 1);
                    DialogFragment dialogFragment = new AlertDialogWindow();
                    dialogFragment.show(getFragmentManager(), "dlg");
                }
            }
            return null;
        }
    }
    protected void onDestroy(){
        mDatabaseAdapter.closeConnection();
        super.onDestroy();
    }
};
