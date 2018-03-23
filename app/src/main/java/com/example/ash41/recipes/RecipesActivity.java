package com.example.ash41.recipes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RecipesActivity extends AppCompatActivity {
    static final String TAG = "RECIPES ACTIVITY";
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Intent intent = getIntent();
        String[] mListOfChosenIngredients = intent.getStringArrayListExtra("chosen_ingredients").toArray(new String[0]);
        Log.d(TAG, mListOfChosenIngredients.toString());

        DatabaseTask dataTask = new DatabaseTask();
        dataTask.execute(mListOfChosenIngredients);
    }

    class DatabaseTask extends AsyncTask<String, Void, ArrayList<Recipe> > {
        @Override
        protected ArrayList<Recipe> doInBackground(String... ingredients) {
            ArrayList<Recipe> recipes = new ArrayList<>();
            try {
                recipes = MainActivity.mDatabaseAdapter.getData(ingredients);
                Collections.sort(recipes, new Comparator<Recipe>() {
                    public int compare(Recipe rec1, Recipe rec2) {
                        return rec1.compareTo(rec2);
                    }
                });
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> result) {
            mRecyclerView = findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.Adapter mAdapter = new RecyclerAdapter(result);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
