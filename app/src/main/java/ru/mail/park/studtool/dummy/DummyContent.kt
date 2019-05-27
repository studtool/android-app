package ru.mail.park.studtool.dummy

import android.os.AsyncTask
import android.widget.EditText
import ru.mail.park.studtool.MainActivity
import ru.mail.park.studtool.R
import ru.mail.park.studtool.activity.BaseActivity
import ru.mail.park.studtool.api.DocumentsApiManager
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.UnauthorizedException
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent : BaseActivity() {
    /**
     * An array of sample (dummy) items.
     */
    private var mDocumentTaskGetDocumentsTask: DummyContent.GetDocumentsTask? = null

    var DOCUMENTS: Array<DocumentInfo> = emptyArray()

    var ITEMS: MutableList<DummyItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, DummyItem> = HashMap()

    private val COUNT = 50

    init {
        // Add some sample items.


        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }

//        if (mDocumentTask == null){
//            mDocumentTask = GetDocumentsTask(loadAuthInfo()!!)
//            mDocumentTask!!.execute(null as Void?)
//        }
//        if (mDocumentTaskGetDocumentsTask == null){
//
//        }
//        mDocumentTaskGetDocumentsTask = GetDocumentsTask(loadAuthInfo()!!)
//        mDocumentTaskGetDocumentsTask!!.execute(null as Void?)
    }

    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): DummyItem {
        return DummyItem(position.toString(), "Item " + position, makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }

    private class GetDocumentsTask(private val mAuthInfo: AuthInfo) : AsyncTask<Void, Void, Array<DocumentInfo> >() {

        override fun doInBackground(vararg params: Void): Array<DocumentInfo> {
            return try {
                DocumentsApiManager().getDocumentsList("lol", mAuthInfo)
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

                DOCUMENTS = documentsInfo
                showErrorMessage("Получено документов: " + DOCUMENTS.size)
//                finish() //TODO show next activity
            }
        }

    }
}
