package ru.mail.park.studtool


import ru.mail.park.studtool.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    var mCalendarHashMap: HashMap<String?, String?> = HashMap()

    var message:String = ""
    var date: String = "8/6/2019"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{

        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)


        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val editText = view.findViewById<EditText>(R.id.calenderEventsText)
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            date = "" + dayOfMonth + "/" + (month + 1) + "/" + year
            var msg = getEvent(date)
            editText.text = Editable.Factory.getInstance().newEditable(msg)
            message = editText.text.toString()

        }

        val calendarButton = view.findViewById<Button>(R.id.calendarEventSaveButton)

        calendarButton.setOnClickListener {
            var editText = view.findViewById<EditText>(R.id.calenderEventsText)
            message = editText.text.toString()
            handleSave(date, message)
        }

        return view
    }

    fun handleSave(date: String, message: String) {
        if (mCalendarHashMap.containsKey(date)){
            mCalendarHashMap[date] = message
        } else {
            mCalendarHashMap.put(date, message)
        }
    }

    fun getEvent(date: String):String{
        if (mCalendarHashMap.containsKey(date)){
            return mCalendarHashMap[date]!!
        } else {
            return ""
        }
    }

} // Required empty public constructor