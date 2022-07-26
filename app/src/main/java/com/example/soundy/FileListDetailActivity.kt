package com.example.soundy

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.icu.text.SimpleDateFormat
import android.media.MediaRecorder
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_file_list_detail.*
import kotlinx.android.synthetic.main.renamefile_popup.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.coroutines.*

const val REQUEST_CODE=200

class FileListDetailActivity : AppCompatActivity(),Timer.OnTimerTickListener, OnItemClickListener{

    /* 오디오 관련 변수 */
    private var permissions= arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false
    private lateinit var recorder: MediaRecorder
    private var dirPath = ""
    private var filename = "audioRecord"
    private var isRecording=false
    private var isPaused = false
    private var duration=""
    private var dirName=""

    private lateinit var vibrator: Vibrator
    private lateinit var timer: Timer

    private lateinit var db : recordDatabase

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var records : ArrayList<audioRecord>
    private lateinit var mAdapter: rvfileAdapter

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var btnBack: ImageButton
    lateinit var btnMypage: ImageButton
    lateinit var titleText: TextView
    lateinit var passedIntent: Intent
    lateinit var deadLineDate: TextView

    lateinit var filePath: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list_detail)

        titleText = findViewById<TextView>(R.id.titleText)

        /* FileListActivity에서 디렉토리 이름 받아오기 */
        passedIntent = intent
        dirName = passedIntent.getStringExtra("dirName").toString()
        titleText.text = dirName

        /* 녹음 기능 코드 */
        permissionGranted=ActivityCompat.checkSelfPermission(this,permissions[0])==PackageManager.PERMISSION_GRANTED

        if(!permissionGranted)
            ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE)

        /* 녹음파일 db 연결 */
        db=Room.databaseBuilder(
            this, recordDatabase::class.java,
            "audioRecords"
        ).build()

        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state=BottomSheetBehavior.STATE_COLLAPSED

        timer = Timer(this)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        btnRecord.setOnClickListener{
            when{
                isPaused->resumeRecorder()
                isRecording->pauseRecorder()

                else->startRecording()
            }
        }

        /* 녹음 종료 버튼 기능 구현 */
        btnDone.setOnClickListener{
            stopRecorder()
            bottomSheetBehavior.state =BottomSheetBehavior.STATE_EXPANDED
            popupBG.visibility=View.VISIBLE
            filenameInput.setText(filename)
        }

        /* 파일취소 버튼 기능 구현 */
        btnCancel.setOnClickListener{
            File("$dirPath$filename.mp3").delete()
            dismiss()
        }

        /* 파일제목설정 popup 확인버튼 기능 구현 */
        btnOK.setOnClickListener{
            dismiss()
            save()

            var newDBManager = DBManager(this, "File", null, 1)
            var newSqliteDB : SQLiteDatabase = newDBManager.writableDatabase

            newSqliteDB.execSQL("INSERT INTO File (fileName, dirName, memo) VALUES ('${filenameInput.text.toString()}', '$dirName', '');")

            newSqliteDB.close()
            newDBManager.close()

            val intent = Intent(this, SaveFileActivity::class.java)
            intent.putExtra("fileName", filenameInput.text.toString())
            intent.putExtra("dirName", dirName)
            intent.putExtra("filePath", filePath)
            startActivity(intent)
            finish()
        }
        popupBG.setOnClickListener{
            File("$dirPath$filename.mp3").delete()
            dismiss()
        }

        /* 파일삭제 버튼 기능 구현 */
        btnDelete.setOnClickListener{
            stopRecorder()
            File("$dirPath$filename.mp3").delete()
            Toast.makeText(this, "녹음이 삭제되었습니다.",Toast.LENGTH_SHORT).show()
        }
        btnDelete.isClickable=false

        btnBack = findViewById(R.id.btnBack)
        deadLineDate = findViewById(R.id.deadLineDate)
        var dateString = ""

        /* FileListActivity에서 디렉토리 이름 받아오기 */
        passedIntent = intent
        dirName = passedIntent.getStringExtra("dirName").toString()
        titleText.text = dirName

        /* 디렉토리의 복습 마감 기한 받아오기 */
        dbManager = DBManager(this, "Directory", null, 1)
        sqliteDB = dbManager.readableDatabase
        var cursor1: Cursor = sqliteDB.rawQuery("select * from Directory where dirName = '$dirName';", null)

        while (cursor1.moveToNext()) {
            var endDate: String = cursor1.getString(1)
            deadLineDate.text = endDate
        }

        sqliteDB.close()
        dbManager.close()

        /* 날짜 클릭 시 복습 마감 기한 수정 가능 */
        deadLineDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}/${month+1}/${dayOfMonth}"

                dbManager = DBManager(this, "Directory", null, 1)
                sqliteDB = dbManager.writableDatabase
                sqliteDB.execSQL("UPDATE Directory SET endDate = '$dateString' where dirName = '$dirName';")

                sqliteDB.close()
                dbManager.close()

                /* 날짜 수정 후 새로고침 */
                val intent = getIntent()
                finish();
                startActivity(intent)
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }
        /* 뒤로가기 기능 */
        btnBack.setOnClickListener {
            finish()
        }
        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        /* rvfile(녹음파일) 리사이클러뷰 목록 관련 */
        records = ArrayList()

        db=Room.databaseBuilder(
            this,
            recordDatabase::class.java,
            "audioRecords"
        ).build()

        mAdapter = rvfileAdapter(records,this)

        rvFile.apply{
            adapter=mAdapter
            layoutManager=LinearLayoutManager(context)
        }
        fetchAll(dirName)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode ==REQUEST_CODE)
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    /*녹음 중지*/
    private fun pauseRecorder(){
        recorder.pause()
        isPaused=true
        btnRecord.setImageResource(R.drawable.ic_record)

        timer.pause()
    }

    /* 녹음 다시 시작 */
    private fun resumeRecorder(){
        recorder.resume()
        isPaused=false
        btnRecord.setImageResource(R.drawable.ic_pause)

        timer.start()
    }

    private fun startRecording(){
        if(!permissionGranted){
            ActivityCompat.requestPermissions(this,permissions, REQUEST_CODE)
            return
        }
        /* 녹음 시작 */
        recorder = MediaRecorder()
        dirPath="${externalCacheDir?.absolutePath}/"

        var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
        //var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD")
        var date=simpleDateFormat.format(Date())
        filename="audioRecord"
        //filename="audioRecord"

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$dirPath$filename.mp3")

            try {
                prepare()
            } catch (e: IOException){}

            start()
        }

        btnRecord.setImageResource(R.drawable.ic_pause)
        isRecording=true
        isPaused=false

        timer.start()

        btnDelete.isClickable=true
        btnDelete.setImageResource(R.drawable.ic_delete)

        /* 녹음 완료 시 btnList->btnDone */
        btnList.visibility=View.GONE
        btnDone.visibility=View.VISIBLE
    }

    private fun stopRecorder(){
        timer.stop()

        recorder.apply {
            stop()
            reset()
            release()
        }
        isPaused=false
        isRecording=false

        btnList.visibility= View.VISIBLE
        btnDone.visibility= View.GONE
        btnDelete.isClickable= false

        btnDelete.setImageResource(R.drawable.ic_delete)
        btnRecord.setImageResource(R.drawable.ic_record)

        tvTimer.text = "00:00.00"
    }

    private fun save(){
        val newFilename =filenameInput.text.toString()
        /* 새로운 파일 이름 생성 */
        if (newFilename != filename){
            var newFile =File("$dirPath$newFilename.mp3")
            File("$dirPath$filename.mp3").renameTo(newFile)
        }

        filePath = "$dirPath$newFilename.mp3"
        Log.d("디버깅", "디테일페이지 filePath: $filePath")
        var timestamp = Date().time
        var ampsPath = "$dirPath$newFilename"

        try {
            var fos=FileOutputStream(ampsPath)
            fos.close()
        }catch (e:IOException){}

        var record =audioRecord(newFilename,filePath,timestamp,duration,ampsPath, dirName)

        GlobalScope.launch{
            db.audioRecordDao().insert(record)
        }

        /* 녹음파일 저장 후 액티비티 새로고침 */
        val intent = intent
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)

    }

    private fun dismiss(){
        popupBG.visibility=View.GONE
        hideKeyboard(filenameInput)
        Handler(Looper.getMainLooper()).postDelayed({
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }, 100)
    }

    /* 파일이름 설정 시 키보드 */
    private fun hideKeyboard(view: View){
        val imm =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /* rvfile(녹음파일) 리사이클러뷰 목록 관련 */
    private fun fetchAll(dirName: String){
        GlobalScope.launch {
            records.clear()
            var queryResult = db.audioRecordDao().getAll(dirName)
            records.addAll(queryResult)

            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onTimerTick(duration: String) {
        tvTimer.text=duration
        this.duration=duration.dropLast(3)
    }

    override fun onItemClickListener(position: Int) {
        var audioRecord = records[position]
        val intent = Intent(this, ShowFileActivity::class.java)
        intent.putExtra("fileName", audioRecord.filename)
        intent.putExtra("dirName", dirName)
        intent.putExtra("filePath", audioRecord.filePath)
        Toast.makeText(this, "$dirName ${audioRecord.filename}",Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    override fun onItemLongClickListener(position: Int) {
        Toast.makeText(this, "Long click",Toast.LENGTH_SHORT).show()
    }
}