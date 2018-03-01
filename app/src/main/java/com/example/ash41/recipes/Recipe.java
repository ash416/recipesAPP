package com.example.ash41.recipes;

import java.util.ArrayList;


class Ingredient{
    private String name;
    private String count;
    public Ingredient(String name, String count){
        this.name = name;
        this.count = count;
    }
    void setName(String name){
        this.name = name;
    }
    String getName(){
        return this.name;
    }
    void setCount(String count){
        this.count = count;
    }
    String getCount(){
        return this.count;
    }
}

public class Recipe {

    private String name;
    private String image;
    private ArrayList<Ingredient> ingredients;
    private String[] haveIngredients;
    private String description;

    public Recipe(String name, String description, String image, ArrayList<Ingredient> ingredients){
        this.name=name;
        this.image = image;
        this.ingredients = ingredients;
        this.description = description;
    }
    public void setHaveIngredients(String[] haveIngredients){ this.haveIngredients = haveIngredients; }
    public String[] getHaveIngredients() {return this.haveIngredients; }
    public String getDescription(){ return this.description; }
    public void setDescription(String description) { this.description = description; }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredients(){
        ArrayList<String> str_ingredients = new ArrayList<>();
        for (Ingredient ingr: this.ingredients){
            str_ingredients.add(ingr.getName() + " - " + ingr.getCount());
        }
        return str_ingredients;
    }
    public String getImage() { return this.image; }

    public int getCountNeedIngredients(){
        int countIngredients = 0;
        String[] haveIngredients = this.haveIngredients;
        ArrayList<String> ingredients = this.getIngredients();
        for (String ingredient: ingredients){
            for (String item: haveIngredients){
                if (ingredient.contains(item))
                    countIngredients++;
            }
        }
        return ingredients.size() - countIngredients;
    }

    public int compareTo(Recipe rec){
        int count1 = rec.getCountNeedIngredients();
        int count2 = this.getCountNeedIngredients();
        return count1 <count2 ? 1 :(count1 == count2 ? 0: -1);
    }
}