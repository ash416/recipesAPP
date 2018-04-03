package com.example.ash41.recipes;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN ACTIVITY";
    public static DatabaseAdapter mDatabaseAdapter;
    public static FavoritesDatabaseAdapter mFavoritesDatabaseAdapter;
    public static ArrayList<Recipe> favRecipes;
    public static HashSet<String> nameFavRecipes;

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

    private void setImage(){
        ImageView imageView = findViewById(R.id.logo);
        imageView.setImageResource(R.drawable.recipe);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setButtons();
        setImage();

        Log.d(TAG, "Connection to database: begins in background");
        ConnectionToDatabaseTask connectionToDatabaseTask = new ConnectionToDatabaseTask();
        connectionToDatabaseTask.execute();
        Log.d(TAG, "Connection to database: established in background");

        mFavoritesDatabaseAdapter = new FavoritesDatabaseAdapter(this);
        ConnectionToLocalDatabaseTask connectionToLocalDatabaseTask = new ConnectionToLocalDatabaseTask();
        connectionToLocalDatabaseTask.execute();
    }

    class ConnectionToDatabaseTask extends AsyncTask<Void, Void, Void >{
        @Override
        protected Void doInBackground(Void... voids) {
            mDatabaseAdapter =  new DatabaseAdapter();
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

    class ConnectionToLocalDatabaseTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids){
            mFavoritesDatabaseAdapter.openConnection();
            favRecipes = mFavoritesDatabaseAdapter.getAllData();
            return null;
        }
    }

    protected void onDestroy(){
        try {
            mDatabaseAdapter.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mFavoritesDatabaseAdapter.closeConnection();
        super.onDestroy();
    }


}
