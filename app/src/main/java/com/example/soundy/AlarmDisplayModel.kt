package com.example.soundy

data class AlarmDisplayModel(
    val hour: Int, // 0~23
    val minute: Int,
    var onOff: Boolean
) {

    fun makeDataForDB(): String {
        return "$hour:$minute"
    }

    val timeHour: String
        get() {
            val h = "%02d".format(hour)

            return "$h"
        }

    val timeMinute: String
        get() {
            val m = "%02d".format(minute)

            return "$m"
        }

    val ampmText: String
        get() {
            return if (hour < 12) "AM" else "PM"
        }

    val onOffText: String
        get(){
            return if(onOff) "알람 끄기" else "알람 켜기"
        }
}