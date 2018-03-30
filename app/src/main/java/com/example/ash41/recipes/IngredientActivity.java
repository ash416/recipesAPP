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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IngredientActivity extends AppCompatActivity {
    static final String TAG = "INGREDIENT ACTIVITY";
    private static RecyclerView mIngredientRecyclerView;
    private static IngredientRecyclerAdapter mIngredientRecyclerAdapter;
    private String[] ingredients;;
    private List<String> mListOfChosenIngredients;

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
                if (!newText.isEmpty() && newText != null){
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
        final Button backButton = findViewById(R.id.button_back_ingredient);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        setButtons();
        setToolbar();
        ingredients  = getResources().getStringArray(R.array.ingredients_array);
        mIngredientRecyclerView = findViewById(R.id.ingredient_recycler_view);
        mIngredientRecyclerAdapter = new IngredientRecyclerAdapter(mListOfChosenIngredients);
        mListOfChosenIngredients = new ArrayList<>();
        setSearchView();
    }

    public static void showListOfChosenIngredients(List<String> mListOfChosenIngredients){
        mIngredientRecyclerAdapter = new IngredientRecyclerAdapter(mListOfChosenIngredients);
        mIngredientRecyclerView.setAdapter(mIngredientRecyclerAdapter);
        Log.d(TAG, "Chosen ingredients: " + mListOfChosenIngredients.toString());
    }
}