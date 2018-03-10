package com.example.ash41.recipes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button ingredientsRecipesButton = (Button)findViewById(R.id.find_recipes_by_ing);
        ingredientsRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        IngredientActivity.class);
                startActivity(intent);
            }
        });
        final Button nameRecipesButton = (Button)findViewById(R.id.find_recipes_by_name);
        nameRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecipesNameSearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
