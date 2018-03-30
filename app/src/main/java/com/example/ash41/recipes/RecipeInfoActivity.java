package com.example.ash41.recipes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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


public class RecipeInfoActivity extends AppCompatActivity {
    String recipeName;
    String image;
    String description;
    String[] ingredients;
    String[] haveIngredients;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        setToolbar();
        setButtons();
        dataInitialization();
        setContext();
    }
    private void setButtons(){
        Button backButton = findViewById(R.id.button_back_recipe_info);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_recipes_info);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title_recipe_info);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void dataInitialization(){
        Intent intent = getIntent();
        recipeName = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        description = intent.getStringExtra("description");
        ingredients = intent.getStringArrayExtra("ingredients");
        haveIngredients = intent.getStringArrayExtra("have_ingredients");
    }

    private void setContext(){
        ImageView imageView = findViewById(R.id.recipeImage);
        TextView textView = findViewById(R.id.recipeName);
        textView.setText(recipeName);
        TextView textViewIngr = findViewById(R.id.textViewIngr);
        for (String ingr : ingredients) {
            textViewIngr.append(getText(ingr));
        }
        Picasso.with(imageView.getContext()).load(image).into(imageView);
        TextView textViewDescr = findViewById(R.id.textViewDesc);
        textViewDescr.setText(description);
    }

    private Spannable getText(String ingredient){
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
}
