package com.example.queuemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {


    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        //we create object of Database to call other function in different activity class
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String qry2="create table customer(username,address,password,number)";
        sqLiteDatabase.execSQL(qry2);
        String qry1="create table business(username,business_name,business_id,address,password) ";
        sqLiteDatabase.execSQL(qry1);

    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void register_business(String username, String business_name, int business_id, String address, String password){
        ContentValues cv = new ContentValues();
        cv.put("username",username);
        cv.put("business_name",business_name);
        cv.put("business_id",business_id);
        cv.put("address",address);
        cv.put("password",password);
        SQLiteDatabase db =  getWritableDatabase();
        db.insert("business",null,cv);
        db.close();
    }

    public int login(String username, String password){
        int result=0;
        String str[]= new String[2];
        str[0]=username;
        str[1]=password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from business where username=? and password=?",str);

        if(c.moveToFirst()){
            result=1;
        }
        return result;
    }

    public void register_customer(String username, String address, String password, int number){
        ContentValues cv = new ContentValues();
        cv.put("username",username);
        cv.put("address",address);
        cv.put("password",password);
        cv.put("number",number);
        SQLiteDatabase db =  getWritableDatabase();
        db.insert("customer",null,cv);
        db.close();
    }

    public int clogin(String username, String password){
        int result=0;
        String str[]= new String[2];
        str[0]=username;
        str[1]=password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from customer where username=? and password=?",str);

        if(c.moveToFirst()){
            result=1;
        }
        return result;
    }

}
