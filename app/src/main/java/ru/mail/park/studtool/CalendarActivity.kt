package ru.mail.park.studtool

import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import ru.mail.park.studtool.activity.BaseActivity
import ru.mail.park.studtool.api.DocumentsApiManager
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.NotFoundApiException
import ru.mail.park.studtool.exception.UnauthorizedException
import ru.mail.park.studtool.logic.Logic

class CalendarActivity : BaseActivity() {

    var mCalendarHashMap: HashMap<String?, String?> = HashMap()

    var message:String = ""
    var date: String = "8/6/2019"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)



        val calendarView = findViewById<CalendarView>(R.id.calendarView_2)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            var editText = findViewById<EditText>(R.id.calenderEventsText_2)
            date = "" + dayOfMonth + "/" + (month + 1) + "/" + year
            var msg = getEvent(date)
            editText.text = Editable.Factory.getInstance().newEditable(msg)
            message = editText.text.toString()
//            Toast.makeText(this@CalendarActivity, msg, Toast.LENGTH_SHORT).show()
        }

        val calendarButton = findViewById<Button>(R.id.calendarEventSaveButton_2)

        calendarButton.setOnClickListener {
            var editText = findViewById<EditText>(R.id.calenderEventsText_2)
            message = editText.text.toString()
            handleSave(date, message)
//            if (mDocumentTask == null) {
//                mDocumentTask = Logic.NewDocumentTask(mDocumentTask,DocumentInfo(title = message, subject = "calendar"), loadAuthInfo()!!)
//                mDocumentTask!!.execute(null as Void?)
//            }
            Toast.makeText(this@CalendarActivity, "Задание добавлено", Toast.LENGTH_SHORT).show()
        }
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
}
