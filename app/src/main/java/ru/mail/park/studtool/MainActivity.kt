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
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.UnauthorizedException

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : BaseActivity() {

    private var mDocumentTask: GetDocumentsTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button_exit)

        button.setOnClickListener {
            deleteAuthInfo()

            val intent = Intent(this, HelloActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun sendMessage(view: View) {
        if (mDocumentTask != null){
            return
        }
        val editText = findViewById<EditText>(R.id.editText)
        val message = editText.text.toString()
        mDocumentTask = GetDocumentsTask(DocumentInfo(title = message, subject = "lol"), loadAuthInfo()!!)
        mDocumentTask!!.execute(null as Void?)
    }

    fun openItems(view: View) {
        val intent = Intent(this, ItemListActivity::class.java)
        startActivity(intent)
    }

//    fun withEditText(view: View) {
//        val builder = AlertDialog.Builder(this)
//        val inflater = layoutInflater
//        builder.setTitle("With EditText")
//        val dialogLayout = inflater.inflate(R.layout.dialog_new_document, null)
//        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
//        builder.setView(dialogLayout)
//        builder.setPositiveButton("OK") { dialogInterface, i -> Toast.makeText(applicationContext, "EditText is " + editText.text.toString(), Toast.LENGTH_SHORT).show() }
//        builder.show()
//    }


    private inner class GetDocumentsTask(private val mDocumentInfo: DocumentInfo, private val mAuthInfo: AuthInfo) : AsyncTask<Void, Void, DocumentInfo?>() {

        override fun doInBackground(vararg params: Void): DocumentInfo? {
            return try {
//                DocumentsApiManager().addDocument(mDocumentInfo, mAuthInfo)
                DocumentsApiManager().getDocumentsList("lol", loadAuthInfo()!!)[0]
            } catch (e: UnauthorizedException) {
                showErrorMessage(getString(R.string.msg_wrong_credentials))
                null
            } catch (e: InternalApiException) {
                showErrorMessage(getString(R.string.msg_internal_server_error))
                null
            } catch (e: InterruptedException) {
                null
            }
        }

        override fun onPostExecute(documentInfo: DocumentInfo?) {
            mDocumentTask = null

            if (documentInfo != null) {


                showErrorMessage("Документ создан " + documentInfo.title)
//                finish() //TODO show next activity
            }
        }
    }
}
