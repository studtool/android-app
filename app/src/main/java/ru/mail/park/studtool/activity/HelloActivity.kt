package ru.mail.park.studtool.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import ru.mail.park.studtool.SignInActivity
import ru.mail.park.studtool.R

class HelloActivity : BaseActivity() {

    private lateinit var mBtnSignIn: Button
    private lateinit var mBtnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        mBtnSignIn = findViewById(R.id.btn_signIn)
        mBtnSignIn.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        mBtnSignUp = findViewById(R.id.btn_signUp)
        mBtnSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
