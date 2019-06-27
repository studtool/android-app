package ru.mail.park.studtool

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import ru.mail.park.studtool.activity.BaseActivity
import ru.mail.park.studtool.activity.HelloActivity
import ru.mail.park.studtool.api.DocumentsApiManager
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.dummy.DummyContent
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.NotFoundApiException
import ru.mail.park.studtool.exception.UnauthorizedException
import ru.mail.park.studtool.logic.Logic

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : BaseActivity() {

    private var mDocumentTaskGetDocumentsTask: Logic.GetDocumentsTask? = null
    private var mMessage: String = ""
    private var mStatusOK: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (mDocumentTaskGetDocumentsTask != null) {
            return
        }
        mDocumentTaskGetDocumentsTask = Logic.GetDocumentsTask(mDocumentTaskGetDocumentsTask, loadAuthInfo()!!, mStatusOK, mMessage)
        mDocumentTaskGetDocumentsTask!!.execute(null as Void?)

        val button = findViewById<Button>(R.id.button_exit)

        button.setOnClickListener {
            deleteAuthInfo()

            val intent = Intent(this, HelloActivity::class.java)
            startActivity(intent)
            finish()
        }

        val buttonTest = findViewById<Button>(R.id.button)

        buttonTest.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }

        val calendarButton = findViewById<Button>(R.id.calendar_button)

        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

//    fun openItems(view: View) {
//        val intent = Intent(this, ItemListActivity::class.java)
//        startActivity(intent)
//    }

}
