package com.example.ash41.recipes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button ingredientsRecipesButton = (Button)findViewById(R.id.find_recipes_by_ing);
        ingredientsRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        IngredientActivity.class);
                startActivity(intent);
            }
        });
//        Log.d(TAG, "Connection to database: begins in background");
//        ConnectionToDatabaseTask connectionToDatabaseTask = new ConnectionToDatabaseTask();
//        connectionToDatabaseTask.execute();
//        Log.d(TAG, "Connection to database: established in background");
        final Button nameRecipesButton = (Button)findViewById(R.id.find_recipes_by_name);
        nameRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecipesNameSearchActivity.class);
                startActivity(intent);
            }
        });
    }
    class ConnectionToDatabaseTask extends AsyncTask<Void, Void, Void >{
        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseAdapter mDatabaseAdapter =  new DatabaseAdapter();
            try {
                mDatabaseAdapter.connectToDatabase();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
};
