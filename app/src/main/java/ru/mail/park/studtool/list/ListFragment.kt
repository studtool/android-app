package ru.mail.park.studtool.list

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_list_content.view.*
import ru.mail.park.studtool.*

import ru.mail.park.studtool.document.DocumentInfo

import java.util.ArrayList

class ListFragment : Fragment() {

//    lateinit var tmP_VAR: DocumentInfo
//    private lateinit var tmP_VAR: DocumentInfo

    lateinit var tmP_VAR: Array<DocumentInfo>


    private lateinit var rv_list: Array<DocumentInfo>
    private var recyclerView: RecyclerView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        recyclerView = view.findViewById(R.id.home_rv)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)

        rv_list = tmP_VAR

        val mAdapter = ListRecyclerAdapter(rv_list)

        recyclerView!!.adapter = mAdapter

        recyclerView!!.itemAnimator = DefaultItemAnimator()

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getParcelableArray("THIS_LIST")?.let {
            tmP_VAR = it as Array<DocumentInfo>
        }
    }


    companion object {

        fun newInstance(documents: Array<DocumentInfo>?):ListFragment{
            val listFragment = ListFragment()
//            listFragment.arguments?.putParcelableArrayList("THIS_LIST", documents)
            listFragment.tmP_VAR = documents as Array<DocumentInfo>
            return listFragment
        }
    }
}// Required empty public constructor