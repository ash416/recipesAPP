package com.example.ash41.recipes;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private List<Recipe> recipes;
    private boolean ifNameSearch = false;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ImageView imageView;
        final TextView nameView;
        final TextView infoView;
        ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.name);
            imageView = view.findViewById(R.id.image);
            infoView = view.findViewById(R.id.info);
            view.setOnClickListener(this);
        }

        public void onClick(View view){
            int position = getLayoutPosition();
            Intent intent = new Intent(view.getContext(), RecipeInfoActivity.class);
            intent.putExtra("name", recipes.get(position).getName());
            intent.putExtra("image", recipes.get(position).getImage());
            intent.putExtra("description", recipes.get(position).getDescription());
            ArrayList<String> ingredients = recipes.get(position).getIngredients();
            intent.putExtra("ingredients", ingredients.toArray(new String[ingredients.size()]));
            intent.putExtra("have_ingredients", recipes.get(position).getHaveIngredients());
            view.getContext().startActivity(intent);
        }
    }

    RecyclerAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        Recipe recipe = recipes.get(position);
        viewHolder.nameView.setText(recipe.getName());
        Picasso.with(viewHolder.imageView.getContext()).load(recipe.getImage())
                .into(viewHolder.imageView);
        if (!ifNameSearch) {
            viewHolder.infoView.setText("Докупить: " + recipe.getCountNeedIngredients());
        }
    }
    @Override
    public int getItemCount(){
        return recipes.size();
    }
    public void setNameSearchFlag(boolean fl){
        this.ifNameSearch = fl;
    }
}
