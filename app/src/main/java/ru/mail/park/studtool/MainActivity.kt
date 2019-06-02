package ru.mail.park.studtool

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ru.mail.park.studtool.activity.BaseActivity
import ru.mail.park.studtool.activity.HelloActivity
import ru.mail.park.studtool.api.AuthApiManager
import ru.mail.park.studtool.api.DocumentsApiManager
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.auth.Credentials
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.dummy.DummyContent
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.UnauthorizedException

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : BaseActivity() {

    private var mDocumentTaskGetDocumentsTask: MainActivity.GetDocumentsTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (mDocumentTaskGetDocumentsTask != null){
            return
        }
        mDocumentTaskGetDocumentsTask = GetDocumentsTask(loadAuthInfo()!!)
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

    }

    fun openItems(view: View) {
        val intent = Intent(this, ItemListActivity::class.java)
        startActivity(intent)
    }


    private inner class GetDocumentsTask(private val mAuthInfo: AuthInfo) : AsyncTask<Void, Void, Array<DocumentInfo> >() {

        override fun doInBackground(vararg params: Void): Array<DocumentInfo> {
            return try {
                DocumentsApiManager().getDocumentsList("subject", mAuthInfo)
            } catch (e: UnauthorizedException) {
                showErrorMessage(getString(R.string.msg_wrong_credentials))
                emptyArray<DocumentInfo>()
            } catch (e: InternalApiException) {
                showErrorMessage(getString(R.string.msg_internal_server_error))
                emptyArray<DocumentInfo>()
            } catch (e: InterruptedException) {
                emptyArray<DocumentInfo>()
            }
        }

        override fun onPostExecute(documentsInfo: Array<DocumentInfo>) {
            mDocumentTaskGetDocumentsTask = null

            if (documentsInfo != null) {

//                documentsInfo.copyInto(DOCUMENTS)

                DummyContent.DOCUMENTS = documentsInfo
//                showErrorMessage("Получено документов " + DummyContent.DOCUMENTS.size)
//                finish() //TODO show next activity
            }
        }

        override fun onCancelled() {
            super.onCancelled()
            mDocumentTaskGetDocumentsTask = null

            DummyContent.DOCUMENTS = emptyArray()

        }

    }
}
