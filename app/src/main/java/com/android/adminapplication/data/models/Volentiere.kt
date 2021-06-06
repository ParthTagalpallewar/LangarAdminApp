package com.android.adminapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "volentiereTable")
data class Volentiere(

    val id:String,
    val phone: String,
    val token: String? = null,
    val address :String?,
    val address2 :String?,
    val city :String?,
    val state :String?,
    val country :String?,
    val postalCode :String?,
    val knownName :String?,
    val status:String?,


    @PrimaryKey
    var uId: Int = Volentiere_Primary_key
){

    companion object{
        val Volentiere_Primary_key = 0
    }


}