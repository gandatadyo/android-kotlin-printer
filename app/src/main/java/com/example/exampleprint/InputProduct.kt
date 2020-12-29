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
import kotlinx.android.synthetic.main.activity_input_product.*
import org.json.JSONObject
import java.util.HashMap

class InputProduct : AppCompatActivity() {
    val modulGlobal= ModuleGlobal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_product)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSaveProduct.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Simpan Barang ?")
            builder.setCancelable(false)
            builder.setPositiveButton("Ya") { dialog, _ ->
                SaveProduct()
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

    private fun SaveProduct(){
        val name = edtNameProduct_InputProduct.text.toString()
        val price = edtPrice_InputProduct.text.toString()
        if((name=="") || (price=="") ){
            Toast.makeText(this,"Mohon isi data dengan lengkap", Toast.LENGTH_LONG).show()
        }else if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this,getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/product_insert"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_SaveProduct(response)
                }, Response.ErrorListener {
                    Toast.makeText(this,getString(R.string.resulterror), Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["name"] = name
                    params["price"] = price
                    return params
                }
            }
            queue.add(postRequest)
        }
    }

    private fun Handle_SaveProduct(response:String){
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