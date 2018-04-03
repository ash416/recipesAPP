package com.example.ash41.recipes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FavoritesDatabaseAdapter {
    private static final String DB_NAME = "favorites_db";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "favorites";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RECIPE_NAME = "recipe_name";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_RECIPE = "recipe";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_RECIPE_NAME + " text, " +
                    COLUMN_IMAGE + " text" +
                    COLUMN_INGREDIENTS + " text" +
                    COLUMN_RECIPE + " text" +
                    ");";

    private final Context mContext;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    FavoritesDatabaseAdapter(Context ctx) {
        mContext = ctx;
    }

    void openConnection() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    void closeConnection() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    ArrayList<Recipe> getAllData() {
        Cursor mCursor = mDB.query(DB_TABLE, null, null, null, null, null, null);
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        if (mCursor.moveToFirst()) {

            int recipeNameColIndex = mCursor.getColumnIndex("recipe_name");
            int imageColIndex = mCursor.getColumnIndex("image");
            int ingredientsColIndex = mCursor.getColumnIndex("ingredients");
            int recipeColIndex = mCursor.getColumnIndex("recipe");

            do {
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                String[] allIngredients = mCursor.getString(ingredientsColIndex).split("\n");
                for (String ingredient : allIngredients) {
                    String[] ingredInfo = ingredient.split(": ");
                    if (ingredInfo.length == 1) ingredients.add(new Ingredient(ingredInfo[0], " "));
                    if (ingredInfo.length == 2) ingredients.add(new Ingredient(ingredInfo[0], ingredInfo[1]));
                }
                String nameRecipe = mCursor.getString((recipeNameColIndex));
                MainActivity.nameFavRecipes.add(nameRecipe);
                recipes.add(new Recipe(nameRecipe, mCursor.getString(recipeColIndex), mCursor.getString(imageColIndex), ingredients));

            } while (mCursor.moveToNext());
        }
        return recipes;
    }

    // добавить запись в DB_TABLE
    public void addRec(String recipe_name, String image, String ingredients, String recipe) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RECIPE_NAME, recipe_name);
        cv.put(COLUMN_IMAGE, image);
        cv.put(COLUMN_INGREDIENTS, ingredients);
        cv.put(COLUMN_RECIPE, recipe);
        mDB.insert(DB_TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    void delRecord(String name) {
        mDB.delete(DB_TABLE, COLUMN_RECIPE_NAME + " = " + name, null);
    }

    private class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                 int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
