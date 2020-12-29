package com.example.exampleprint

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exampleprint.modul.AdapterCustomer
import com.example.exampleprint.modul.ModelCustomer
import com.example.exampleprint.modul.ModuleGlobal
import kotlinx.android.synthetic.main.activity_master_customer.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class MasterCustomer : AppCompatActivity() {
    private val modulGlobal = ModuleGlobal()
    private var dataCustomer =  arrayListOf<ModelCustomer>()
    private val adapterTrans = AdapterCustomer(dataCustomer) { partItem : ModelCustomer -> ChooseCustomer(partItem) }
    private lateinit var linearLayoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_customer)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnAddCustomer.setOnClickListener { startActivity(Intent(this, InputCustomer::class.java)) }

        linearLayoutManager = LinearLayoutManager(this)
        recycler_ListCustomer.layoutManager = linearLayoutManager
        recycler_ListCustomer.adapter = adapterTrans
    }

    override fun onResume() {
        super.onResume()
        GetDataCustomer()
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

    private fun GetDataCustomer(){
        if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this,getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/list_customer"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_GetDataCustomer(response)
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

    private fun Handle_GetDataCustomer(response:String){
        val responseObject = JSONObject(response)
        val list: JSONArray = responseObject.getJSONArray("results")
        dataCustomer.clear()
        if(list.length()>0){
            for (i in 0 until list.length()) {
                dataCustomer.add(
                    ModelCustomer(
                        list.getJSONObject(i).getString("ID"),
                        list.getJSONObject(i).getString("NameCustomer"),
                        list.getJSONObject(i).getString("Email"),
                        list.getJSONObject(i).getString("Telp"),
                        list.getJSONObject(i).getString("Address")
                    )
                )
            }
        }
        adapterTrans.notifyDataSetChanged()
    }

    private fun ChooseCustomer(data:ModelCustomer){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Hapus Customer ?")
        builder.setCancelable(false)
        builder.setPositiveButton("Ya") { dialog, _ ->
            DeleteCustomer(data.ID.toString())
            dialog.dismiss()
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun DeleteCustomer(iddata:String){
        if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this,getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/customer_delete"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_DeleteCustomer(response)
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

    private fun Handle_DeleteCustomer(response:String){
        val status = JSONObject(response).getString("status")
        val message = JSONObject(response).getString("message")
        if(status=="true"){
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
            GetDataCustomer()
        }else{
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        }
    }
}