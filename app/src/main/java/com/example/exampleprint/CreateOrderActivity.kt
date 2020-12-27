package com.example.exampleprint

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.exampleprint.modul.RestApi
import kotlinx.android.synthetic.main.activity_create_order.*

class CreateOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnInputOrder.setOnClickListener {
            val contentValue = ContentValues()
            contentValue.put("namecustomer",edtCustomer.text.toString())
            contentValue.put("telp",edtTelp.text.toString())
            contentValue.put("address",edtAddress.text.toString())
            contentValue.put("startdaterent",edtStartDate.text.toString())
            contentValue.put("renttime",edtRentTime.text.toString())
            contentValue.put("qty",edtQty.text.toString())
            contentValue.put("notes",edtNotes.text.toString())

            val restApi = RestApi()
            restApi.SendDataOrder(this,contentValue) { response:String -> HandleData(response) }
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

    private fun HandleData(response: String){
        val objIntent = Intent(this,PrintActivity()::class.java)
        objIntent.putExtra("namecustomer",edtCustomer.text.toString())
        objIntent.putExtra("daterent",edtStartDate.text.toString())
        startActivity(objIntent)
    }
}
