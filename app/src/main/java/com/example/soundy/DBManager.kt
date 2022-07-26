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
        db!!.execSQL("CREATE TABLE Directory (dirname text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS User")
        db!!.execSQL("DROP TABLE IF EXISTS Directory")
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

//    fun checkIdExist(id: String): Boolean {
//        val db = this.readableDatabase
//
//        // 리턴받고자 하는 컬럼 값 ID의 array
//        val projection = arrayOf(BaseColumns._ID)
//           // ,registerUser.userData.COLUMN_NAME_ID, registerUser.userData.COLUMN_NAME_PASSWORD)
//
//        //  WHERE "id" = id AND "password"=password 구문 적용하는 부분
//        val selection = "${registerUser.userData.COLUMN_NAME_ID} = ?"
//        val selectionArgs = arrayOf(id)
//
////         정렬조건 지정
////        val sortOrder = "${FeedEntry.COLUMN_NAME_SUBTITLE} DESC"
//
//        val cursor = db.query(
//            registerUser.userData.TABLE_NAME,   // 테이블
//            projection,             // 리턴 받고자 하는 컬럼
//            selection,              // where 조건
//            selectionArgs,          // where 조건에 해당하는 값의 배열
//            null,                   // 그룹 조건
//            null,                   // having 조건
//            null               // orderby 조건 지정
//        )
//        return cursor.count>0
//    }

}
