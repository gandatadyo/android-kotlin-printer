package com.example.exampleprint

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.exampleprint.database.DatabaseModul
import com.example.exampleprint.modul.ModuleGlobal
import com.example.exampleprint.modul.RestApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private val modulGlobal = ModuleGlobal()
    private val restApi = RestApi()
    private val dbmodul = DatabaseModul()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        this.supportActionBar?.hide()

        btnLogin.setOnClickListener {
            currentFocus?.let { modulGlobal.HideKeyBoard(this, it) }
            restApi.LoginAdmin(this,edtUsername_Login.text.toString(),edtPassword_Login.text.toString()) { partActivity: Activity, partItem:String-> HandleResponse(partActivity, partItem)}
        }

        if(CheckAccount()){
            startActivity(Intent(this, MenuActivity::class.java))
            finishAffinity()
        }
    }

    private fun CheckAccount():Boolean{
        val cursor: Cursor =  dbmodul.OpenQuery(this,"select * from dbmadmin limit 1")
        return cursor.count>0
    }

    private fun HandleResponse(activity: Activity, response: String){
        val status = JSONObject(response).getString("status")
        val message = JSONObject(response).getString("message")
        if(status=="true"){
            val data = JSONObject(response).getString("data")
            if (SaveAccount(data)) {
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            }else Snackbar.make(activity.findViewById(android.R.id.content),activity.baseContext.getString(R.string.resulterror),Snackbar.LENGTH_LONG).show()
        }else Snackbar.make(activity.findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG).show()
    }

    private fun SaveAccount(data:String):Boolean{
        dbmodul.ExeqQuery(this,"Delete From dbmadmin")

        val item = ContentValues()
        item.put("IDAccount", JSONObject(data).getString("id"))
        item.put("Username", edtUsername_Login.text.toString())
        item.put("Password", "")
        dbmodul.InsertAccount(this, item)
        return true
    }
}
