package com.example.ash41.recipes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RecipeInfoActivity extends AppCompatActivity {
    String[] haveIngredients;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        Intent intent = getIntent();
        String recipeName = intent.getStringExtra("name");
        haveIngredients = intent.getStringArrayExtra("have_ingredients");
        DatabaseTask dataTask = new DatabaseTask();
        dataTask.execute(recipeName);
    }

    class DatabaseTask extends AsyncTask<String, Void, Recipe> {
        @Override
        protected Recipe doInBackground(String... recipeName) {
            Recipe recipe = null;
            DatabaseAdapter dbAdapter = new DatabaseAdapter();
            try {
                recipe = dbAdapter.getData(recipeName[0]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return recipe;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Recipe result) {
            ImageView imageView = findViewById(R.id.recipeImage);
            TextView textView = findViewById(R.id.recipeName);
            textView.setText(result.getName());
            String[] ingredients = result.getIngredients().toArray(new String[0]);
            TextView textViewIngr = findViewById(R.id.textViewIngr);
            for (String ingr : ingredients) {
                textViewIngr.append(getText(ingr));
            }
            Picasso.with(imageView.getContext()).load(result.getImage()).into(imageView);

            String description = result.getDescription();
            int position = description.indexOf("\'");
            String finalDescription = "";
            while(position >= 0){
                int nextPosition = description.indexOf("\'", position + 1);
                finalDescription += description.substring(position + 1, nextPosition) + " ";
                position = description.indexOf("\'", nextPosition + 1);
            }
            TextView textViewDescr = findViewById(R.id.textViewDesc);
            textViewDescr.setText(finalDescription);
        }
    }
    protected Spannable getText(String ingredient){
        Spannable text = new SpannableString(ingredient + "\n");
        for(String haveIngr: haveIngredients) {
            if (ingredient.toLowerCase().contains(haveIngr.toLowerCase())) {
                return text;
            }
        }
        text.setSpan(new ForegroundColorSpan(Color.RED), 0, ingredient.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }
}
