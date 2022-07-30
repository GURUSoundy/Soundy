package com.example.soundy

import android.media.AudioRecord
import androidx.room.*


@Dao
abstract interface audioRecordDao {
    @Query("SELECT*FROM audioRecords")
    fun getAll():List<audioRecord>

    @Insert
    fun insert(vararg audioRecord: audioRecord)

    @Delete
    fun delete(audioRecord: audioRecord)

    @Delete
    fun delete(audioRecords: Array<audioRecord>)

    @Update
    fun update(audioRecord: audioRecord)

}