package com.example.ash41.recipes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//import static android.support.v4.content.ContextCompat.startActivity;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private List<Recipe> recipes;

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

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void onClick(View view){
            int position = getLayoutPosition();
            Intent intent = new Intent(view.getContext(), RecipeInfoActivity.class);
            intent.putExtra("name", recipes.get(position).getName());
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        Recipe recipe = recipes.get(position);
        viewHolder.nameView.setText(recipe.getName());
        Picasso.with(viewHolder.imageView.getContext()).load(recipe.getImage())
                .into(viewHolder.imageView);
        viewHolder.infoView.setText("Докупить: " + recipe.getCountNeedIngredients());
    }
    @Override
    public int getItemCount(){
        return recipes.size();
    }
}
