package ru.mail.park.studtool.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.ScrollView

import ru.mail.park.studtool.R
import ru.mail.park.studtool.api.AuthApiManager
import ru.mail.park.studtool.api.AuthInfo
import ru.mail.park.studtool.api.Credentials
import ru.mail.park.studtool.validator.CredentialsValidator

class SignInActivity : BaseActivity() {

    private var mAuthTask: UserSignInTask? = null

    private lateinit var mScvSignInForm: ScrollView
    private lateinit var mPbSignInProgress: ProgressBar

    private lateinit var mEtSignInEmail: EditText
    private lateinit var mEtSignInPassword: EditText

    private lateinit var mBtnPerformSignIn: Button
    private lateinit var mCredentialsValidator: CredentialsValidator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupActionBar()

        mScvSignInForm = findViewById(R.id.scv_SignInForm)
        mPbSignInProgress = findViewById(R.id.pb_SignInProgress)

        mEtSignInEmail = findViewById(R.id.et_SignInEmail)
        mEtSignInPassword = findViewById(R.id.et_SignInPassword)

        mBtnPerformSignIn = findViewById(R.id.btn_AttemptSignIn)
        mBtnPerformSignIn.setOnClickListener { attemptSignIn() }

        mCredentialsValidator = CredentialsValidator()
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun attemptSignIn() {
        if (mAuthTask != null) {
            return
        }

        mEtSignInEmail.error = null
        mEtSignInPassword.error = null

        val emailStr = mEtSignInEmail.text.toString()
        val passwordStr = mEtSignInPassword.text.toString()

        var cancel = false
        var focusView: View? = null

        var validationResult = mCredentialsValidator.ValidateEmail(emailStr)
        if (!validationResult.first) {
            mEtSignInEmail.error = validationResult.second
            focusView = mEtSignInEmail
            cancel = true
        }
        validationResult = mCredentialsValidator.ValidatePassword(passwordStr)
        if (!validationResult.first) {
            mEtSignInPassword.error = validationResult.second
            focusView = mEtSignInPassword
            cancel = true
        }

        if (cancel) {
            focusView!!.requestFocus()
        } else {
            showProgress(true)
            mAuthTask = UserSignInTask(Credentials(emailStr, passwordStr))
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        mScvSignInForm.visibility = if (show) View.GONE else View.VISIBLE
        mScvSignInForm.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 0 else 1).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mScvSignInForm.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

        mPbSignInProgress.visibility = if (show) View.VISIBLE else View.GONE
        mPbSignInProgress.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mPbSignInProgress.visibility = if (show) View.VISIBLE else View.GONE
                }
            })

    }

    private inner class UserSignInTask(private val mCredentials: Credentials) : AsyncTask<Void, Void, AuthInfo?>() {

        override fun doInBackground(vararg params: Void): AuthInfo? {
            return try {
                AuthApiManager().performSignIn(mCredentials)
            } catch (e: InterruptedException) { //TODO handle exceptions
                return null
            }
        }

        override fun onPostExecute(authInfo: AuthInfo?) {
            mAuthTask = null
            showProgress(false)

            if (authInfo != null) {
                finish() //TODO handle login
            } else {
                // TODO handle errors
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }
}
