package com.example.ash41.recipes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;


public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.ViewHolder>{
    private List<String> ingredients;
    private String TAG = "INGREDIENT RECYCLER ADAPTER";
    class ViewHolder extends RecyclerView.ViewHolder {
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
                        IngredientActivity.showListOfChosenIngredients(ingredients);
                    }
                }
            });
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
