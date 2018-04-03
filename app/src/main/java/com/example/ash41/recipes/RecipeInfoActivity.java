package com.example.ash41.recipes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeInfoActivity extends AppCompatActivity {
    private String recipeName;
    private String image;
    private String description;
    private String[] ingredients;
    private String[] haveIngredients;
    private boolean isFavorite = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        setToolbar();
        dataInitialization();
        setContext();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_recipes_info);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title_recipe_info);
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

    private void dataInitialization() {
        Intent intent = getIntent();
        recipeName = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        description = intent.getStringExtra("description");
        ingredients = intent.getStringArrayExtra("ingredients");
        haveIngredients = intent.getStringArrayExtra("have_ingredients");
    }

    private void setContext() {
        final ImageView imageView = findViewById(R.id.recipeImage);
        TextView textView = findViewById(R.id.recipeName);
        textView.setText(recipeName);
        TextView textViewIngr = findViewById(R.id.textViewIngr);
        for (String ingr : ingredients) {
            textViewIngr.append(getText(ingr));
        }
        Picasso.with(imageView.getContext()).load(image).into(imageView);
        TextView textViewDescr = findViewById(R.id.textViewDesc);
        textViewDescr.setText(description);
        final Button favButton = findViewById(R.id.button_favorites);
        if (MainActivity.favRecipes.contains(recipeName)) {
            isFavorite = true;
            favButton.setBackgroundResource(R.drawable.full);
        } else {
            favButton.setBackgroundResource(R.drawable.nofull);
        }
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    DeleteFromLocDBTask deleteFromLocDBTask = new DeleteFromLocDBTask();
                    deleteFromLocDBTask.execute();
                    favButton.setBackgroundResource(R.drawable.nofull);
                    isFavorite = false;
                } else {
                    AddToLocDBTask addToLocDBTask = new AddToLocDBTask();
                    addToLocDBTask.execute();
                    favButton.setBackgroundResource(R.drawable.full);
                    isFavorite = true;
                }
            }
        });
    }

    private Spannable getText(String ingredient) {
        Spannable text = new SpannableString(ingredient + "\n");
        if (haveIngredients.length != 0) {
            for (String haveIngr : haveIngredients) {
                if (ingredient.toLowerCase().contains(haveIngr.toLowerCase())) {
                    return text;
                }
            }
            text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, ingredient.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return text;
        }
        return text;
    }

    class DeleteFromLocDBTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            MainActivity.mFavoritesDatabaseAdapter.delRecord(recipeName);
            MainActivity.favRecipes = MainActivity.mFavoritesDatabaseAdapter.getAllData();
            return null;
        }
    }

    class AddToLocDBTask extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected  Void doInBackground(Void... voids){
            ArrayList<Ingredient> ingr = new ArrayList<>();
            for (String ingredient : ingredients) {
                String[] ingredInfo = ingredient.split(" - ");
                if (ingredInfo.length == 1) ingr.add(new Ingredient(ingredInfo[0], " "));
                if (ingredInfo.length == 2) ingr.add(new Ingredient(ingredInfo[0], ingredInfo[1]));
            }
            MainActivity.mFavoritesDatabaseAdapter.addRec(recipeName, image, String.join("\n", ingredients), description);
            MainActivity.favRecipes.add(new Recipe(recipeName, description, image, ingr));
            return null;
        }
    }
}
