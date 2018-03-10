package com.example.ash41.recipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

public class RecipesNameSearchActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_name_search);

//        RecipesActivity.DatabaseTask dataTask = new RecipesActivity.DatabaseTask();
//        dataTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
