package com.example.rohal.task_work

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

public class Person() :Parcelable {
    var id:String=" "
    var name: String = " "
    var ur1:  String=" "
    var type: String=" "
    var phone: String= " "
    var email: String= " "
    var country: String=" "

    constructor(parcel: Parcel) : this() {
        ur1 = parcel.readString()
        name=parcel.readString()
        email=parcel.readString()
        phone=parcel.readString()
        country=parcel.readString()
        type=parcel.readString()
        id=parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ur1)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(country)
        parcel.writeString(type)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return Person[size]
        }


        private operator fun CREATOR.get(size: Int): Array<Person?> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}
