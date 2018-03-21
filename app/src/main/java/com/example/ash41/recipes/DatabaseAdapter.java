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
    private static final String url = "jdbc:mysql://128.72.47.234:13826/db_recipes";
    private static final String user = "root";
    private static final String password = "15263A";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private static final String TAG = "DATABASE ADAPTER";

    private List<Integer> getIdIngredients(String[] ingredients) throws SQLException {
        List<Integer> id = new ArrayList<>();
        String query = "select id from ingredients where name LIKE" + " \'%" + ingredients[0] + "%\'";
        for (int i = 1; i < ingredients.length; i++) {
            query += " or name LIKE" + "\"%" + ingredients[i] + "%\"";
        }
        rs = stmt.executeQuery(query);
        while (rs.next())
            id.add(rs.getInt("id"));
        return id;
    }

    private List<Integer> getIdRecipes(List<Integer> idIngredients) throws SQLException {
        String query = "select recipe_ID from ingredients_info where ingredient_id=" + idIngredients.get(0);
        for (int j = 1; j < idIngredients.size(); ++j) {
            query += " or ingredient_id=" + idIngredients.get(j);
        }
        rs = stmt.executeQuery(query);
        List<Integer> recipes_id = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("recipe_ID");
            recipes_id.add(id);
        }
        return recipes_id;
    }

    private Recipe getRecipe() throws SQLException {
        ArrayList<Ingredient> array_ingred = new ArrayList<>();
        String[] allIngredients = rs.getString("Ingredients").split("\n");
        for (String ingredient : allIngredients) {
            String[] ingredInfo = ingredient.split(": ");
            if (ingredInfo.length == 1) array_ingred.add(new Ingredient(ingredInfo[0], " "));
            if (ingredInfo.length == 2) array_ingred.add(new Ingredient(ingredInfo[0], ingredInfo[1]));
        }
        String image = rs.getString("Image");
        if (!image.contains("http"))
            image = "https://kedem.ru/" + image;
        return new Recipe(rs.getString("Name"), rs.getString("Description"), image, array_ingred);
    }

    void connectToDatabase() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con = DriverManager.getConnection(url, user, password);
        stmt = con.createStatement();
        Log.d(TAG, "connection established");
    }
    void closeConnection() throws SQLException {
        con.close();
        Log.d(TAG, "connection closed");
    }

    Recipe getData(String recipeName) throws ClassNotFoundException,
                                                IllegalAccessException,
                                                InstantiationException,
                                                SQLException {
        Recipe recipe = null;
        connectToDatabase();
        String query = "SELECT DISTINCT `Name`, `Description`, `Image`, `Ingredients` from recipes where Name=" + "\'" + recipeName + "\'";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            recipe = getRecipe();
        }
        closeConnection();
        return recipe;
    }

    ArrayList<Recipe> getData(String[] ingredients) throws ClassNotFoundException,
                                                            SQLException,
                                                            InstantiationException,
                                                            IllegalAccessException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        connectToDatabase();
        List<Integer> id = getIdIngredients(ingredients);
        List<Integer> idRecipes = getIdRecipes(id);
        String query = "SELECT DISTINCT `Name`, `Description`, `Image`, `Ingredients` from recipes where ID=" + idRecipes.get(0);
        for (int i = 1; i < idRecipes.size(); i++) {
            query += " or ID=" + idRecipes.get(i);
        }
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            Recipe recipe = getRecipe();
            recipe.setHaveIngredients(ingredients);
            recipes.add(recipe);
        }
         closeConnection();

        return recipes;
    }
}
