package com.example.exampleprint.modul

import android.app.Activity
import android.content.ContentValues
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exampleprint.R
import com.google.android.material.snackbar.Snackbar

class RestApi {
//    val dbmodul = DatabaseModul()
    val modulGlobal = ModuleGlobal()

    fun LoginAdmin(activity:Activity,username:String,password:String,clickItem:(activity:Activity,response:String)->Unit){
        if(!modulGlobal.isNetworkAvailable(activity.baseContext)) {
            Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(
                R.string.resulterrorconnection
            ),Snackbar.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(activity.baseContext)
            val url =  modulGlobal.GetIPServer(activity.baseContext) + "/api/login"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    clickItem(activity,response)
                }, Response.ErrorListener {
                    Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(
                        R.string.resulterror
                    ),Snackbar.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = username
                    params["password"] = password
                    return params
                }
            }
            queue.add(postRequest)
        }
    }

    fun GetDataMaster(activity:Activity,clickItem:(response:String)->Unit){
        if(!modulGlobal.isNetworkAvailable(activity.baseContext)) {
            Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(
                R.string.resulterrorconnection
            ),Snackbar.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(activity.baseContext)
            val url =  modulGlobal.GetIPServer(activity.baseContext) + "/systemglobal/show_datamaster"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    clickItem(response)
                }, Response.ErrorListener {
                    Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(
                        R.string.resulterror
                    ),Snackbar.LENGTH_LONG).show()
                }) {}
            queue.add(postRequest)
        }
    }

    fun GetDataOrder(activity:Activity,clickItem:(response:String)->Unit){
        if(!modulGlobal.isNetworkAvailable(activity.baseContext)) {
            Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(
                R.string.resulterrorconnection
            ),Snackbar.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(activity.baseContext)
            val url =  modulGlobal.GetIPServer(activity.baseContext) + "/systemglobal/show_datatrans"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    clickItem(response)
                }, Response.ErrorListener {
                    Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(
                        R.string.resulterror
                    ),Snackbar.LENGTH_LONG).show()
                }) {}
            queue.add(postRequest)
        }
    }

    fun GetDataDetailTransaction(activity:Activity,docnumber:String,clickItem:(response:String)->Unit){
        if(!modulGlobal.isNetworkAvailable(activity.baseContext)) {
            Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(
                R.string.resulterrorconnection
            ),Snackbar.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(activity.baseContext)
            val url = modulGlobal.GetIPServer(activity.baseContext) + "/systemglobal/data_detailtransaction"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    clickItem(response)
                }, Response.ErrorListener {
                    Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(
                        R.string.resulterror
                    ),Snackbar.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["docnumber"] = docnumber
                    return params
                }
            }
            queue.add(postRequest)
        }
    }

    fun SendDataOrder(activity:Activity,contentValues: ContentValues,clickItem:(response:String)->Unit){
        if(!modulGlobal.isNetworkAvailable(activity.baseContext)) {
            Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterrorconnection),Snackbar.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(activity.baseContext)
            val url = modulGlobal.GetIPServer(activity.baseContext) + "/systemglobal/input_order"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    clickItem(response)
                }, Response.ErrorListener {
                    Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterror),Snackbar.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["namecustomer"] = contentValues.getAsString("namecustomer")
                    params["telp"] = contentValues.getAsString("telp")
                    params["address"] = contentValues.getAsString("address")
                    params["startdaterent"] = contentValues.getAsString("startdaterent")
                    params["renttime"] = contentValues.getAsString("renttime")
                    params["qty"] = contentValues.getAsString("qty")
                    params["notes"] = contentValues.getAsString("notes")
                    return params
                }
            }
            queue.add(postRequest)
        }
    }


    fun GetDataAccount(activity:Activity,clickItem:(response:String)->Unit){
//        if(!modulGlobal.isNetworkAvailable(activity.baseContext)) {
//            Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterrorconnection),Snackbar.LENGTH_LONG).show()
//        }else {
//            val modelListAccount = dbmodul.GetDataProfle(activity.baseContext)
//            val queue = Volley.newRequestQueue(activity.baseContext)
//            val url = modulGlobal.GetIPServer(activity.baseContext) + "/systemglobal/show_infoaccountadmin"
//            val postRequest = object : StringRequest(
//                Method.POST, url,
//                Response.Listener<String> { response ->
//                    clickItem(response)
//                }, Response.ErrorListener {
//                    Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterror),Snackbar.LENGTH_LONG).show()
//                }) {
//                override fun getParams(): Map<String, String> {
//                    val params = HashMap<String, String>()
//                    params["idaccount"] = modelListAccount.ID.toString()
//                    return params
//                }
//            }
//            queue.add(postRequest)
//        }
    }

    fun LogoutAccount(activity:Activity,clickItem:(response:String)->Unit){
//        if(!modulGlobal.isNetworkAvailable(activity.baseContext)) {
//            Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterrorconnection),Snackbar.LENGTH_LONG).show()
//        }else {
//            val modelListAccount = dbmodul.GetDataProfle(activity.baseContext)
//            val queue = Volley.newRequestQueue(activity.baseContext)
//            val url = modulGlobal.GetIPServer(activity.baseContext) + "/systemglobal/logout_admin"
//            val postRequest = object : StringRequest(
//                Method.POST, url,
//                Response.Listener<String> { response ->
//                    clickItem(response)
//                }, Response.ErrorListener {
//                    Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterror),Snackbar.LENGTH_LONG).show()
//                }) {
//                override fun getParams(): Map<String, String> {
//                    val params = HashMap<String, String>()
//                    params["idaccount"] = modelListAccount.ID.toString()
//                    return params
//                }
//            }
//            queue.add(postRequest)
//        }
    }

    fun ChangePassword(activity:Activity,contentValues: ContentValues,clickItem:(response:String)->Unit){
//        if(!modulGlobal.isNetworkAvailable(activity.baseContext)) {
//            Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterrorconnection),Snackbar.LENGTH_LONG).show()
//        }else {
//            val modelListAccount = dbmodul.GetDataProfle(activity.baseContext)
//            val queue = Volley.newRequestQueue(activity.baseContext)
//            val url = modulGlobal.GetIPServer(activity.baseContext) + "/systemglobal/changepasswordadmin"
//            val postRequest = object : StringRequest(
//                Method.POST, url,
//                Response.Listener<String> { response ->
//                    clickItem(response)
//                }, Response.ErrorListener {
//                    Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterror),Snackbar.LENGTH_LONG).show()
//                }) {
//                override fun getParams(): Map<String, String> {
//                    val params = HashMap<String, String>()
//                    params["idaccount"] = modelListAccount.ID.toString()
//                    params["oldpassword"] = contentValues.getAsString("oldpassword")
//                    params["newpassword"] = contentValues.getAsString("newpassword")
//                    return params
//                }
//            }
//            queue.add(postRequest)
//        }
    }
}