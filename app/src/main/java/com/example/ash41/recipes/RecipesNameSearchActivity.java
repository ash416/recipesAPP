package com.example.ash41.recipes;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipesNameSearchActivity extends AppCompatActivity {

    private RecyclerView mRecipesRecyclerView;
    private List<Recipe> mRecipesList;
    private static final String TAG = "RECIPE NAME SEARCH";

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_name_search);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title_recipe_name_search);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setButtons(){
        Button backButton = findViewById(R.id.button_back_recipe_name_search);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private void setSearchView(){
        SearchView mSearchView = findViewById(R.id.recipes_search_view);
        final SearchView.SearchAutoComplete searchSrcTextView = findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchSrcTextView.setTextColor(Color.WHITE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Recipe> foundRecipes = new ArrayList<>();
                if (!newText.isEmpty() && newText != null){
                    for (Recipe recipe : mRecipesList){
                        if (recipe.getName().toLowerCase().indexOf(newText.toLowerCase()) == 0){
                            foundRecipes.add(recipe);
                        }
                    }
                }
                else{
                    foundRecipes = mRecipesList;
                }
                RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(foundRecipes);
                mRecyclerAdapter.setNameSearchFlag(true);
                mRecipesRecyclerView.setAdapter(mRecyclerAdapter);
                return true;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchSrcTextView.setText("");
                return false;
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_name_search);
        setToolbar();
        setButtons();

        DatabaseTask dataTask = new DatabaseTask();
        try {
            mRecipesList = dataTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mRecipesRecyclerView = findViewById(R.id.recipes_recycler_view);
        setSearchView();
    }

    class DatabaseTask extends AsyncTask<Void, Void, ArrayList<Recipe> > {
        @Override
        protected ArrayList<Recipe> doInBackground(Void... voids) {
            ArrayList<Recipe> recipes = new ArrayList<>();
            try {
                recipes = MainActivity.mDatabaseAdapter.getData();
                Collections.sort(recipes, new Comparator<Recipe>() {
                    public int compare(Recipe rec1, Recipe rec2) {
                        return rec1.getName().compareTo(rec2.getName());
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> result) {
            mRecipesRecyclerView = findViewById(R.id.recipes_recycler_view);
            mRecipesRecyclerView.setHasFixedSize(true);
            RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(result);
            mRecyclerAdapter.setNameSearchFlag(true);
            mRecipesRecyclerView.setAdapter(mRecyclerAdapter);
        }
    }
}
