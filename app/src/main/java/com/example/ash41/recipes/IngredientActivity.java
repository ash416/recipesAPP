package com.example.ash41.recipes;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class IngredientActivity extends AppCompatActivity {
    String TAG = "INGREDIENT ACTIVITY";
    static RecyclerView mIngredientRecyclerView;
    static IngredientRecyclerAdapter mIngredientRecyclerAdapter;
    SearchView mSearchView;
    Toolbar toolbar;
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
    List<String> mListOfChosenIngredients;

//    @Override
    protected void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIngredientRecyclerView = findViewById(R.id.ingredient_recycler_view);
        mIngredientRecyclerAdapter = new IngredientRecyclerAdapter(mListOfChosenIngredients);
        mListOfChosenIngredients = new ArrayList<String>();

        Button findReceptButton = findViewById(R.id.find_recipes_by_ing_button);
        findReceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientActivity.this, RecipesActivity.class);
                intent.putStringArrayListExtra("chosen_ingredients", (ArrayList<String>) mListOfChosenIngredients);
                startActivity(intent);
            }
        });

        mSearchView = (SearchView) findViewById(R.id.search_view);
        final SearchView.SearchAutoComplete searchSrcTextView =(SearchView.SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchSrcTextView.setThreshold(1);
        searchSrcTextView.setAdapter(new SuggestionAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, null));
        searchSrcTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, (String) parent.getItemAtPosition(position));
                mListOfChosenIngredients.add((String) parent.getItemAtPosition(position));

                showListOfChosenIngredients(mListOfChosenIngredients);
                searchSrcTextView.clearComposingText();
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
                List<String> foundIngredients = new ArrayList<String>();
                if (!newText.isEmpty() && newText != null){
                    for (String ingredient : ingredients){
                        if (ingredient.indexOf(newText) == 0){
                            foundIngredients.add(ingredient);
                        }
                    }
                }
                searchSrcTextView.setAdapter(new SuggestionAdapter<String>(IngredientActivity.this, android.R.layout.simple_dropdown_item_1line, foundIngredients));
                return true;
            }
        });
    }

    public static void showListOfChosenIngredients(List<String> mListOfChosenIngredients){
        mIngredientRecyclerAdapter = new IngredientRecyclerAdapter(mListOfChosenIngredients);
        mIngredientRecyclerView.setAdapter(mIngredientRecyclerAdapter);
    }



}