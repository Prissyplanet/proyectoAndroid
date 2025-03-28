package com.example.myapplication.db
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class Connection(context:Context, name:String, factory: CursorFactory?, version:Int):
    SQLiteOpenHelper(context,name,factory,version){
    override fun onCreate(db: SQLiteDatabase) {
        var table_products = "create table products (id integer primary key AUTOINCREMENT, " +
                "nombre text, precio real, imagen text, descripcion text)"
        db.execSQL(table_products)
        var table_categories = "create table categories (id integer primary key AUTOINCREMENT," +
                "nombre text, imagen text)"
        db.execSQL(table_categories)
    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}