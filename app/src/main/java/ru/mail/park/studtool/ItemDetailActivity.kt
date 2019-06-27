package ru.mail.park.studtool

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.fragment_document_detail.*
import ru.mail.park.studtool.activity.BaseActivity
import ru.mail.park.studtool.api.DocumentsApiManager
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.UnauthorizedException
import ru.mail.park.studtool.list.ItemDetailFragment
import ru.mail.park.studtool.logic.Logic

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */
class ItemDetailActivity : BaseActivity() {

//    private var mDocumentTaskGetDocumentsTask: ItemDetailActivity.GetDocumentDetails? = null
//
//    mDocumentTaskGetDocumentsTask = GetDocumentDetails( it.documentId, )
//    mDocumentTaskGetDocumentsTask!!.execute(null as Void?)

    private var mDocumentTaskGetDocumentDetailsTask: Logic.GetDocumentDetailsTask? = null
    private var mDocumentTaskPatchDocumentDetailsTask: Logic.PatchDocumentDetailsTask? = null
    var documentData: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_document_detail)
        setSupportActionBar(detail_toolbar)


//        fab.setOnClickListener { view ->
//            //            val inflater = layoutInflater
////            val itemLayout = inflater.inflate(R.layout.item_detail, null)
////            val editText  = itemLayout.findViewById<EditText>(R.id.item_detail)
////            val message = editText.text.toString()
//
//
//            val editText = findViewById<EditText>(R.id.item_detail)
//            val message = editText.text.toString()
//
//            if (mDocumentTaskPatchDocumentDetailsTask == null) {
//                mDocumentTaskPatchDocumentDetailsTask = Logic.PatchDocumentDetailsTask(
//                    mDocumentTaskPatchDocumentDetailsTask,
//                    intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID),
//                    message,
//                    loadAuthInfo()!!.authToken
////                    intent.getStringExtra(ItemDetailFragment.ARG_AUTH)
//                )
//                mDocumentTaskPatchDocumentDetailsTask!!.execute(null as Void?)
//            }
//
////            Snackbar.make(view, "data" + message, Snackbar.LENGTH_LONG)
////                .setAction("Action", null).
////                    show()
//        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            val fragment = ItemDetailFragment().apply {
//                arguments = Bundle().apply {
//                    putString(
//                        ItemDetailFragment.ARG_ITEM_ID,
//                        intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID)
//                    )
//                    putString(
//                        ItemDetailFragment.ARG_ITEM_TITLE,
//                        intent.getStringExtra(ItemDetailFragment.ARG_ITEM_TITLE)
//                    )
//                }
//            }
//
//            supportFragmentManager.beginTransaction()
//                .add(R.id.item_detail_container, fragment)
//                .commit()
//        }


        val editText = findViewById<EditText>(R.id.item_detail)

        if (mDocumentTaskGetDocumentDetailsTask == null) {
            mDocumentTaskGetDocumentDetailsTask = Logic.GetDocumentDetailsTask(
                mDocumentTaskGetDocumentDetailsTask,
                intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID),
//                intent.getStringExtra(ItemDetailFragment.ARG_AUTH),
                loadAuthInfo()!!.authToken,
                documentData,
                editText
            )
            mDocumentTaskGetDocumentDetailsTask!!.execute(null as Void?)
        }

        // TODO добавить передачу параметров
//        this.toolbar_layout?.title = intent.getStringExtra(ItemDetailFragment.ARG_ITEM_TITLE)


//        editText.text = Editable.Factory.getInstance().newEditable(documentData)


    }


    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                // TODO(Save file here)

                navigateUpTo(Intent(this, NavigationActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    private inner class GetDocumentDetails(
        private val documentId: String,
        private val authToken: String,
        private val editText: EditText
    ) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String {
            return try {
                String( DocumentsApiManager().getDocumentContent(documentId, AuthInfo(authToken=authToken)))
            } catch (e: UnauthorizedException) {
                return ""
            } catch (e: InternalApiException) {
                return ""
            } catch (e: InterruptedException) {
                return ""
            }
        }


        override fun onPostExecute(result: String?) {
            mDocumentTaskGetDocumentDetailsTask = null

            if (result != null) {

//                documentsInfo.copyInto(DOCUMENTS)

                documentData = result
                editText.text = Editable.Factory.getInstance().newEditable(documentData)

//                showErrorMessage("Получено документов " + result)
//                finish() //TODO show next activity
            }
        }
    }


    inner class PatchDocumentDetails(
        private val documentId: String,
        private val data: String,
        private val authToken: String
    ) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String {
            return try {
                DocumentsApiManager().setDocumentContent(
                    documentId,
                    data.toByteArray(),
                    AuthInfo(authToken=authToken)
                ).toString()
            } catch (e: UnauthorizedException) {
                return ""
            } catch (e: InternalApiException) {
                return ""
            } catch (e: InterruptedException) {
                return ""
            }
        }


        override fun onPostExecute(result: String?) {
            mDocumentTaskPatchDocumentDetailsTask = null

            if (result != null) {

//                documentsInfo.copyInto(DOCUMENTS)

//                documentData = result
//                rootView.item_detail.text = Editable.Factory.getInstance().newEditable(documentData)

                showErrorMessage("Изменения сохранены")
//                finish() //TODO show next activity
            }
        }
    }

}
