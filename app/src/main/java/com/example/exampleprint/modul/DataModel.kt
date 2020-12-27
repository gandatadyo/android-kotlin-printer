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

@Parcelize
data class ModelListTrans(
    var ID: Int?,
    var DocNumber: String?,
    var DocDate: String?,
    val IDAdmin: String?,
    val NameAdmin: String?,
    var CustomerName: String?,
    var CustomerTelp: String?,
    var CustomerAddress: String?,
    var Amount: Double?,
    var GrandTotal: Double?,
    var TimeCreated: String?,
    var TimeUpdated: String?
): Parcelable

@Parcelize
data class ModelListProduct(
    var ID: Int?,
    var CodeProduct: String?,
    var NameProduct: String?,
    val Description: String?,
    var Price: Double?,
    var Img: String?,
    var isDelete: Int?,
    var TimeCreated: String?,
    var TimeUpdated: String?
): Parcelable

