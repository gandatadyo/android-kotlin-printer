package com.example.exampleprint

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exampleprint.modul.*
import kotlinx.android.synthetic.main.activity_report_transaction.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class ReportTransaction : AppCompatActivity() {
    private val modulGlobal = ModuleGlobal()
    private var dataTransaction =  arrayListOf<ModelListTransaction>()
    private val adapterTrans = AdapterTransaction(dataTransaction) { partItem : ModelListTransaction -> ChooseTransaction(partItem) }
    private lateinit var linearLayoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_transaction)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        linearLayoutManager = LinearLayoutManager(this)
        recycler_Transaction.layoutManager = linearLayoutManager
        recycler_Transaction.adapter = adapterTrans

        GetDataReport()
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

    private fun GetDataReport(){
        if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this,getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/list_transaction"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_GetDataReport(response)
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

    private fun Handle_GetDataReport(response:String){
        val responseObject = JSONObject(response)
        val list: JSONArray = responseObject.getJSONArray("results")
        dataTransaction.clear()
        if(list.length()>0){
            for (i in 0 until list.length()) {
                dataTransaction.add(
                    ModelListTransaction(
                        list.getJSONObject(i).getString("ID"),
                        list.getJSONObject(i).getString("DocNumber"),
                        list.getJSONObject(i).getString("NameCustomer"),
                        list.getJSONObject(i).getString("RentDate"),
                        list.getJSONObject(i).getString("ReturnDate"),
                        list.getJSONObject(i).getString("GrandTotal"),
                        list.getJSONObject(i).getString("TimeCreated"),
                        list.getJSONObject(i).getString("TimeRent")
                    )
                )
            }
        }
        adapterTrans.notifyDataSetChanged()
    }
    
    private fun ChooseTransaction(data:ModelListTransaction){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Hapus Transaksi ?")
        builder.setCancelable(false)
        builder.setPositiveButton("Ya") { dialog, _ ->
            DeleteTransaction(data.ID.toString())
            dialog.dismiss()
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun DeleteTransaction(iddata:String){
        if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this,getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/delete_transaction"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_DeleteTransaction(response)
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

    private fun Handle_DeleteTransaction(response:String){
        val status = JSONObject(response).getString("status")
        val message = JSONObject(response).getString("message")
        if(status=="true"){
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
            GetDataReport()
        }else{
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        }
    }
}