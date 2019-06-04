package ru.mail.park.studtool

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_sidebar.*
import kotlinx.android.synthetic.main.nav_header_sidebar.view.*
import ru.mail.park.studtool.activity.BaseActivity
import android.content.Intent
import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import ru.mail.park.studtool.api.DocumentsApiManager
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.UnauthorizedException
import ru.mail.park.studtool.home.HomeFragment
import java.util.ArrayList

class NavigationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mDocumentTask: NewDocumentTask? = null

    fun withEditText(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle(R.string.create_file_title)
        val dialogLayout = inflater.inflate(R.layout.dialog_new_document, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.create_new_document_button) { dialogInterface, i ->
            val message = editText.text.toString()
            if (mDocumentTask == null) {
                mDocumentTask = NewDocumentTask(DocumentInfo(title = message, subject = "subject"), loadAuthInfo()!!)
                mDocumentTask!!.execute(null as Void?)
                Thread.sleep(1_000)

            } else {
                return@setPositiveButton
            }

        }
        builder.show()
    }


    val fragment1: Fragment = HomeFragment()
//        .apply {
//        arguments = Bundle().apply {
//            putString("AUTH_INFO", loadAuthInfo()!!.authToken)
//        }
//    }
    val fragment2: Fragment = DashboardFragment()
    val fragment3: Fragment = NotificationsFragment()
    val fm = supportFragmentManager
    var active = fragment1

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                fm.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                fm.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)


        setSupportActionBar(toolbar_lol)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar_lol, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.sidebar, menu)
        val sidebarHeader = findViewById<LinearLayout>(R.id.sidebar_header)

        sidebarHeader.sidebar_email.text = loadCredentials()!!.email
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    private inner class NewDocumentTask(private val mDocumentInfo: DocumentInfo, private val mAuthInfo: AuthInfo) :
        AsyncTask<Void, Void, DocumentInfo?>() {

        override fun doInBackground(vararg params: Void): DocumentInfo? {
            return try {
                DocumentsApiManager().addDocument(mDocumentInfo, mAuthInfo)
            } catch (e: UnauthorizedException) {
                showErrorMessage(getString(R.string.msg_wrong_credentials))
                null
            } catch (e: InternalApiException) {
                showErrorMessage(getString(R.string.msg_internal_server_error))
                null
            } catch (e: InterruptedException) {
                null
            }
        }

        override fun onPostExecute(documentInfo: DocumentInfo?) {
            mDocumentTask = null
            if (documentInfo != null) {
                showErrorMessage("Создан: " + mDocumentInfo.title)
            }
        }
    }
}
