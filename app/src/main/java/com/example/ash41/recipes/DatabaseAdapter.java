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
    private static final String url = "jdbc:mysql://91.240.84.78:3306/recipes_db?useUnicode=true&characterEncoding=utf-8";
    private static final String user = "root";
    private static final String password = "15263A";
    private static final String TAG = "DATABASE ADAPTER";
    public int MAX_CONNECTION_COUNT = 5;

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private boolean isConnected;
    private int ConnectionCounter;

    DatabaseAdapter(){
        this.isConnected = false;
        this.ConnectionCounter = 0;
    }

    private List<Integer> getIdRecipes(String[] ingredients) {
        String query = "select recipes_id from ingredients where ingredient_name = " + "\'" + ingredients[0].toLowerCase() + "\'";
        for (int i = 1; i < ingredients.length; i++) {
            query += " or ingredient_name = " + "\'" + ingredients[i].toLowerCase() + "\'";
        }
        List<Integer> recipesId = new ArrayList<>();
        try {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] ids = rs.getString("recipes_id").split(", ");
                for (String id : ids) {
                    recipesId.add(Integer.parseInt(id));
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return recipesId;
    }
    private Recipe getRecipe() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try{
            String[] allIngredients = rs.getString("ingredients").split("\n");
            for (String ingredient : allIngredients) {
                String[] ingredInfo = ingredient.split(": ");
                if (ingredInfo.length == 1) ingredients.add(new Ingredient(ingredInfo[0], " "));
                if (ingredInfo.length == 2) ingredients.add(new Ingredient(ingredInfo[0], ingredInfo[1]));
            }
            return new Recipe(rs.getString("name"), rs.getString("recipe"), rs.getString("image"), ingredients);
        }catch (SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    void connectToDatabase() throws DatabaseAdapterSQLException {
        try {
            Log.d(TAG, "Connection to database: start");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            isConnected = true;
            Log.d(TAG, "Connection to database: established");
        }catch (SQLException ex){
            ex.printStackTrace();
            throw (new DatabaseAdapterSQLException());
        }catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }catch (IllegalAccessException ex){
            ex.printStackTrace();
            throw (new DatabaseAdapterSQLException());
        }catch (InstantiationException ex){
            ex.printStackTrace();
        }
    }

    void closeConnection() {
        try{
            con.close();
            isConnected = false;
            Log.d(TAG, "Connection to database: closed");
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    ArrayList<Recipe> getData(String[] ingredients) throws DatabaseAdapterSQLException {
        try{
            while (!isConnected && ConnectionCounter < MAX_CONNECTION_COUNT){
                wait(500);
                ConnectionCounter += 1;
            }
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            if (ingredients.length > 0) {
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
            } else {
                recipes = getData();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            throw (new DatabaseAdapterSQLException());
        }
        return recipes;
    }
    ArrayList<Recipe> getData() throws DatabaseAdapterSQLException {
        try{
            while (!isConnected && ConnectionCounter < MAX_CONNECTION_COUNT){
                wait(500);
                ConnectionCounter += 1;
            }
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        String query = "SELECT DISTINCT `name`, `image`, `recipe`, `ingredients` from recipes";
        try {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                Recipe recipe = getRecipe();
                recipe.setHaveIngredients(new String[]{});
                recipes.add(recipe);
            }
        }catch (SQLException ex){
            throw (new DatabaseAdapterSQLException());
        }
        return recipes;
    }
    ArrayList<String> getIngredients() throws DatabaseAdapterSQLException {
        try{
            while (!isConnected && ConnectionCounter < MAX_CONNECTION_COUNT){
                wait(500);
                ConnectionCounter += 1;
            }
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        ArrayList<String> ingredients = new ArrayList<String>();
        String query = "select `ingredient_name` from ingredients";
        try{
            rs = stmt.executeQuery(query);
            Log.d(TAG, "ok");
            while (rs.next()) {
                ingredients.add(rs.getString("ingredient_name"));
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            throw (new DatabaseAdapterSQLException());
        }
        return ingredients;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int getConnectionCounter() {
        return ConnectionCounter;
    }
    public void setConnectionCounter(int connectionCounter) {
        ConnectionCounter = connectionCounter;
    }
    class DatabaseAdapterSQLException extends SQLException{};
}
