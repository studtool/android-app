package ru.mail.park.studtool.activity

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.auth.Credentials
import java.text.DateFormat

abstract class BaseActivity : AppCompatActivity() {

    fun showErrorMessage(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

    fun saveCredentials(credentials: Credentials) {
        val editor = getCredentialsSharedPreferences().edit()

        editor.putString(CREDENTIALS_EMAIL_KEY, credentials.email)
        editor.putString(CREDENTIALS_PASSWORD_KEY, credentials.password)

        editor.apply()
    }

    fun loadCredentials(): Credentials? {
        val p = getCredentialsSharedPreferences()

        val email = p.getString(CREDENTIALS_EMAIL_KEY, null) ?: return null
        val password = p.getString(CREDENTIALS_PASSWORD_KEY, null) ?: return null

        return Credentials(email, password)
    }

    fun saveAuthInfo(authInfo: AuthInfo) {
        val editor = getAuthInfoSharedPreferences().edit()

        editor.putString(AUTH_INFO_USER_ID_KEY, authInfo.userId)
        editor.putString(AUTH_INFO_AUTH_TOKEN_KEY, authInfo.authToken)
        editor.putString(AUTH_INFO_REFRESH_TOKEN_KEY, authInfo.refreshToken)
        editor.putString(AUTH_INFO_EXPIRE_TIME_KEY, getDateFormat().format(authInfo.expireTime))

        editor.apply()
    }

    fun loadAuthInfo(): AuthInfo? {
        val p = getAuthInfoSharedPreferences()

        val userId = p.getString(AUTH_INFO_USER_ID_KEY, null) ?: return null
        val authToken = p.getString(AUTH_INFO_AUTH_TOKEN_KEY, null) ?: return null
        val refreshToken = p.getString(AUTH_INFO_REFRESH_TOKEN_KEY, null) ?: return null
        val expireTime = p.getString(AUTH_INFO_EXPIRE_TIME_KEY, null) ?: return null

        return AuthInfo(userId, authToken, refreshToken, getDateFormat().parse(expireTime))
    }

    private fun getCredentialsSharedPreferences(): SharedPreferences {
        return getSharedPreferences(CREDENTIALS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private fun getAuthInfoSharedPreferences(): SharedPreferences {
        return getSharedPreferences(AUTH_INFO_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private fun getDateFormat(): DateFormat {
        return DateFormat.getDateTimeInstance()
    }

    private companion object {
        const val CREDENTIALS_SHARED_PREFERENCES_NAME = "STUDTOOL_CREDENTIALS_SHARED_PREFERENCES"
        const val AUTH_INFO_SHARED_PREFERENCES_NAME = "STUDTOOL_AUTH_INFO_SHARED_PREFERENCES"

        const val CREDENTIALS_EMAIL_KEY = "CREDENTIALS_EMAIL"
        const val CREDENTIALS_PASSWORD_KEY = "CREDENTIALS_PASSWORD"

        const val AUTH_INFO_USER_ID_KEY = "AUTH_INFO_USER_ID"
        const val AUTH_INFO_AUTH_TOKEN_KEY = "AUTH_INFO_AUTH_TOKEN"
        const val AUTH_INFO_REFRESH_TOKEN_KEY = "AUTH_INFO_REFRESH_TOKEN"
        const val AUTH_INFO_EXPIRE_TIME_KEY = "AUTH_INFO_EXPIRE_TIME_KEY"
    }
}
