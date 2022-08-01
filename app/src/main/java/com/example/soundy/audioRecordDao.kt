package com.example.soundy

import android.media.AudioRecord
import androidx.room.*


@Dao
abstract interface audioRecordDao {
    @Query("SELECT * FROM audioRecords WHERE dirName = :dirName")
    fun getAll(dirName: String):List<audioRecord>

    @Insert
    fun insert(vararg audioRecord: audioRecord)

    @Query("DELETE FROM audioRecords")
    fun delete()

    @Delete
    fun delete(audioRecords: Array<audioRecord>)

    @Update
    fun update(audioRecord: audioRecord)

}