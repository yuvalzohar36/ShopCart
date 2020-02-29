package com.example.shoppingcart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "shopping.db";
    public static final String TABLE_NAME = "shopping_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "CHECKIF";
    public static final String COL_4 = "QUANTITY";
    public static final String TABLE_NAME_2 = "newshopping_table";
    public static final String NEWCOL_1 = "NAME";
    public static final String NEWCOL_2 = "CHECKIF";
    public static final String NEWCOL_3 = "QUANTITY";






    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,CHECKIF BOOL,QUANTITY INTEGER)");
        db.execSQL("create table " + TABLE_NAME_2 +" (NAME TEXT,CHECKIF BOOL,QUANTITY INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }

    public boolean insertData(String name, boolean value,Integer quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, value);
        contentValues.put(COL_4, quantity);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertData_2(String name_2, boolean value_2, String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWCOL_1, name_2);
        contentValues.put(NEWCOL_2, value_2);
        contentValues.put(NEWCOL_3, quantity);
        long result = db.insert(TABLE_NAME_2, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME,null);
        return res;
    }
    public Cursor getAllData_2() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME_2,null);
        return res;
    }

    public Cursor getSpecificQuantity(String TheWord){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COL_4};
        String selection = COL_2+" LIKE ?";
        String [] selection_args = {TheWord};
        Cursor cursor = db.query(TABLE_NAME,projections,selection,selection_args,null,null,null);
        return cursor;
    }


    public Integer deleteData (String delete) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "NAME = ?",new String[] {delete});


    }

    public Integer deleteData_2 (String delete) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_2, "NAME = ?",new String[] {delete});


    }

    public boolean updateData(String name,boolean value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, value);
        db.update(TABLE_NAME, contentValues, "NAME = ?",new String[] {name});
        return true;

    }

    public boolean updateData_2(String name,boolean value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWCOL_2, value);
        db.update(TABLE_NAME_2, contentValues, "NAME = ?",new String[] {name});
        return true;
    }



    public void Deleta_all_data(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_2, null, null);

    }
    }