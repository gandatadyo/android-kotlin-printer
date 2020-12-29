package com.example.exampleprint.modul

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelListAdmin(
    var ID: Int?,
    var NameAdmin: String?,
    var Username: String?,
    val Password: String?,
    var isDelete: Int?,
    var TimeCreated: String?,
    var TimeUpdated: String?
): Parcelable

//data list main
@Parcelize
data class ModelCustomer(
    var ID: String?,
    var NameCustomer: String?,
    var Email: String?,
    val Telp: String?,
    var Address: String?
): Parcelable

@Parcelize
data class ModelProduct(
    var ID: String?,
    var NameProduct: String?,
    var Description: String?,
    var Price: String?
): Parcelable

@Parcelize
data class ModelInputProduct(
    var ID: String?,
    var NameProduct: String?,
    var Qty: Int?,
    var Price: Double?,
    var Total: Double?
): Parcelable

@Parcelize
data class ModelListTransaction(
    var ID: String?,
    var DocNumber: String?,
    var NameCustomer: String?,
    var RentDate: String?,
    var ReturnDate: String?,
    var GrandTotal: String?,
    var TimeCreated: String?
): Parcelable
