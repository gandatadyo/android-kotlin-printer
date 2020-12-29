package com.example.exampleprint.modul

import android.app.Activity
import android.content.ContentValues
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exampleprint.R
import com.google.android.material.snackbar.Snackbar

class RestApi {
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
}