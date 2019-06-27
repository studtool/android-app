package ru.mail.park.studtool.logic

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_sidebar.*
import kotlinx.android.synthetic.main.nav_header_sidebar.view.*
import ru.mail.park.studtool.DashboardFragment
import ru.mail.park.studtool.NotificationsFragment
import ru.mail.park.studtool.R
import ru.mail.park.studtool.activity.BaseActivity
import ru.mail.park.studtool.api.DocumentsApiManager
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.dummy.DummyContent
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.NotFoundApiException
import ru.mail.park.studtool.exception.UnauthorizedException
import java.lang.Error

class Logic: BaseActivity() {

    class NewDocumentTask(
        var mDocumentTask: NewDocumentTask?,
        private val mDocumentInfo: DocumentInfo,
        private val mAuthInfo: AuthInfo,
        var mStatus: Boolean
    ) :
        AsyncTask<Void, Void, DocumentInfo?>() {

        override fun doInBackground(vararg params: Void): DocumentInfo? {
            return try {
                DocumentsApiManager().addDocument(mDocumentInfo, mAuthInfo)
            } catch (e: UnauthorizedException) {
                mStatus = false;
                null
            } catch (e: InternalApiException) {
                null
            } catch (e: InterruptedException) {
                null
            }
        }

        override fun onPostExecute(documentInfo: DocumentInfo?) {
            mDocumentTask = null
            if (documentInfo != null) {
                mStatus = true;
            }
        }
    }



    class GetDocumentsTask(var mGetDocumentsTask: GetDocumentsTask?,
                           private val mAuthInfo: AuthInfo,
                           var mStatus: Boolean,
                           var mMessage: String
    ) :
        AsyncTask<Void, Void, Array<DocumentInfo>>() {

        override fun doInBackground(vararg params: Void): Array<DocumentInfo> {
            return try {
                DocumentsApiManager().getDocumentsList("subject", mAuthInfo)
            } catch (e: UnauthorizedException) {
                mStatus = false
                mMessage = "Вы не авторизованы"
                emptyArray<DocumentInfo>()
            } catch (e: InternalApiException) {
                mStatus = false
                mMessage = "Нет созданных документов"
                emptyArray<DocumentInfo>()
            } catch (e: NotFoundApiException) {
                emptyArray<DocumentInfo>()
            } catch (e: InterruptedException) {
                emptyArray<DocumentInfo>()
            }
        }

        override fun onPostExecute(documentsInfo: Array<DocumentInfo>) {
            mGetDocumentsTask = null

            if (documentsInfo != null) {
                DummyContent.DOCUMENTS = documentsInfo
                mStatus = true
            }
        }
    }

    class GetDocumentDetailsTask(
        var mDocumentTaskGetDocumentDetailsTask: GetDocumentDetailsTask?,
        private val documentId: String,
        private val authToken: String,
        var documentData: String?,
        private val editText: EditText
    ) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String {
            return try {
                String( DocumentsApiManager().getDocumentContent(documentId, AuthInfo(authToken=authToken)))
            } catch (e: UnauthorizedException) {
                return ""
            } catch (e: InternalApiException) {
                return ""
            } catch (e: InterruptedException) {
                return ""
            }
        }


        override fun onPostExecute(result: String?) {
            mDocumentTaskGetDocumentDetailsTask = null

            if (result != null) {

//                documentsInfo.copyInto(DOCUMENTS)

                documentData = result
                editText.text = Editable.Factory.getInstance().newEditable(documentData)

//                showErrorMessage("Получено документов " + result)
//                finish() //TODO show next activity
            }
        }
    }

    class PatchDocumentDetailsTask(
        var mDocumentTaskPatchDocumentDetailsTask: PatchDocumentDetailsTask?,
        private val documentId: String,
        private val data: String,
        private val authToken: String
    ) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String {
            return try {
                DocumentsApiManager().setDocumentContent(
                    documentId,
                    data.toByteArray(),
                    AuthInfo(authToken=authToken)
                ).toString()
            } catch (e: UnauthorizedException) {
                return ""
            } catch (e: InternalApiException) {
                return ""
            } catch (e: InterruptedException) {
                return ""
            }
        }


        override fun onPostExecute(result: String?) {
            mDocumentTaskPatchDocumentDetailsTask = null

            if (result != null) {

//                documentsInfo.copyInto(DOCUMENTS)

//                documentData = result
//                rootView.item_detail.text = Editable.Factory.getInstance().newEditable(documentData)

//                showErrorMessage("Изменения сохранены")
//                finish() //TODO show next activity
            }
        }
    }
}
