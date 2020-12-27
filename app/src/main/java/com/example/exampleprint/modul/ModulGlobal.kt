package com.example.exampleprint.modul

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.exampleprint.R
import java.text.NumberFormat
import java.util.*

class ModuleGlobal{
    fun InitPreference(context: Context){
        val prefs : SharedPreferences = context.getSharedPreferences(" com.module.kotlin",0)
        val editor = prefs.edit()
        editor?.putString("namelocation","")
        editor?.putString("latitude","-7.2739823")
        editor?.putString("longtitude","112.7196448")
        editor?.apply()
    }
    fun CurrencyFormat(amoune: Double):String{
        val locale = Locale("in", "ID")
        val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(locale)
        return numberFormat.format(amoune)
    }
    fun SaveSharedPreference(context: Context,nameshared:String,valueshared:String){
        var prefs : SharedPreferences = context.getSharedPreferences(" com.module.kotlin",0)
        val editor = prefs?.edit()
        editor?.putString(nameshared,valueshared)
        editor?.apply()
    }
    fun GetSharedPreference(context: Context,nameshared:String):String{
        var prefs : SharedPreferences = context.getSharedPreferences(" com.module.kotlin",0)
        val tempname= prefs?.getString(nameshared,"")
        return tempname.toString()
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
    fun HideKeyBoard(context: Context,currentFocus: View){
        val inputManager: InputMethodManager =context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
    }
    fun GetIPServer(context: Context):String {
        val iptemp = if (GetSharedPreference(context, "ipsharedpreference") != ""){
            GetSharedPreference(context, "ipsharedpreference")
        }else context.getString(R.string.ipserver)
        return iptemp
    }
}