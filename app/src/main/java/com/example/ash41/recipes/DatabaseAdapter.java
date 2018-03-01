package com.example.ash41.recipes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

public class DatabaseAdapter {
    private static final String url = "jdbc:mysql://10.0.2.2:13826/db_recipes";
    private static final String user = "root";
    private static final String password = "15263A";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public ArrayList<Recipe> getData(String[] ingredients) throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            String query = "select id from ingredients where name LIKE" + "\"%" + ingredients[0] + "%\"";
            for (int i = 1; i < ingredients.length; i++) {
                query += " or name Like" + "\"%" + ingredients[i] + "%\"";
            }
            rs = stmt.executeQuery(query);
            query = "select recipe_ID from ingredients_info where ingredient_id=";
            int j = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                if (j++ == 0)
                    query += Integer.toString(id);
                else
                    query += " or ingredient_id=" + id;
            }
            rs = stmt.executeQuery(query);
            HashSet<Integer> recipes_id = new HashSet<>();
            while (rs.next()) {
                int id = rs.getInt("recipe_ID");
                recipes_id.add(id);
            }
            Integer[] intResipeId = recipes_id.toArray(new Integer[recipes_id.size()]);
            query = "SELECT DISTINCT `Name`, `Description`, `Image`, `Ingredients` from recipes where ID=" + Integer.toString(intResipeId[0]);
            for (int i = 1; i < intResipeId.length; i++) {
                query += " or ID=" + intResipeId[i];
            }
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("Name");
                String description = rs.getString("Description");
                String image = rs.getString("Image");
                String ingred = rs.getString("Ingredients");
                ArrayList<Ingredient> array_ingred = new ArrayList<>();
                String[] allIngredients = ingred.split("\n");
                for (int i = 0; i < allIngredients.length; ++i) {
                    String[] ingredInfo = allIngredients[i].split(": ");
                    Ingredient ingr;
                    if (ingredInfo.length == 1) {
                        ingr = new Ingredient(ingredInfo[0], " ");
                        array_ingred.add(ingr);
                    }
                    if (ingredInfo.length == 2) {
                        ingr = new Ingredient(ingredInfo[0], ingredInfo[1]);
                        array_ingred.add(ingr);
                    }
                }
                if (!image.contains("http"))
                    image = "https://kedem.ru/" + image;
                Recipe recipe = new Recipe(name, description, image, array_ingred);
                recipe.setHaveIngredients(ingredients);
                recipes.add(recipe);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            con.close();
        }
        return recipes;
    }

    public Recipe getData(String recipeName) throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            SQLException {
        Recipe recipe = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            String query = "SELECT DISTINCT `Name`, `Description`, `Image`, `Ingredients` from recipes where Name=" + "\'" + recipeName + "\'";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String ingred;
                ArrayList<Ingredient> array_ingred = new ArrayList<>();
                ingred = rs.getString("Ingredients");
                String[] allIngredients = ingred.split("\n");
                for (int i = 0; i < allIngredients.length; ++i) {
                    String[] ingredInfo = allIngredients[i].split(": ");
                    Ingredient ingr;
                    if (ingredInfo.length == 1) {
                        ingr = new Ingredient(ingredInfo[0], " ");
                        array_ingred.add(ingr);
                    }
                    if (ingredInfo.length == 2) {
                        ingr = new Ingredient(ingredInfo[0], ingredInfo[1]);
                        array_ingred.add(ingr);
                    }
                }
                String image = rs.getString("Image");
                if (!image.contains("http"))
                    image = "https://kedem.ru/" + image;
                recipe = new Recipe(rs.getString("Name"), rs.getString("Description"), image, array_ingred);
            }
            return recipe;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            con.close();
        }
        return recipe;
    }
}
