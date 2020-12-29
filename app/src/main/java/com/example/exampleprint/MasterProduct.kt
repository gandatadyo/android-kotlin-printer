package com.example.exampleprint

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exampleprint.modul.*
import kotlinx.android.synthetic.main.activity_master_product.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class MasterProduct : AppCompatActivity() {
    private val modulGlobal = ModuleGlobal()
    private var dataProduct =  arrayListOf<ModelProduct>()
    private val adapterTrans = AdapterProduct(dataProduct) { partItem :  ModelProduct -> ChooseProduct(partItem) }
    private lateinit var linearLayoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_product)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnAddProduct.setOnClickListener { startActivity(Intent(this, InputProduct::class.java)) }

        linearLayoutManager = LinearLayoutManager(this)
        recycler_ListProduct.layoutManager = linearLayoutManager
        recycler_ListProduct.adapter = adapterTrans

    }

    override fun onResume() {
        super.onResume()
        GetDataProduct()
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

    private fun GetDataProduct(){
        if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this,getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/list_product"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_GetDataProduct(response)
                }, Response.ErrorListener {
                    Toast.makeText(this,getString(R.string.resulterror), Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
//                    params["data"] = data
                    return params
                }
            }
            queue.add(postRequest)
        }
    }

    private fun Handle_GetDataProduct(response:String){
        val responseObject = JSONObject(response)
        val list: JSONArray = responseObject.getJSONArray("results")
        dataProduct.clear()
        if(list.length()>0){
            for (i in 0 until list.length()) {
                dataProduct.add(
                    ModelProduct(
                        list.getJSONObject(i).getString("ID"),
                        list.getJSONObject(i).getString("NameProduct"),
                        list.getJSONObject(i).getString("Description"),
                        list.getJSONObject(i).getString("Price")
                    )
                )
            }
        }
        adapterTrans.notifyDataSetChanged()
    }

    private fun ChooseProduct(data:ModelProduct){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Hapus Barang ?")
        builder.setCancelable(false)
        builder.setPositiveButton("Ya") { dialog, _ ->
            DeleteProduct(data.ID.toString())
            dialog.dismiss()
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun DeleteProduct(iddata:String){
        if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this,getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/product_delete"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_DeleteProduct(response)
                }, Response.ErrorListener {
                    Toast.makeText(this,getString(R.string.resulterror), Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["iddata"] = iddata
                    return params
                }
            }
            queue.add(postRequest)
        }
    }

    private fun Handle_DeleteProduct(response:String){
        val status = JSONObject(response).getString("status")
        val message = JSONObject(response).getString("message")
        if(status=="true"){
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
            GetDataProduct()
        }else{
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        }
    }
}
