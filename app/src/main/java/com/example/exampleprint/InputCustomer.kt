package com.example.exampleprint

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exampleprint.modul.ModuleGlobal
import kotlinx.android.synthetic.main.activity_input_customer.*
import org.json.JSONObject
import java.util.HashMap

class InputCustomer : AppCompatActivity() {
    val modulGlobal=ModuleGlobal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_customer)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSaveCustomer.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Simpan Customer ?")
            builder.setCancelable(false)
            builder.setPositiveButton("Ya") { dialog, _ ->
                SaveCustomer()
                dialog.dismiss()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun SaveCustomer(){
        val name = edtNameCustomer_InputCustomer.text.toString()
        val email = edtEmail_InputCustomer.text.toString()
        val telp = edtTelp_InputCustomer.text.toString()
        val address = edtAddress_InputCustomer.text.toString()
        if((name=="") || (email=="") ||(telp=="") ||(address=="")){
            Toast.makeText(this,"Mohon isi data dengan lengkap", Toast.LENGTH_LONG).show()
        }else if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this,getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/customer_insert"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_SaveCustomer(response)
                }, Response.ErrorListener {
                    Toast.makeText(this,getString(R.string.resulterror), Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["name"] = name
                    params["email"] = email
                    params["telp"] = telp
                    params["address"] = address
                    return params
                }
            }
            queue.add(postRequest)
        }
    }

    private fun Handle_SaveCustomer(response:String){
        val status = JSONObject(response).getString("status")
        val message = JSONObject(response).getString("message")
        if(status=="true"){
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
            finish()
        }else{
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        }
    }
}