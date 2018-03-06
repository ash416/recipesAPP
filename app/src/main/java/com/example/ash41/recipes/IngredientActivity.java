package com.example.ash41.recipes;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.ash41.recipes.R.id.ingredient_recycler_view;


public class IngredientActivity extends AppCompatActivity {
    static RecyclerView mIngredientRecyclerView;
    static IngredientRecyclerAdapter mIngredientRecyclerAdapter;
    String TAG = "INGREDIENT ACTIVITY";
    MaterialSearchView materialSearchView;
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
    List<String> chosenIngredients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chosenIngredients = new ArrayList<String>();
        setContentView(R.layout.activity_ingredient);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mIngredientRecyclerView = findViewById(R.id.ingredient_recycler_view);
        mIngredientRecyclerAdapter = new IngredientRecyclerAdapter(chosenIngredients);
        Button findReceptButton = findViewById(R.id.find_recipes_by_ing_button);
        findReceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientActivity.this, RecipesActivity.class);
                intent.putStringArrayListExtra("chosen_ingredients", (ArrayList<String>) chosenIngredients);
                startActivity(intent);
            }
        });
        searchViewCode();
    }

    public void searchViewCode(){
        materialSearchView = findViewById(R.id.search_view);
        materialSearchView.setSuggestions(ingredients);
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
        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
        materialSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentIngredient = (String) parent.getItemAtPosition(position);
                if (!chosenIngredients.contains(currentIngredient)){
                    chosenIngredients.add(currentIngredient);
                    showChosenIngredients(chosenIngredients);
                }
                materialSearchView.closeSearch();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.actions_search);
        materialSearchView.setMenuItem(item);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.actions_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onBackPressed(){
        if (materialSearchView.isSearchOpen()){
            materialSearchView.closeSearch();
        }
        else {
            super.onBackPressed();
        }
    }
    public static void showChosenIngredients(List<String> chosenIngredients){
        mIngredientRecyclerAdapter = new IngredientRecyclerAdapter(chosenIngredients);
        mIngredientRecyclerView.setAdapter(mIngredientRecyclerAdapter);
    }
}