package com.example.ash41.recipes;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class DatabaseAdapter {
    private static final String url = "jdbc:mysql://128.72.47.234:13826/db_recipes?useUnicode=true&characterEncoding=utf-8";
    private static final String user = "root";
    private static final String password = "15263A";
    private static final String TAG = "DATABASE ADAPTER";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private boolean isConnected;

    private List<Integer> getIdRecipes(String[] ingredients) throws SQLException {
        String query = "select recipes_id from ingredients where ingredient_name = " + "\'" + ingredients[0].toLowerCase() + "\'";
        for (int i = 1; i < ingredients.length; i++){
            query += " or ingredient_name = " + "\'" + ingredients[i].toLowerCase() + "\'";
        }
        rs = stmt.executeQuery(query);
        List<Integer> recipesId = new ArrayList<>();
        while (rs.next()) {
            String[] ids = rs.getString("recipes_id").split(", ");
            for(String id: ids){
                recipesId.add(Integer.parseInt(id));
            }
        }
        return recipesId;
    }
    private Recipe getRecipe() throws SQLException {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String[] allIngredients = rs.getString("ingredients").split("\n");
        for (String ingredient : allIngredients) {
            String[] ingredInfo = ingredient.split(": ");
            if (ingredInfo.length == 1) ingredients.add(new Ingredient(ingredInfo[0], " "));
            if (ingredInfo.length == 2) ingredients.add(new Ingredient(ingredInfo[0], ingredInfo[1]));
        }
        return new Recipe(rs.getString("name"), rs.getString("recipe"), rs.getString("image"), ingredients);
    }

    void connectToDatabase() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Log.d(TAG, "Connection to database: start");
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con = DriverManager.getConnection(url, user, password);
        stmt = con.createStatement();
        isConnected = true;
        Log.d(TAG, "Connection to database: established");
    }
    void closeConnection() throws SQLException {
        con.close();
        isConnected = false;
        Log.d(TAG, "Connection to database: closed");
    }

    Recipe getData(String recipeName) throws ClassNotFoundException,
                                                IllegalAccessException,
                                                InstantiationException,
                                                SQLException {
        Recipe recipe = null;
        //connectToDatabase();
        String query = "SELECT DISTINCT `Name`, `Description`, `Image`, `Ingredients` from recipes where Name=" + "\'" + recipeName + "\'";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            recipe = getRecipe();
        }
        //closeConnection();
        return recipe;
    }

    ArrayList<Recipe> getData(String[] ingredients) throws ClassNotFoundException,
            SQLException,
            InstantiationException,
            IllegalAccessException, InterruptedException {
        while (!isConnected){
            wait(500);
        }
        ArrayList<Recipe> recipes = new ArrayList<>();
        if (ingredients.length > 0){
            List<Integer> recipesId = getIdRecipes(ingredients);
            String query = "SELECT DISTINCT `name`, `image`, `recipe`, `ingredients` from recipes where id=" + recipesId.get(0);
            for (int i = 1; i < recipesId.size(); i++) {
                query += " or id=" + recipesId.get(i);
            }
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                Recipe recipe = getRecipe();
                recipe.setHaveIngredients(ingredients);
                recipes.add(recipe);
            }
        }
        else{
            recipes = getData();
        }
        return recipes;
    }
    ArrayList<Recipe> getData() throws SQLException, InterruptedException {
        while (!isConnected){
            wait(500);
        }
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        String query = "SELECT DISTINCT `name`, `image`, `recipe`, `ingredients` from recipes";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            Recipe recipe = getRecipe();
            recipe.setHaveIngredients(new String[]{});
            recipes.add(recipe);
        }
        return recipes;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
