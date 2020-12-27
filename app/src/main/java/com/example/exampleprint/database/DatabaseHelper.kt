package com.example.exampleprint.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        const val DATABASE_NAME="dbrent"
        const val DATABASE_VERSION=1

        private val ssqlCreateAccount = "CREATE TABLE dbmadmin(\n" +
                "ID integer primary key AUTOINCREMENT,\n" +
                "IDAccount integer default '',\n" +
                "NameAdmin varchar(50) default '',\n" +
                "Username varchar(32) default '',\n" +
                "Password varchar(32) default '',\n" +
                "isDelete integer default 0,\n" +
                "TimeCreated varchar(50) default '',\n" +
                "TimeUpdated varchar(50) default '');"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(ssqlCreateAccount)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val ssql = "drop table if exists dbmadmin;"
        db?.execSQL(ssql)
    }
}