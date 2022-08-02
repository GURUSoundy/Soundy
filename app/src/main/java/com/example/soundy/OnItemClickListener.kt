package com.example.soundy

import android.media.MediaRecorder

interface OnItemClickListener {
    fun onItemClickListener(position: Int)
    fun onItemLongClickListener(position: Int)
}