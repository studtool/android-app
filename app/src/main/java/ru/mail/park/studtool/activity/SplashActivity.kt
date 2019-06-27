package ru.mail.park.studtool.activity

import android.content.Intent
import android.os.Bundle
import ru.mail.park.studtool.MainActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (loadAuthInfo() != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, HelloActivity::class.java))
            finish()
        }
    }
}
