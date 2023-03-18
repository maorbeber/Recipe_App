package com.example.recipeapp.DatabaseService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.recipeapp.Model.Recipe;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {


    public static final String DBName = "recipe_Database";
    public static final String TABLEName = "Reciper";
    public static final String Col_1 = "Description";
    public static final String Col_2 = "Image";
    public static final String Col_3 = "id";








    public DbHelper(Context context) {
        super(context,DBName,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {

        myDB.execSQL(" create table " + TABLEName + "(" + Col_1 + " TEXT," + Col_2 + " TEXT,"
                + Col_3 + " INTEGER PRIMARY KEY AUTOINCREMENT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int i, int i1) {
        myDB.execSQL("DROP TABLE IF EXISTS " + TABLEName);
        onCreate(myDB);
    }


    public void addRecipe(Recipe recipe){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(Col_1,recipe.getDescription());
        cv.put(Col_2,recipe.getImage());
        db.insert(TABLEName,null,cv);
        db.close();
    }
    public ArrayList<Recipe> getRecipeData(){
        ArrayList<Recipe> recipeArrayList=new ArrayList<Recipe>();

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query(TABLEName, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            recipeArrayList.add(new Recipe(cursor.getString(0),cursor.getString(1)));
            cursor.moveToNext();
        }
        return recipeArrayList;
    }


}
