package com.example.exampleprint

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exampleprint.database.DatabaseModul
import com.example.exampleprint.modul.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_input_invoice_product.*
import kotlinx.android.synthetic.main.view_inputdp.view.*
import kotlinx.android.synthetic.main.view_inputqty.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class InputInvoiceProduct : AppCompatActivity()  {
    private var amountopen : Double = 0.0
    private var amountsettled : Double = 0.0
    private var amountbalance : Double = 0.0
    private var amountdayrent : Int = 0

    private val modulGlobal = ModuleGlobal()
    private var dataProduct =  arrayListOf<ModelInputProduct>()
    private val adapterTrans = AdapterInputProduct(dataProduct) { partItem: ModelInputProduct, position: Int -> ChooseProduct(
        partItem,
        position
    ) }
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var contentValueGlobal:ContentValues

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_invoice_product)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val param = this.intent.getParcelableExtra("params") as ContentValues
        contentValueGlobal = param
        lblName_InputInvoice.text = param.getAsString("namecustomer")
        lblEmail_InputInvoice.text = param.getAsString("email")
        lblTelp_InputInvoice.text = param.getAsString("telp")
        lblAddress_InputInvoice.text = param.getAsString("address")

        lblDateRent_InputInvoice.text = param.getAsString("rentdate")
        lblAmountDayRent_InputInvoice.text = param.getAsString("amountdayrent")+" Hari"
        amountdayrent = 0

        linearLayoutManager = LinearLayoutManager(this)
        recycler_InputInvoiceProduct.layoutManager = linearLayoutManager
        recycler_InputInvoiceProduct.adapter = adapterTrans

        btnSaveInvoiceCheckout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Buat Transaksi ?")
            builder.setCancelable(false)
            builder.setPositiveButton("Ya") { dialog, _ ->
                SendDataCreateInvoice()
                dialog.dismiss()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        btnDP_InputInvoice.setOnClickListener {
            if(amountopen==0.0){
                Toast.makeText(this, "Tentukan barang terlebih dahulu", Toast.LENGTH_LONG).show()
            }else {
                val view = LayoutInflater.from(this).inflate(R.layout.view_inputdp, null)
                val dialogDP = BottomSheetDialog(this)
                view.edtDP.setText("0")
                view.btnSaveDP.setOnClickListener {
                    if (view.edtDP.text.toString() == "") {
                        Toast.makeText(this, "DP tidak boleh kosong", Toast.LENGTH_LONG).show()
                    } else {
                        dialogDP.dismiss()
                        amountsettled = view.edtDP.text.toString().toDouble()
                        CalculateData()
                    }
                }
                dialogDP.setContentView(view)
                dialogDP.show()
            }
        }
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
            Toast.makeText(this, getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/list_product"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_GetDataProduct(response)
                }, Response.ErrorListener {
                    Toast.makeText(this, getString(R.string.resulterror), Toast.LENGTH_LONG).show()
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

    private fun Handle_GetDataProduct(response: String){
        val responseObject = JSONObject(response)
        val list: JSONArray = responseObject.getJSONArray("results")
        dataProduct.clear()
        if(list.length()>0){
            for (i in 0 until list.length()) {
                dataProduct.add(
                    ModelInputProduct(
                        list.getJSONObject(i).getString("ID"),
                        list.getJSONObject(i).getString("NameProduct"),
                        0,
                        list.getJSONObject(i).getDouble("Price"),
                        (list.getJSONObject(i).getDouble("Price") * 0)
                    )
                )
            }
        }
        CalculateData()
    }

    private fun ChooseProduct(data: ModelInputProduct, position: Int){
        ShowModalQty(position)
    }

    private fun CalculateData(){
        var amounttotal = 0.0
        for (i in 0 until dataProduct.size){
            amounttotal += dataProduct[i].Total!!
        }
        amountopen = amounttotal
        amountbalance = amountopen - amountsettled
        lblTotal_InputInvoice.text = modulGlobal.CurrencyFormat(amountopen)
        lblDP_InputInvoice.text = modulGlobal.CurrencyFormat(amountsettled)
        lblBalance_InputInvoice.text = modulGlobal.CurrencyFormat(amountbalance)
        adapterTrans.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun ShowModalQty(position: Int){
        val view = LayoutInflater.from(this).inflate(R.layout.view_inputqty, null)
        val dialogBarcode = BottomSheetDialog(this)
        view.edtQty.setText(dataProduct[position].Qty.toString())
        view.lblNameProductQty.text = dataProduct[position].NameProduct.toString()
        view.lblPriceProductQty.text = "Harga Sewa Rp. "+modulGlobal.CurrencyFormat(dataProduct[position].Price!!)
        view.btnAddQty.setOnClickListener {
            val qty:Int = view.edtQty.text.toString().toInt()+1
            view.edtQty.setText(qty.toString())
        }
        view.btnMinusQty.setOnClickListener {
            val qty:Int = view.edtQty.text.toString().toInt()-1
            view.edtQty.setText(qty.toString())
        }
        view.btnSaveQty.setOnClickListener {
            dialogBarcode.dismiss()
            val total: Double = (dataProduct[position].Price!! *  view.edtQty.text.toString().toInt())
            dataProduct[position].Qty = view.edtQty.text.toString().toInt()
            dataProduct[position].Total = total
            CalculateData()
        }
        dialogBarcode.setContentView(view)
        dialogBarcode.show()
    }

    private fun CheckQtyNothing():Boolean{
        var flagstatus = false
        for (i in 0 until dataProduct.size){
            if(dataProduct[i].Qty!! >0){
                flagstatus=true
            }
        }
        return flagstatus
    }

    private fun SendDataCreateInvoice(){
        if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else if(!CheckQtyNothing()) {
            Toast.makeText(this,"Tidak ada produk yang dipesan", Toast.LENGTH_LONG).show()
        }else {
            val dataprofile :ModelListAdmin = DatabaseModul().GetDataProfle(this)

            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/create_transaction"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    Handle_SendDataCreateInvoice(response)
                }, Response.ErrorListener {
                    Toast.makeText(this,getString(R.string.resulterror), Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["idadmin"] = dataprofile.ID.toString()

                    params["name"] = contentValueGlobal.getAsString("namecustomer")
                    params["email"] = contentValueGlobal.getAsString("email")
                    params["telp"] = contentValueGlobal.getAsString("telp")
                    params["address"] = contentValueGlobal.getAsString("address")

                    params["rentdate"] = contentValueGlobal.getAsString("rentdate")
                    params["amountdayrent"] = contentValueGlobal.getAsString("amountdayrent")
                    params["notes"] = contentValueGlobal.getAsString("rentdate")

                    params["total"] = amountopen.toString()
                    params["dp"] = amountsettled.toString()
                    params["grandtotal"] = amountbalance.toString()

                    params["dataset"] = Gson().toJson(dataProduct)
                    return params
                }
            }
            queue.add(postRequest)
        }
    }

    private fun Handle_SendDataCreateInvoice(response: String){
        val status = JSONObject(response).getString("status")
        val message = JSONObject(response).getString("message")
        if(status=="true"){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Transaksi Berhasil Dibuat. Cetak Nota ?")
            builder.setCancelable(false)
            builder.setPositiveButton("Ya") { dialog, _ ->
                dialog.dismiss()
                val objIntent = Intent(this, ConfirmationInvoice::class.java)
                objIntent.putExtra("docnumber", JSONObject(response).getString("docnumber"))
                startActivity(objIntent)
                finishAffinity()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, MenuActivity::class.java))
                finishAffinity()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }else{
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        }
    }
}