package ru.mail.park.studtool.document

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.mail.park.studtool.R

// @Parcelize
// Note: IDE подчеркивает как ошибку, хотя это ей не является
// источник: https://proandroiddev.com/parcelable-in-kotlin-here-comes-parcelize-b998d5a5fcac


data class DocumentInfo(
    val documentId: String? = "",
    val title: String? = "",
    val ownerId: String? = "",
    val subject: String? = "",
    val icon: Int = R.drawable.ic_home_black_24dp
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(documentId)
        parcel.writeString(title)
        parcel.writeString(ownerId)
        parcel.writeString(subject)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DocumentInfo> {
        override fun createFromParcel(parcel: Parcel): DocumentInfo {
            return DocumentInfo(parcel)
        }

        override fun newArray(size: Int): Array<DocumentInfo?> {
            return arrayOfNulls(size)
        }
    }
}