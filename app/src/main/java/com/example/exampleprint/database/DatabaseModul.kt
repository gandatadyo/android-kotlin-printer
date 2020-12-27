package com.example.exampleprint.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.widget.Toast
import com.example.exampleprint.modul.ModelListAdmin

class DatabaseModul{

    fun OpenQuery(context: Context,ssql:String):Cursor{
        val databaseHelper = DatabaseHelper(context)
        val db = databaseHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(ssql,null)
        return cursor
    }

    fun ExeqQuery(context: Context,ssql:String){
        val databaseHelper = DatabaseHelper(context)
        val db = databaseHelper.writableDatabase
        db.execSQL(ssql)
    }

    fun GetDataProfle(context: Context): ModelListAdmin {
        val databaseHelper = DatabaseHelper(context)
        val db = databaseHelper.readableDatabase
        val ssql = "select * from dbmadmin limit 1"
        val cursor: Cursor = db.rawQuery(ssql,null)
        var modellist = ModelListAdmin(0,"-","-","-",0,"-","-")
        if(cursor.count>0){
            cursor.moveToPosition(0)
            modellist = ModelListAdmin(
                cursor.getInt(cursor.getColumnIndex("IDAccount")),
                cursor.getString(cursor.getColumnIndex("NameAdmin")),
                cursor.getString(cursor.getColumnIndex("Username")),
                cursor.getString(cursor.getColumnIndex("Password")),
                cursor.getInt(cursor.getColumnIndex("isDelete")),
                cursor.getString(cursor.getColumnIndex("TimeCreated")),
                cursor.getString(cursor.getColumnIndex("TimeUpdated"))
            )
        }
        return modellist
    }

    fun InsertAccount(context: Context,item:ContentValues){
        val databaseHelper = DatabaseHelper(context)
        val db = databaseHelper.writableDatabase
        val result:Long = db.insert("dbmadmin",null,item)
        if(result>0)
        else Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
    }

}