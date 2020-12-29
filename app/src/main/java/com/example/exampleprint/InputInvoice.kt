package com.example.exampleprint

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exampleprint.modul.AdapterCustomer
import com.example.exampleprint.modul.ModelCustomer
import com.example.exampleprint.modul.ModuleGlobal
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_input_invoice.*
import kotlinx.android.synthetic.main.view_search_customer.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class InputInvoice : AppCompatActivity() {
    val modulGlobal= ModuleGlobal()
    // variable for search customer
    private lateinit var viewSearchCustomer:View
    private lateinit var dialogSearchCustomer:BottomSheetDialog
    private var dataCustomer =  arrayListOf<ModelCustomer>()
    private val adapterSearchCustomer = AdapterCustomer(dataCustomer) { partItem : ModelCustomer -> ChooseCustomer(partItem) }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_invoice)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // init search customer
        viewSearchCustomer = LayoutInflater.from(this).inflate(R.layout.view_search_customer, null)
        dialogSearchCustomer = BottomSheetDialog(this)
        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(this)
        viewSearchCustomer.recycler_search_customer.layoutManager = linearLayoutManager
        viewSearchCustomer.recycler_search_customer.adapter = adapterSearchCustomer
        dialogSearchCustomer.setContentView(viewSearchCustomer)

        btnSaveInvoice.setOnClickListener {
            val name = edtNameCustomer_InputInvoice.text.toString()
            val email = edtEmail_InputInvoice.text.toString()
            val telp = edtTelp_InputInvoice.text.toString()
            val address = edtAddress_InputInvoice.text.toString()
            val rentdate = edtRentDate_InputInvoice.text.toString()
            val amountdayrent = edtAmountDayRent_InputInvoice.text.toString()
            val notes = edtNotes_InputInvoice.text.toString()

            if((name=="") || (email=="") || (telp=="") || (address=="") || (rentdate=="") || (amountdayrent=="")  ) {
                Toast.makeText(this, "Mohon isi data dengan lengkap", Toast.LENGTH_LONG).show()
            }else {
                val params = ContentValues()
                params.put("namecustomer", name)
                params.put("email",email)
                params.put("telp", telp)
                params.put("address", address)

                params.put("rentdate", rentdate)
                params.put("amountdayrent", amountdayrent)
                params.put("notes", notes)

                val objIntent = Intent(this, InputInvoiceProduct::class.java)
                objIntent.putExtra("params", params)
                startActivity(objIntent)
            }
        }

        edtRentDate_InputInvoice.setOnClickListener {
            val c = Calendar.getInstance()
            if(edtRentDate_InputInvoice.text.toString()!="") {
                val mydate = SimpleDateFormat("yyyy-MM-dd").parse(edtRentDate_InputInvoice.text.toString())
                c.time = mydate
            }
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                edtRentDate_InputInvoice.setText(sdf.format(cal.time))
            }, year, month, day).show()
        }

        btnSearchCustomer.setOnClickListener {
            GetSearchCustomer()
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

    private fun GetSearchCustomer(){
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
        adapterSearchCustomer.notifyDataSetChanged()
        dialogSearchCustomer.show()
    }

    private fun ChooseCustomer(data: ModelCustomer){
        dialogSearchCustomer.dismiss()
        edtNameCustomer_InputInvoice.setText(data.NameCustomer)
        edtEmail_InputInvoice.setText(data.Email)
        edtTelp_InputInvoice.setText(data.Telp)
        edtAddress_InputInvoice.setText(data.Address)
    }
}