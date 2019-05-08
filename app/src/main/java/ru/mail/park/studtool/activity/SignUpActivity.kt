package ru.mail.park.studtool.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.ScrollView
import kotlinx.android.synthetic.main.activity_login.*
import ru.mail.park.studtool.R
import ru.mail.park.studtool.api.AuthApiManager
import ru.mail.park.studtool.auth.Credentials
import ru.mail.park.studtool.exception.ConflictApiException
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.validator.CredentialsValidator

class SignUpActivity : BaseActivity() {
    private var mAuthTask: UserSignUpTask? = null

    private lateinit var mScvSignUpForm: ScrollView
    private lateinit var mPbSignUpProgress: ProgressBar

    private lateinit var mEtSignUpEmail: EditText
    private lateinit var mEtSignUpPassword: EditText
    private lateinit var mEtSignUpPasswordRepeat: EditText

    private lateinit var mBtnPerformSignUp: Button
    private lateinit var mCredentialsValidator: CredentialsValidator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mScvSignUpForm = findViewById(R.id.scv_SignUpForm)
        mPbSignUpProgress = findViewById(R.id.pb_SignUpProgress)

        mEtSignUpEmail = findViewById(R.id.et_SignUpEmail)
        mEtSignUpPassword = findViewById(R.id.et_SignUpPassword)
        mEtSignUpPasswordRepeat = findViewById(R.id.et_SignUpPasswordRepeat)

        mBtnPerformSignUp = findViewById(R.id.btn_AttemptSignUp)
        mBtnPerformSignUp.setOnClickListener { attemptSignUp() }

        mCredentialsValidator = CredentialsValidator()
    }

    private fun attemptSignUp() {
        if (mAuthTask != null) {
            return
        }

        mEtSignUpEmail.error = null
        mEtSignUpPassword.error = null

        val emailStr = mEtSignUpEmail.text.toString()
        val passwordStr = mEtSignUpPassword.text.toString()
        val passwordRepeatStr = mEtSignUpPasswordRepeat.text.toString()

        var cancel = false
        var focusView: View? = null

        var validationResult = mCredentialsValidator.ValidateEmail(emailStr)
        if (!validationResult.first) {
            mEtSignUpEmail.error = validationResult.second
            focusView = mEtSignUpEmail
            cancel = true
        }
        validationResult = mCredentialsValidator.ValidatePassword(passwordStr)
        if (!validationResult.first) {
            mEtSignUpPassword.error = validationResult.second
            focusView = mEtSignUpPassword
            cancel = true
        }

        if (passwordRepeatStr != passwordStr) {
            mEtSignUpPasswordRepeat.error = "Passwords mismatch!"
            focusView = mEtSignUpPasswordRepeat
            cancel = true
        }

        if (cancel) {
            focusView!!.requestFocus()
        } else {
            showProgress(true)
            mAuthTask = UserSignUpTask(Credentials(emailStr, passwordStr))
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        mScvSignUpForm.visibility = if (show) View.GONE else View.VISIBLE
        mScvSignUpForm.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 0 else 1).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mScvSignUpForm.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

        mPbSignUpProgress.visibility = if (show) View.VISIBLE else View.GONE
        mPbSignUpProgress.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mPbSignUpProgress.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
    }

    private inner class UserSignUpTask(private val mCredentials: Credentials) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean {
            return try {
                AuthApiManager().performSignUp(mCredentials)
                true
            } catch (e: ConflictApiException) {
                et_SignInEmail.error = getString(R.string.msg_email_duplicate)
                et_SignInEmail.requestFocus()
                false
            } catch (e: InternalApiException) {
                showErrorMessage(getString(R.string.msg_internal_server_error))
                false
            } catch (e: InterruptedException) {
                false
            }
        }

        override fun onPostExecute(success: Boolean) {
            mAuthTask = null
            showProgress(false)

            if (success) {
                saveCredentials(mCredentials)
                startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                finish()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }
}
