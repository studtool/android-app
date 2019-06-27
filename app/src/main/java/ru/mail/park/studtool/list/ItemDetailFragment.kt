package ru.mail.park.studtool.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_item_detail.*
import ru.mail.park.studtool.R
import ru.mail.park.studtool.document.DocumentInfo

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: DocumentInfo? = null
    private var auth: String? = null
    private var title: String = ""
    //    private var mDocumentTaskGetDocumentDetailsTask: ItemDetailFragment.GetDocumentDetails? = null
//    private var mDocumentTaskPatchDocumentDetailsTask: ItemDetailFragment.PatchDocumentDetails? = null
    var documentData: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            if (
                it.containsKey(ARG_AUTH)
                && it.containsKey(ARG_ITEM_ID)
                && it.containsKey(ARG_ITEM_TITLE)
            ) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = DocumentInfo(documentId = it.getString(ARG_ITEM_ID), title = it.getString(ARG_ITEM_TITLE))
                auth = it.getString(ARG_AUTH)
                title = it.getString(ARG_ITEM_TITLE)
//                activity?.toolbar_layout?.title = item?.title
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_document_detail, container, false)


        // Show the dummy content as text in a TextView.
        item?.let {
            val documentId = it.documentId

            auth?.let {

            }

//            rootView.item_detail.text = Editable.Factory.getInstance().newEditable(documentData)
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_TITLE = "item_title"
        const val ARG_ITEM_ID = "item_id"
        const val ARG_AUTH = "auth"
        fun lol(): String {

            return "lol"
        }
    }


}
