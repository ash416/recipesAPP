package com.example.ash41.recipes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//import static android.support.v4.content.ContextCompat.startActivity;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.ViewHolder>{
    public List<String> ingredients;
    String TAG = "INGREDIENT RECYCLER ADAPTER";
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView ingredientName;
        final CheckBox ingredientCheckbox;
        ViewHolder(View view) {
            super(view);
            ingredientName = view.findViewById(R.id.ingredient_name);
            ingredientCheckbox = view.findViewById(R.id.ingredient_checkbox);
            ingredientCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked){
                        int position = getLayoutPosition();
                        Log.d(TAG, ingredients.get(position));
                        ingredients.remove(ingredients.get(position));
                        //IngredientActivity.showChosenIngredients(ingredients);
                    }
                }
            });
            view.setOnClickListener(this);
        }

        public void onClick(View view){
            int position = getLayoutPosition();
        }
    }

    IngredientRecyclerAdapter(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    @Override
    public IngredientRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position){
        final String ingredient = ingredients.get(position);
        viewHolder.ingredientName.setText(ingredient);
    }
    @Override
    public int getItemCount(){
        return ingredients.size();
    }
}
