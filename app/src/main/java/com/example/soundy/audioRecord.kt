package com.example.soundy

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "audioRecords")
data class audioRecord(
    var filename : String,
    var filePath : String,
    var timestamp : Long,
    var duration : String,
    var ampsPath : String,
    var dirName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @Ignore
    var isChecked = false
}