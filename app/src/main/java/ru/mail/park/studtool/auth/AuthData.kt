package ru.mail.park.studtool.auth

import android.os.Parcel
import android.os.Parcelable
import ru.mail.park.studtool.document.DocumentInfo

data class Credentials(
    val email: String,
    val password: String
)


data class AuthInfo(
    var userId: String = "",
    var authToken: String = "",
    var refreshToken: String = "",
    var sessionId: String = "",
    var expireTime: String = ""
): Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(authToken)
        parcel.writeString(refreshToken)
        parcel.writeString(sessionId)
        parcel.writeString(expireTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AuthInfo> {
        override fun createFromParcel(parcel: Parcel): AuthInfo {
            return AuthInfo(parcel)
        }

        override fun newArray(size: Int): Array<AuthInfo?> {
            return arrayOfNulls(size)
        }
    }
}
