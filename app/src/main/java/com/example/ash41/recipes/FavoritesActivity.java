package com.example.ash41.recipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_recipes);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title_recipe);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Избранное");
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        setToolbar();

        ArrayList<Recipe> recipes = MainActivity.favRecipes;
        mRecyclerView = findViewById(R.id.recipes_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(recipes);
        mRecyclerAdapter.setFavoritesFlag(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}
