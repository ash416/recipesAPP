package com.example.ash41.recipes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class IngredientActivity extends AppCompatActivity {
    static final String TAG = "INGREDIENT ACTIVITY";
    static RecyclerView mIngredientRecyclerView;
    static IngredientRecyclerAdapter mIngredientRecyclerAdapter;
    SearchView mSearchView;
    Toolbar toolbar;
    String[] ingredients;;
    List<String> mListOfChosenIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        ingredients  = getResources().getStringArray(R.array.ingredients_array);

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
        searchSrcTextView.setHintTextColor(getResources().getColor(R.color.grey));
        searchSrcTextView.setHint(getResources().getString(R.string.search_hint));
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
                List<String> foundIngredients = new ArrayList<String>();
                if (!newText.isEmpty() && newText != null){
                    for (String ingredient : ingredients){
                        if (ingredient.toLowerCase().indexOf(newText.toLowerCase()) == 0){
                            foundIngredients.add(ingredient);
                        }
                    }
                }
                searchSrcTextView.setAdapter(new SuggestionAdapter<String>(IngredientActivity.this, android.R.layout.simple_dropdown_item_1line, foundIngredients));
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
    public static void showListOfChosenIngredients(List<String> mListOfChosenIngredients){
        mIngredientRecyclerAdapter = new IngredientRecyclerAdapter(mListOfChosenIngredients);
        mIngredientRecyclerView.setAdapter(mIngredientRecyclerAdapter);
        Log.d(TAG, "Chosen ingredients: " + mListOfChosenIngredients.toString());
    }
}