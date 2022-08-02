package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_save_file.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SaveFileActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var routineDBManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var routineSqliteDB: SQLiteDatabase

    lateinit var dirName : String

    lateinit var mWebView: WebView
    val IMAGE_SELECTOR_REQ = 1
    private var mFilePathCallback: ValueCallback<*>? = null

    lateinit var btnMypage : ImageButton
    lateinit var fileName : TextView
    lateinit var btnUpload : Button
    lateinit var btnMemo : ImageView
    lateinit var tvMemoContent : TextView
    lateinit var btnSave : Button
    lateinit var btnBack : ImageButton
    lateinit var filePath: String

    lateinit var btnmark : ImageButton // 망각

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_file)

        fileName = findViewById(R.id.fileName)
        btnUpload = findViewById(R.id.btnUpload)
        btnMemo = findViewById(R.id.ivMemo)
        tvMemoContent = findViewById(R.id.tvMemoContent)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

        btnmark=findViewById(R.id.btn_mark) // 망각

        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        routineDBManager = DBManager(this, "TodoList", null, 1)
        routineSqliteDB = routineDBManager.writableDatabase

        /* 파일 업로드 클릭 시 */
        mWebView = findViewById<WebView>(R.id.webview)
        setmWebViewFileUploadPossible()
        with(mWebView) { this.loadUrl("file:///android_asset/upload.html") }

        var passedIntent : Intent = intent
        dirName = passedIntent.getStringExtra("dirName").toString()
        fileName.setText(passedIntent.getStringExtra("fileName"))
        filePath = passedIntent.getStringExtra("filePath").toString()

        /*btn_mark 클릭 시 망각곡선 이미지 뜸*/
        btn_mark.setOnClickListener {
            val img = findViewById<View>(R.id.img_curve) as ImageView

            if (img.visibility == View.GONE) {
                img.visibility = View.VISIBLE
            } else {
                img.visibility = View.GONE
            }
        }

        /* 메모 부분 클릭 시 */
        tvMemoContent.setOnClickListener {
            val intent = Intent(this, MemoActivity::class.java)
            intent.putExtra("fileName", fileName.text)
            intent.putExtra("dirName", dirName)
            intent.putExtra("filePath", filePath)
            startActivity(intent)
        }

        /* 저장 버튼 클릭 시 */
        btnSave.setOnClickListener {

            /* 복습 루틴 설정하여 투두리스트 DB에 추가 */
            /* 복습 마감 기한 처리 필요 */
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

            var today = LocalDateTime.now()
            var oneDayLater = today.plusDays(1)
            var sevenDaysLater = today.plusDays(7)
            var oneMonthLater = today.plusMonths(1)

            var strOneDayLater: String = oneDayLater.format(formatter)
            var strSevenDaysLater: String = sevenDaysLater.format(formatter)
            var strOneMonthLater: String = oneMonthLater.format(formatter)

            routineSqliteDB.execSQL("INSERT INTO TodoList VALUES('$strOneDayLater', '[$dirName - ${fileName.text}] 1차 복습', 0);")
            routineSqliteDB.execSQL("INSERT INTO TodoList VALUES('$strSevenDaysLater', '[$dirName - ${fileName.text}] 2차 복습', 0);")
            routineSqliteDB.execSQL("INSERT INTO TodoList VALUES('$strOneMonthLater', '[$dirName - ${fileName.text}] 3차 복습', 0);")

            var cursor: Cursor = sqliteDB.rawQuery("select * from File where fileName = '$fileName';", null)

            if(cursor.count == 1){
                Toast.makeText(this@SaveFileActivity, "이미 존재하는 파일입니다.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@SaveFileActivity, "${fileName.text},$dirName 이(가) 저장되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, FileListDetailActivity::class.java)
                intent.putExtra("fileName", fileName.text)
                intent.putExtra("dirName", dirName)
                intent.putExtra("filePath", filePath)
                finish()
            }
        }

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        /* 뒤로 가기 버튼 클릭 시 */
        btnBack.setOnClickListener {
            finish()
        }
    }

    protected fun setmWebViewFileUploadPossible() {
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: android.webkit.ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                mFilePathCallback = filePathCallback
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Select picture"),
                    IMAGE_SELECTOR_REQ
                )
                return true
            }
        }
    }
}