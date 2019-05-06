package ru.mail.park.studtool.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import ru.mail.park.studtool.MainActivity
import ru.mail.park.studtool.R
import ru.mail.park.studtool.api.AuthApiManager
import ru.mail.park.studtool.api.Credentials
import ru.mail.park.studtool.exception.ConflictApiException
import ru.mail.park.studtool.exception.InternalApiException

class SignUpActivity : BaseActivity() {
    private var mAuthTask: UserSignUpTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        tv_SignUpPassword.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptSignUp()
                return@OnEditorActionListener true
            }
            false
        })

        btn_performSignUp.setOnClickListener { attemptSignUp() }
    }

    private fun attemptSignUp() {
        if (mAuthTask != null) {
            return
        }

        tv_SignUpEmail.error = null
        tv_SignUpPassword.error = null

        val emailStr = tv_SignUpEmail.text.toString()
        val passwordStr = tv_SignUpPassword.text.toString()
        val passwordRepeatStr = et_passwordRepeat.text.toString()

        var cancel = false
        var focusView: View? = null

        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr) && !TextUtils.isEmpty(passwordRepeatStr) && !isPasswordValid(
                passwordRepeatStr
            )
        ) {
            tv_SignUpPassword.error = getString(R.string.error_invalid_password)
            focusView = tv_SignUpPassword
            cancel = true
        }

        if (passwordStr != passwordRepeatStr) {
            tv_SignUpPassword.error = getString(R.string.error_passwords_mismatch)
            focusView = tv_SignUpPassword
            cancel = true
        }

        if (TextUtils.isEmpty(emailStr)) {
            tv_SignUpEmail.error = getString(R.string.error_field_required)
            focusView = tv_SignUpEmail
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            tv_SignUpEmail.error = getString(R.string.error_invalid_email)
            focusView = tv_SignUpEmail
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            mAuthTask = UserSignUpTask(emailStr, passwordStr)
            if (mAuthTask!!.execute(null as Void?).get()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 0 else 1).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    login_form.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    login_progress.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
    }

    private inner class UserSignUpTask(private val mEmail: String,
                                       private val mPassword: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean {
            return try {
                AuthApiManager()
                    .performSignUp(
                        Credentials(
                            email = mEmail, password = mPassword
                        )
                    )
                true
            } catch (e: ConflictApiException) {
                Toast.makeText(applicationContext, "Email duplicate!", Toast.LENGTH_LONG).show() //TODO
                false
            } catch (e: InternalApiException) {
                Toast.makeText(applicationContext, "Server error!", Toast.LENGTH_LONG).show() //TODO
                false
            } catch (e: InterruptedException) {
                false
            }
        }

        override fun onPostExecute(success: Boolean) {
            mAuthTask = null
            showProgress(true)

            if (success) {
                finish()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }
}
