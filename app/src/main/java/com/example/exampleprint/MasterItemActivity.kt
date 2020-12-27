package com.example.exampleprint

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exampleprint.modul.AdapterProduct
import com.example.exampleprint.modul.ModelListProduct
import com.example.exampleprint.modul.RestApi
import kotlinx.android.synthetic.main.activity_master_item.*
import org.json.JSONArray
import org.json.JSONObject

class MasterItemActivity : AppCompatActivity() {
    private val restApi = RestApi()
    private var dataProduct =  arrayListOf<ModelListProduct>()
    private val adapterlist = AdapterProduct(dataProduct) { context: Context, partItem : ModelListProduct -> ChooseItem(context,partItem) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_item)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        lblnotfound_product.visibility = View.GONE

        recyclerview_product.setHasFixedSize(true)
        recyclerview_product.layoutManager =  LinearLayoutManager(this)
        recyclerview_product.adapter = adapterlist
        restApi.GetDataMaster(this) { response:String ->  HandleData(response)}
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

    private fun ChooseItem(context: Context,data:ModelListProduct){

    }

    private fun HandleData(response: String){
        val responseObject = JSONObject(response)
        val list: JSONArray = responseObject.getJSONArray("results")
        dataProduct.clear()

        if(list.length()>0){
            lblnotfound_product.visibility = View.GONE
            for (i in 0 until list.length()) {
                dataProduct.add(
                    ModelListProduct(
                        list.getJSONObject(i).getInt("ID"),
                        list.getJSONObject(i).getString("CodeProduct"),
                        list.getJSONObject(i).getString("NameProduct"),
                        list.getJSONObject(i).getString("Description"),
                        list.getJSONObject(i).getDouble("Price"),
                        list.getJSONObject(i).getString("Img"),
                        list.getJSONObject(i).getInt("isDelete"),
                        list.getJSONObject(i).getString("TimeCreated"),
                        list.getJSONObject(i).getString("TimeUpdated")
                    )
                )
            }
        }else lblnotfound_product.visibility = View.VISIBLE
        adapterlist .notifyDataSetChanged()
        swireRefresh_product.isRefreshing = false
    }
}
