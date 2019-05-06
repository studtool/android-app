package ru.mail.park.studtool.activity

import android.os.Bundle
import android.content.Intent

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, HelloActivity::class.java))
        finish()
    }
}
