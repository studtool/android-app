package ru.mail.park.studtool

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import ru.mail.park.studtool.dummy.DummyContent
import android.R.string.cancel
import android.content.DialogInterface
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.os.AsyncTask
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.coroutines.awaitAll
import ru.mail.park.studtool.activity.BaseActivity
import ru.mail.park.studtool.api.DocumentsApiManager
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.UnauthorizedException
import java.util.ArrayList


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : BaseActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private var mDocumentTask: ItemListActivity.NewDocumentTask? = null
    private var mDocumentTaskGetDocumentsTask: ItemListActivity.GetDocumentsTask? = null
//    private var mDocumentTaskGetDocumentDetailsTask: ItemListActivity.GetDocumentDetails? = null


    fun withEditText(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle(R.string.create_file_title)
        val dialogLayout = inflater.inflate(R.layout.dialog_new_document, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.create_new_document_button) {
                dialogInterface, i ->
            val message = editText.text.toString()
            if (mDocumentTask == null){
                mDocumentTask = NewDocumentTask(DocumentInfo(title = message, subject = "lol"), loadAuthInfo()!!)
                mDocumentTask!!.execute(null as Void?)
            }
            if (mDocumentTaskGetDocumentsTask == null){
                mDocumentTaskGetDocumentsTask = GetDocumentsTask(loadAuthInfo()!!)
                mDocumentTaskGetDocumentsTask!!.execute(null as Void?)

                val item = DummyContent.DOCUMENTS.last()
                showErrorMessage("Всего документов " + DummyContent.DOCUMENTS.size)
                finish();
                startActivity(getIntent());
            }

        }
        builder.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mDocumentTaskGetDocumentsTask != null){
            return
        }
        mDocumentTaskGetDocumentsTask = GetDocumentsTask(loadAuthInfo()!!)
        mDocumentTaskGetDocumentsTask!!.execute(null as Void?)

//        TODO REMOVE
//        if (mDocumentTaskGetDocumentDetailsTask != null){
//            return
//        }
//        mDocumentTaskGetDocumentDetailsTask = GetDocumentDetails(DummyContent.DOCUMENTS.last().documentId)
//        mDocumentTaskGetDocumentDetailsTask!!.execute(null as Void?)

        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            withEditText(view)
        }
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(item_list)


    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(loadAuthInfo()!!,this, DummyContent.DOCUMENTS, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val authInfo: AuthInfo,
        private val parentActivity: ItemListActivity,
        private val values: Array<DocumentInfo>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DocumentInfo

                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_TITLE, item.title)
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.documentId)
                            putString(ItemDetailFragment.ARG_AUTH, authInfo.authToken)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_TITLE, item.title)
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.documentId)
                        putExtra(ItemDetailFragment.ARG_AUTH, authInfo.authToken)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = (position + 1).toString() //item.documentId
            holder.contentView.text = item.title

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }

    private inner class NewDocumentTask(private val mDocumentInfo: DocumentInfo, private val mAuthInfo: AuthInfo) : AsyncTask<Void, Void, DocumentInfo?>() {

        override fun doInBackground(vararg params: Void): DocumentInfo? {
            return try {
                DocumentsApiManager().addDocument(mDocumentInfo, mAuthInfo)
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

//                showErrorMessage("Создан: " + mDocumentInfo.title)
//                finish() //TODO show next activity
            }
        }
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

    }



//    private inner class GetDocumentDetails(private val documentId: String) : AsyncTask<Void, Void, String>() {
//
//        override fun doInBackground(vararg params: Void): String {
//            return try {
//                DocumentsApiManager().getDocumentsDetails(documentId, loadAuthInfo()!!)
//            } catch (e: UnauthorizedException) {
//                return ""
//            } catch (e: InternalApiException) {
//                return ""
//            } catch (e: InterruptedException) {
//                return ""
//            }
//        }
//
//        fun onPostExecute(documentsInfo: Array<DocumentInfo>) {
//            mDocumentTaskGetDocumentDetailsTask = null
//
//            if (documentsInfo != null) {
//
////                documentsInfo.copyInto(DOCUMENTS)
//
////                DummyContent.DOCUMENTS = documentsInfo
////                showErrorMessage("Получено документов " + DummyContent.DOCUMENTS.size)
////                finish() //TODO show next activity
//            }
//        }
//
//    }

}
