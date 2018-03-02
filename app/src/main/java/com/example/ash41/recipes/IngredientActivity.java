package com.example.ash41.recipes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.miguelcatalan.materialsearchview.MaterialSearchView;


public class IngredientActivity extends AppCompatActivity {
    MaterialSearchView searchView;
    DatabaseAdapter databaseAdapter;
    String TAG = "INGREDIENT ACTIVITY";

    List<String> ingredientsList = Arrays.asList(
            "milk",
            "eggs",
            "flour",
            "salt",
            "sugar",
            "cucumbers",
            "tomatos",
            "chicken",
            "kefir"
    );
    List<String> chosenIngredients = new ArrayList<String>();
    ListView lstView;

    class ConnectionToDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                databaseAdapter.connectToDatabase();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            databaseAdapter.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        databaseAdapter = new DatabaseAdapter();
        ConnectionToDatabase connectionToDatabase = new ConnectionToDatabase();
        connectionToDatabase.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.ingredients_search);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        lstView = (ListView) findViewById(R.id.lstView);
        final ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredientsList);
        lstView.setAdapter(adapter);

        AdapterView.OnItemClickListener adapterOnClickListner = new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ingredient = (String) adapterView.getItemAtPosition(i);
                if (!chosenIngredients.contains(ingredient)){
                    chosenIngredients.add(ingredient);
                    Log.d("CHOSEN INGREDIENTS", chosenIngredients.toString());
                }
                else{
                    //adapterView.getChildAt(i).setBackgroundColor(0);
                    chosenIngredients.remove(ingredient);
                    Log.d("CHOSEN INGREDIENTS", chosenIngredients.toString());
                }
            }
        };

        lstView.setOnItemClickListener(adapterOnClickListner);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {}

            @Override
            public void onSearchViewClosed() {
                ArrayAdapter <String> adapter = new ArrayAdapter(IngredientActivity.this, android.R.layout.simple_list_item_1, ingredientsList);
                lstView.setAdapter(adapter);
            }


        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<String> foundIngredients = new ArrayList<>();
                if (newText != null && !newText.isEmpty()){
                    for (String ingredient:ingredientsList){
                        if (ingredient.contains(newText)){
                            foundIngredients.add(ingredient);
                        }
                    }
                }
                else{
                    foundIngredients = ingredientsList;
                }
                ArrayAdapter <String> adapter = new ArrayAdapter(IngredientActivity.this, android.R.layout.simple_list_item_1, foundIngredients);
                lstView.setAdapter(adapter);
                return false;
            }
        });
        Button findRecipes = (Button)findViewById(R.id.find_recipes_by_ing_2);
        findRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Recipe> recipes = databaseAdapter.getData(chosenIngredients);
                    Log.d(TAG, recipes.get(0).getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(IngredientActivity.this, RecipesActivity.class);
                intent.putStringArrayListExtra("chosen_ingredients", (ArrayList<String>) chosenIngredients);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.actions_search);
        searchView.setMenuItem(item);
        return true;
    }
}