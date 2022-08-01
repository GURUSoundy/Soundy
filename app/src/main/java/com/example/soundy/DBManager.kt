package com.example.soundy

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE User (id text PRIMARY KEY, nickname text, password text)")
        db!!.execSQL("CREATE TABLE Directory (dirname text, endDate text)")
        db!!.execSQL("CREATE TABLE File (fileName text PRIMARY KEY, dirName text, addFile text, memo text)")
        db!!.execSQL("CREATE TABLE TodoList(date DATE not null, list text, isChecked int)")  // isChecked: 0 - 체크X / 1 - 체크O
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS User")
        db!!.execSQL("DROP TABLE IF EXISTS Directory")
        db!!.execSQL("DROP TABLE IF EXISTS File")
        db!!.execSQL("DROP TABLE IF EXISTS TodoList")
        onCreate(db)
    }

    /*DB에 회원 정보 삽입*/
    fun insertUser(nickname: String, id: String, password: String){
        val db = this.writableDatabase

        /* insert될 데이터값 */
        db.execSQL(
            "INSERT INTO User VALUES('" + nickname + "'" + ", '" + id + "'" + ", '" + password + "');")

        db.close()
    }

    /* 테이블 내 모든 데이터 삭제 */
    fun deleteData(tableName: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM '$tableName';")
        db.close()
    }
    fun saveFile(fileName: String, dirName: String, addFile: String, stt: String, routine: String){
        val db = this.writableDatabase

        db.execSQL("INSERT INTO File VALUES('" + fileName + "', '" + dirName + "', '" + addFile + "', '" + stt + "', '" + routine + "');")

        db.close()
    }

}
