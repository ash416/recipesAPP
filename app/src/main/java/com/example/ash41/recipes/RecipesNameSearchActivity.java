package com.example.ash41.recipes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecipesNameSearchActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    Toolbar toolbar;
    MaterialSearchView materialSearchView;
    String[] ingredients = new String[]{
            "Молоко",
            "Курица",
            "Грибы",
            "Яблоки",
            "Шоколад",
            "Кефир",
            "Кумыс",
            "chicken",
            "cucumbers",
            "milk",
            "butter"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        DatabaseTask dataTask = new DatabaseTask();
        dataTask.execute(ingredients);
        toolbar = findViewById(R.id.toolbar_name_search);
        setSupportActionBar(toolbar);
        materialSearchView = findViewById(R.id.name_search_view);
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    class DatabaseTask extends AsyncTask<String, Void, ArrayList<Recipe> > {
        @Override
        protected ArrayList<Recipe> doInBackground(String... ingredients) {
            ArrayList<Recipe> recipes = new ArrayList<>();
            DatabaseAdapter dbAdapter = new DatabaseAdapter();
            try {
                recipes = dbAdapter.getData(ingredients);
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
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> result) {
            mRecyclerView = findViewById(R.id.recipes_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.Adapter mAdapter = new RecyclerAdapter(result);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
