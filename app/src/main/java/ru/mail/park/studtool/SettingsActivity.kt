package ru.mail.park.studtool

import ru.mail.park.studtool.R
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import ru.mail.park.studtool.activity.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

    }
}