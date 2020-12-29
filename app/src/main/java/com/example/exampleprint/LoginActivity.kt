package com.example.exampleprint

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.exampleprint.database.DatabaseModul
import com.example.exampleprint.modul.ModuleGlobal
import com.example.exampleprint.modul.RestApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private val modulGlobal = ModuleGlobal()
    private val restApi = RestApi()
    private val dbmodul = DatabaseModul()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        this.supportActionBar?.hide()
//        this.supportActionBar?.title="Login"

//        progressdlg.visibility = View.GONE

        btnLogin.setOnClickListener {
            currentFocus?.let { modulGlobal.HideKeyBoard(this, it) }
//            progressdlg.visibility = View.VISIBLE
            restApi.LoginAdmin(this,edtUsername_Login.text.toString(),edtPassword_Login.text.toString()) { partActivity: Activity, partItem:String-> HandleResponse(partActivity, partItem)}
        }

        if(CheckAccount()){
            startActivity(Intent(this, MenuActivity::class.java))
            finishAffinity()
        }

//        lblCaption_Login.setOnClickListener {
//            startActivity(Intent(this, TestingActivity::class.java))
//        }
    }

    private fun CheckAccount():Boolean{
        val cursor: Cursor =  dbmodul.OpenQuery(this,"select * from dbmadmin limit 1")
        return cursor.count>0
    }

    private fun HandleResponse(activity: Activity, response: String){
//        progressdlg.visibility = View.GONE
        val status = JSONObject(response).getString("status")
        val message = JSONObject(response).getString("message")
        if(status=="true"){
            if (SaveAccount()) {
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            }else Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterror),
                Snackbar.LENGTH_LONG).show()
        }else Snackbar.make(activity.findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG).show()
    }

    private fun SaveAccount():Boolean{
//        var flagresult = false
        dbmodul.ExeqQuery(this,"Delete From dbmadmin")

        val item = ContentValues()
//        val list = JSONArray(objdata)
//        if(list.length()>0) {
//            item.put("IDAccount", "")
            item.put("Username", edtUsername_Login.text.toString())
            item.put("Password", "")
//            item.put("isDelete", "")
//            item.put("TimeCreated", "")
//            item.put("TimeUpdated", "")
            dbmodul.InsertAccount(this, item)
//            flagresult = true
//        }
        return true
    }
}
