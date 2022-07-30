package com.example.soundy

import android.media.AudioRecord
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(audioRecord::class), version = 1)
abstract class recordDatabase : RoomDatabase() {
    abstract fun audioRecordDao() : audioRecordDao
}