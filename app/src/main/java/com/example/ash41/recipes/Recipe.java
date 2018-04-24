package com.example.ash41.recipes;

import java.util.ArrayList;


class Ingredient{
    private String name;
    private String count;
    Ingredient(String name, String count){
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

class Recipe {

    private String name;
    private String image;
    private ArrayList<Ingredient> ingredients;
    private String[] haveIngredients;
    private String description;

    Recipe(String name, String description, String image, ArrayList<Ingredient> ingredients){
        this.name=name;
        this.image = image;
        this.ingredients = ingredients;
        this.description = description;
    }
    void setHaveIngredients(String[] haveIngredients){this.haveIngredients = haveIngredients;}

    String[] getHaveIngredients() {return this.haveIngredients; }

    String getDescription(){ return this.description; }

    void setDescription(String description) { this.description = description; }

    String getName() {
        return this.name;
    }

    void setName(String name) {
        this.name = name;
    }

    void setIngredients(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    ArrayList<String> getIngredients(){
        ArrayList<String> str_ingredients = new ArrayList<>();
        for (Ingredient ingr: this.ingredients){
            str_ingredients.add(ingr.getName() + " - " + ingr.getCount());
        }
        return str_ingredients;
    }
    String getImage() { return this.image; }

    int getCountNeedIngredients(){
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

    int compareTo(Recipe rec){
        int count1 = rec.getCountNeedIngredients();
        int count2 = this.getCountNeedIngredients();
        return count1 <count2 ? 1 :(count1 == count2 ? 0: -1);
    }
}