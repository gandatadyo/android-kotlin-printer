package com.example.exampleprint

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exampleprint.modul.AdapterOrder
import com.example.exampleprint.modul.ModelListTrans
import com.example.exampleprint.modul.RestApi
import kotlinx.android.synthetic.main.activity_history_order.*
import org.json.JSONArray
import org.json.JSONObject

class HistoryOrderActivity : AppCompatActivity() {
    private val restApi = RestApi()
    private var dataOrder =  arrayListOf<ModelListTrans>()
    private val adapterlist = AdapterOrder(dataOrder) { context: Context, partItem : ModelListTrans -> ChooseItem(context,partItem) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_order)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        lblnotfound_order.visibility = View.GONE

        recyclerview_order.setHasFixedSize(true)
        recyclerview_order.layoutManager =  LinearLayoutManager(this)
        recyclerview_order.adapter = adapterlist
        restApi.GetDataOrder(this) { response:String ->  HandleData(response)}
    }

    private fun ChooseItem(context: Context,data: ModelListTrans){

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

    private fun HandleData(response: String){
        val responseObject = JSONObject(response)
        val list: JSONArray = responseObject.getJSONArray("results")
        dataOrder.clear()

        if(list.length()>0){
            lblnotfound_order.visibility = View.GONE
            for (i in 0 until list.length()) {
                dataOrder.add(
                    ModelListTrans(
                        list.getJSONObject(i).getInt("ID"),
                        list.getJSONObject(i).getString("DocNumber"),
                        list.getJSONObject(i).getString("DocDate"),
                        list.getJSONObject(i).getString("IDAdmin"),
                        list.getJSONObject(i).getString("NameAdmin"),
                        list.getJSONObject(i).getString("CustomerName"),
                        list.getJSONObject(i).getString("CustomerTelp"),
                        list.getJSONObject(i).getString("CustomerAddress"),
                        list.getJSONObject(i).getDouble("Amount"),
                        list.getJSONObject(i).getDouble("GrandTotal"),
                        list.getJSONObject(i).getString("TimeCreated"),
                        list.getJSONObject(i).getString("TimeUpdated")
                    )
                )
            }
        }else lblnotfound_order.visibility = View.VISIBLE
        adapterlist .notifyDataSetChanged()
        swireRefresh_order.isRefreshing = false
    }
}
