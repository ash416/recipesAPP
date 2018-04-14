package com.example.ash41.recipes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class IngredientActivity extends AppCompatActivity {
    static final String TAG = "INGREDIENT ACTIVITY";
    private static RecyclerView mIngredientRecyclerView;
    private static IngredientRecyclerAdapter mIngredientRecyclerAdapter;
    private List<String> ingredients;
    private List<String> mListOfChosenIngredients;

    @SuppressLint("RestrictedApi")
    private void setSearchView(){
        SearchView mSearchView = findViewById(R.id.search_view);
        final SearchView.SearchAutoComplete searchSrcTextView = findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchSrcTextView.setHintTextColor(getResources().getColor(R.color.grey));
        searchSrcTextView.setHint(getResources().getString(R.string.search));
        searchSrcTextView.setThreshold(1);
        searchSrcTextView.setAdapter(new SuggestionAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, null));
        searchSrcTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, (String) parent.getItemAtPosition(position));
                String mChosenIngredient = (String) parent.getItemAtPosition(position);
                if (!mListOfChosenIngredients.contains(mChosenIngredient)){
                    mListOfChosenIngredients.add(mChosenIngredient);
                }
                showListOfChosenIngredients(mListOfChosenIngredients);
                searchSrcTextView.setText("");
                return;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<String> foundIngredients = new ArrayList<>();
                if (!newText.isEmpty()){
                    for (String ingredient : ingredients){
                        if (ingredient.toLowerCase().indexOf(newText.toLowerCase()) == 0){
                            foundIngredients.add(ingredient);
                        }
                    }
                }
                searchSrcTextView.setAdapter(new SuggestionAdapter<>(IngredientActivity.this, android.R.layout.simple_dropdown_item_1line, foundIngredients));
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

    private void setToolbar(){
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final TextView mTitle = toolbar.findViewById(R.id.toolbar_title_ingredient);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setButtons(){
        final Button findReceptButton = findViewById(R.id.find_recipes_by_ing_button);
        findReceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientActivity.this, RecipesActivity.class);
                intent.putStringArrayListExtra("chosen_ingredients", (ArrayList<String>) mListOfChosenIngredients);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        ingredients = getIngredientsInBackground();
        setButtons();
        setToolbar();
        mIngredientRecyclerView = findViewById(R.id.ingredient_recycler_view);
        mIngredientRecyclerAdapter = new IngredientRecyclerAdapter(mListOfChosenIngredients);
        mListOfChosenIngredients = new ArrayList<>();
        setSearchView();
    }
    public List<String> getIngredientsInBackground(){
        DatabaseTask databaseTask = new DatabaseTask();
        try {
            ingredients = databaseTask.execute().get();
        } catch (Exception e) {
            int connectionCounter = MainActivity.mDatabaseAdapter.getConnectionCounter();
            if (connectionCounter < MainActivity.mDatabaseAdapter.MAX_CONNECTION_COUNT){
                MainActivity.mDatabaseAdapter.setConnectionCounter(connectionCounter + 1);
                DialogFragment dialogFragment = new AlertDialogWindow();
                dialogFragment.show(getFragmentManager(), "dlg");
            }
        }
        return ingredients;
    }
    public static void showListOfChosenIngredients(List<String> mListOfChosenIngredients){
        mIngredientRecyclerAdapter = new IngredientRecyclerAdapter(mListOfChosenIngredients);
        mIngredientRecyclerView.setAdapter(mIngredientRecyclerAdapter);
        Log.d(TAG, "Chosen ingredients: " + mListOfChosenIngredients.toString());
    }
    class DatabaseTask extends AsyncTask<Void, Void, ArrayList<String> > {
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> ingredients = new ArrayList<>();
            try {
                ingredients = MainActivity.mDatabaseAdapter.getIngredients();
                Collections.sort(ingredients, new Comparator<String>() {
                    public int compare(String ing1, String ing2) {
                        return ing1.compareTo(ing2);
                    }
                });
            } catch (DatabaseAdapter.DatabaseAdapterSQLException e) {
                int connectionCounter = MainActivity.mDatabaseAdapter.getConnectionCounter();
                if (connectionCounter < MainActivity.mDatabaseAdapter.MAX_CONNECTION_COUNT){
                    MainActivity.mDatabaseAdapter.setConnectionCounter(connectionCounter + 1);
                    DialogFragment dialogFragment = new AlertDialogWindow();
                    dialogFragment.show(getFragmentManager(), "dlg");
                }
            }
            return ingredients;
        }
    }
}