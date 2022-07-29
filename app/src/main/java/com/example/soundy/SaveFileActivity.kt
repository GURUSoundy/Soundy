package com.example.soundy

import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import java.util.*

class SaveFileActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase

    lateinit var fileName : String
    lateinit var sttContent : String
    lateinit var routine : String

    lateinit var mWebView: WebView
    val IMAGE_SELECTOR_REQ = 1
    private var mFilePathCallback: ValueCallback<*>? = null

    lateinit var btnMypage : ImageButton
    lateinit var edtFileName : EditText
    lateinit var btnUpload : Button
    lateinit var btnStt : ImageView
    lateinit var tvSttContent : TextView
    lateinit var btnRoutine : Button
    lateinit var btnSave : Button
    lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_file)

        edtFileName = findViewById(R.id.fileName)
        btnUpload = findViewById(R.id.btnUpload)
        btnStt = findViewById(R.id.ivStt)
        tvSttContent = findViewById(R.id.tvSttContent)
        btnRoutine = findViewById(R.id.btnRoutine)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        mWebView = findViewById<WebView>(R.id.webview)
        setmWebViewFileUploadPossible()
        with(mWebView) { this.loadUrl("file:///android_asset/upload.html") }

        /* 업로드 버튼 클릭 시 */
        btnUpload.setOnClickListener {
            // 파일 목록이 팝업으로 떠서 사용자가 직접 고를 수 있게
            // 그리고 버튼 텍스트가 해당 파일명으로 바뀜
        }

        /* 인텐트로 stt 내용 받아와서 stt 텍스트뷰를 통해 일부 내용을 보여줌 */
        sttContent = "아직 없어요.."
        //val sttContent = intent.getStringExtra("sttContent")
        tvSttContent.setText(sttContent)

        /* 복습 루틴 버튼 클릭 시 */
        btnRoutine.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                routine = "${year}-${month+1}-${dayOfMonth}"
                Toast.makeText(this@SaveFileActivity, "${year}년 ${month+1}월 ${dayOfMonth}일로 설정되었습니다.", Toast.LENGTH_SHORT).show()
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        /* 저장 버튼 클릭 시 */
        btnSave.setOnClickListener {
            fileName = edtFileName.text.toString()

            var cursor: Cursor = sqliteDB.rawQuery("select * from File where fileName = '$fileName';", null)

            if(fileName != ""){
                if(cursor.count == 1){
                    Toast.makeText(this@SaveFileActivity, "이미 존재하는 파일입니다.", Toast.LENGTH_SHORT).show()
                }
                //saveFile(fileName, dirName, addFile, sttContent, routine)
                Toast.makeText(this@SaveFileActivity, "${fileName}이(가) 저장되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, FileListDetailActivity::class.java)
                startActivity(intent)
            } else{
                Toast.makeText(this@SaveFileActivity, "파일 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }

        }

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, mypage::class.java)
            startActivity(intent)
        }

        /* 뒤로 가기 버튼 클릭 시 */
        btnBack.setOnClickListener {
            val intent = Intent(this, FileListDetailActivity::class.java)
            startActivity(intent)
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