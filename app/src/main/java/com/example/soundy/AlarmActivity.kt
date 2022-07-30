package com.example.soundy


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.util.*

class AlarmActivity : AppCompatActivity() {

    lateinit var edtHour : EditText
    lateinit var edtMinute : EditText
    lateinit var rgTime : RadioGroup
    lateinit var rbAm : RadioButton
    lateinit var rbPm : RadioButton
    lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        edtHour = findViewById(R.id.editTime)
        edtMinute = findViewById(R.id.editMinute)
        rgTime = findViewById(R.id.rgTime)
        rbAm = findViewById(R.id.rbAm)
        rbPm = findViewById(R.id.rbPm)
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            finish()
        }

        /* 뷰 초기화 */
        alarmOnoff()
        saveAlarm()

        val model = fetchDataFromSharedPreferences() // 저장된 데이터 가져오기
        renderView(model) // 뷰에 데이터 그려주기
    }


    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(M_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        /* sharedPreferences에서 데이터 가져오기 */
        val timeDBValue = sharedPreferences.getString(M_ALARM_KEY, "07:00") ?: "07:00"
        val onOffDBValue = sharedPreferences.getBoolean(M_ONOFF_KEY, false)

        val alarmData = timeDBValue.split(":") // 시:분 형식으로 가져온 데이터 스플릿

        val alarmModel = AlarmDisplayModel(alarmData[0].toInt(), alarmData[1].toInt(), onOffDBValue)

        // 보정 조정 예외처 (브로드 캐스트 가져오기)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            M_ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) // 있으면 가져오고 없으면 안 만든다. (null)

        if ((pendingIntent == null) and alarmModel.onOff) {
            //알람은 꺼져있는데, 데이터는 켜져있는 경우
            alarmModel.onOff = false

        } else if ((pendingIntent != null) and alarmModel.onOff.not()) {
            // 알람은 켜져있는데 데이터는 꺼져있는 경우.
            // 알람을 취소함
            pendingIntent.cancel()
        }
        return alarmModel
    }

    /* 알람 저장 */
    private fun saveAlarm() {
        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {

            var hour = edtHour.text.toString().toInt()
            var minute = edtMinute.text.toString().toInt()
            when(rgTime.checkedRadioButtonId){
                R.id.rbAm -> hour = hour
                R.id.rbPm -> hour = hour + 12
            }

            val model = saveAlarmModel(hour, minute, false)

            renderView(model)

            cancelAlarm()

        }
    }

    /* 알람 켜기 끄기 */
    private fun alarmOnoff() {
        val onOffButton = findViewById<Button>(R.id.btnOnoff)
        onOffButton.setOnClickListener {
            /* 저장한 데이터 확인 */
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener // 형변환 실패하는 경우에는 null
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not()) // on off 스위칭
            renderView(newModel)

            if (newModel.onOff) {
                /* on -> 알람 작동 */
                val calender = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1) // 지나간 시간의 경우 하루 더해서 다음 날 울리도록
                    }
                }

                /* 알람 매니저 가져오기 */
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    M_ALARM_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                ) // 있으면 새로 만든 걸로 업데이트

                /* 복습 루틴에 맞게 울리도록 변경 해야됨 */
                alarmManager.setInexactRepeating( // 정시에 반복
                    AlarmManager.RTC_WAKEUP, // RTC_WAKEUP : 실제 시간 기준으로 wakeup , ELAPSED_REALTIME_WAKEUP : 부팅 시간 기준으로 wakeup
                    calender.timeInMillis, // 언제 알람이 발동할지.
                    AlarmManager.INTERVAL_DAY, // 하루에 한번씩.
                    pendingIntent
                )
            } else {
                /* off -> 알람 제거 */
                cancelAlarm()
            }


        }
    }

    /* 최초 실행 또는 시간 재설정 시 */
    private fun renderView(model: AlarmDisplayModel) {

        if(model.timeHour.toInt() > 12){
            edtHour.setText("0"+(model.timeHour.toInt()-12).toString())
        }else{
            edtHour.setText(model.timeHour)
        }
        edtMinute.setText(model.timeMinute)
        when(model.ampmText){
            "AM" -> rbAm.isChecked = true
            "PM" -> rbPm.isChecked = true
        }

        findViewById<Button>(R.id.btnOnoff).apply {
            text = model.onOffText
            tag = model
        }
    }

    /* 알람 삭제 */
    private fun cancelAlarm() {

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            M_ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) // 있으면 가져오고 없으면 안만든다. (null)

        pendingIntent?.cancel() // 기존 알람 삭제
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        // time 에 대한 db 파일 생성
        val sharedPreferences = getSharedPreferences(M_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        // edit 모드로 열어서 작업 (값 저장)
        with(sharedPreferences.edit()) {
            putString(M_ALARM_KEY, model.makeDataForDB())
            putBoolean(M_ONOFF_KEY, model.onOff)
            commit()
        }

        return model
    }


    companion object {
        // static 영역 (상수 지정)
        private const val M_SHARED_PREFERENCE_NAME = "time"
        private const val M_ALARM_KEY = "alarm"
        private const val M_ONOFF_KEY = "onOff"
        private val M_ALARM_REQUEST_CODE = 1000
    }
}