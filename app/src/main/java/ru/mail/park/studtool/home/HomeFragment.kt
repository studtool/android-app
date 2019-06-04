package ru.mail.park.studtool.home

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_home.*
import ru.mail.park.studtool.NavigationActivity

import ru.mail.park.studtool.R
import ru.mail.park.studtool.api.DocumentsApiManager
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.UnauthorizedException

import java.util.ArrayList

class HomeFragment : Fragment() {

    lateinit var testPass : String
    private var rv_list: MutableList<HomeItem>? = null
    private var recyclerView: RecyclerView? = null

//    private var mDocumentTask: NewDocumentTask? = null


//    fun withEditText(view: View) {
//        val builder = AlertDialog.Builder(this)
//        val inflater = layoutInflater
//        builder.setTitle(R.string.create_file_title)
//        val dialogLayout = inflater.inflate(R.layout.dialog_new_document, null)
//        val editText = dialogLayout.findViewById<EditText>(R.id.editText)
//        builder.setView(dialogLayout)
//        builder.setPositiveButton(R.string.create_new_document_button) { dialogInterface, i ->
//            val message = editText.text.toString()
//            if (mDocumentTask == null) {
////                mDocumentTask = NewDocumentTask(DocumentInfo(title = message, subject = "subject"), loadAuthInfo()!!)
////                mDocumentTask!!.execute(null as Void?)
////                Thread.sleep(1_000)
//
//            } else {
//                return@setPositiveButton
//            }
//
//        }
//        builder.show()
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//        fab_files.setOnClickListener { view ->
//            withEditText(view)
//        }

        recyclerView = view.findViewById(R.id.home_rv)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)

        rv_list = ArrayList<HomeItem>() as MutableList<HomeItem>?
        rv_list!!.add(HomeItem("Home", R.drawable.ic_home_black_24dp))
        rv_list!!.add(HomeItem("Dashboard", R.drawable.ic_dashboard_black_24dp))
        rv_list!!.add(HomeItem("Notification", R.drawable.ic_notifications_black_24dp))
        rv_list!!.add(HomeItem("image", R.drawable.ic_menu_gallery))
        rv_list!!.add(HomeItem("Music video", R.drawable.ic_menu_camera))
        rv_list!!.add(HomeItem("Settings", R.drawable.ic_menu_send))


        val mAdapter = HomeRecyclerAdapter(rv_list as ArrayList<HomeItem>)

        recyclerView!!.adapter = mAdapter

        recyclerView!!.itemAnimator = DefaultItemAnimator()

        return view
    }


//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        arguments?.getString("AUTH_INFO")?.let {
//            testPass = it
//        }
//    }


//    private inner class NewDocumentTask(private val mDocumentInfo: DocumentInfo, private val mAuthInfo: AuthInfo) :
//        AsyncTask<Void, Void, DocumentInfo?>() {
//
//        override fun doInBackground(vararg params: Void): DocumentInfo? {
//            return try {
//                DocumentsApiManager().addDocument(mDocumentInfo, mAuthInfo)
//            } catch (e: UnauthorizedException) {
////                showErrorMessage(getString(R.string.msg_wrong_credentials))
//                null
//            } catch (e: InternalApiException) {
////                showErrorMessage(getString(R.string.msg_internal_server_error))
//                null
//            } catch (e: InterruptedException) {
//                null
//            }
//        }
//
//        override fun onPostExecute(documentInfo: DocumentInfo?) {
//            mDocumentTask = null
//            if (documentInfo != null) {
////                showErrorMessage("Создан: " + mDocumentInfo.title)
//            }
//        }
//    }

}// Required empty public constructor