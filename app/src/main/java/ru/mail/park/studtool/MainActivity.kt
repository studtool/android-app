package ru.mail.park.studtool

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import ru.mail.park.studtool.activity.BaseActivity
import ru.mail.park.studtool.activity.HelloActivity

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button_exit)

        button.setOnClickListener{
            deleteAuthInfo()

            val intent = Intent(this, HelloActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    fun openItems(view: View) {
        val intent = Intent(this, ItemListActivity::class.java)
        startActivity(intent)
    }
}
