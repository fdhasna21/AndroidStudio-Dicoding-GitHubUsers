package com.fdhasna21.githubusers.dataclass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parceler

@Parcelize
data class Account(
    val avatar: String?,
    val username: String?,
    val name: String?,
    val company: String?,
    val location: String?,
    val repository: Int,
    val follower: Int,
    val following: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    companion object : Parceler<Account> {

        override fun Account.write(parcel: Parcel, flags: Int) {
            parcel.writeString(avatar)
            parcel.writeString(username)
            parcel.writeString(name)
            parcel.writeString(company)
            parcel.writeString(location)
            parcel.writeInt(repository)
            parcel.writeInt(follower)
            parcel.writeInt(following)
        }

        override fun create(parcel: Parcel): Account {
            return Account(parcel)
        }
    }
}
