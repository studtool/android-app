package ru.mail.park.studtool


import ru.mail.park.studtool.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    lateinit var countTv: TextView
    lateinit var countBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        countTv = view.findViewById(R.id.count_tv)
        countTv.text = "0"
        countBtn = view.findViewById(R.id.count_btn)
        countBtn.setOnClickListener { increaseCount() }
        return view
    }

    private fun increaseCount() {
        val current = Integer.parseInt(countTv.text as String)
        countTv.text = (current + 1).toString()

    }

}// Required empty public constructor