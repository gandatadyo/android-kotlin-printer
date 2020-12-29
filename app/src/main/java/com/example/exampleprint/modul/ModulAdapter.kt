package com.example.exampleprint.modul

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.exampleprint.R
import java.util.ArrayList

class AdapterCustomer(private val listData: ArrayList<ModelCustomer>, val clickListener: (ModelCustomer) -> Unit) : RecyclerView.Adapter<AdapterCustomer.CardViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_customer,viewGroup,false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data:ModelCustomer) {
            val card: CardView = itemView.findViewById(R.id.card_ItemCustomer)
            val lblName: TextView = itemView.findViewById(R.id.lblName_ItemCustomer)
            val lblEmail: TextView = itemView.findViewById(R.id.lblEmail_ItemCustomer)
            val lblTelp: TextView = itemView.findViewById(R.id.lblTelp_ItemCustomer)
            val lblAddress: TextView = itemView.findViewById(R.id.lblAddress_ItemCustomer)
            lblName.text = "Nama Customer : "+data.NameCustomer
            lblEmail.text = "Email : "+data.Email
            lblTelp.text = "Telp : "+data.Telp
            lblAddress.text = "Address : "+data.Address
            card.setOnClickListener { clickListener(data)}
        }
    }
}

class AdapterProduct(private val listData: ArrayList<ModelProduct>, val clickListener: (ModelProduct) -> Unit) : RecyclerView.Adapter<AdapterProduct.CardViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_product,viewGroup,false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data:ModelProduct) {
            val card: CardView = itemView.findViewById(R.id.card_ItemProduct)
            val lblName: TextView = itemView.findViewById(R.id.lblName_ItemProduct)
            val lblPrice: TextView = itemView.findViewById(R.id.lbllPrice_ItemProduct)
            lblName.text = "Nama Product :"+data.NameProduct
            lblPrice.text = "Harga :"+data.Price
            card.setOnClickListener { clickListener(data)}
        }
    }
}

class AdapterInputProduct(private val listData: ArrayList<ModelInputProduct>, val clickListener: (ModelInputProduct,Int) -> Unit) : RecyclerView.Adapter<AdapterInputProduct.CardViewHolder>() {
    val moduleGlobal = ModuleGlobal()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_product_qty,viewGroup,false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listData[position],position)
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data: ModelInputProduct, position: Int) {
            val card: CardView = itemView.findViewById(R.id.card_ItemProductQty)
            val lblName: TextView = itemView.findViewById(R.id.lblName_ItemProductQty)
            val lblPriceQty: TextView = itemView.findViewById(R.id.lbllPriceQty_ItemProductQty)
            lblName.text = data.NameProduct
//            val total: Double = (data.Price!! * data.Qty!!)
            lblPriceQty.text = "${moduleGlobal.CurrencyFormat(data.Price!!)} x ${data.Qty} Unit = ${moduleGlobal.CurrencyFormat(data.Total!!)}"
            card.setOnClickListener { clickListener(data,position)}
        }
    }
}

class AdapterTransaction(private val listData: ArrayList<ModelListTransaction>, val clickListener: (ModelListTransaction) -> Unit) : RecyclerView.Adapter<AdapterTransaction.CardViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_transaction,viewGroup,false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data:ModelListTransaction) {
            val card: CardView = itemView.findViewById(R.id.card_trans)
            val lbldocnumber: TextView = itemView.findViewById(R.id.lblDocNumber_trans)
            val lblcustomer: TextView = itemView.findViewById(R.id.lblNameCustomer_trans)
            val lblgrantotal: TextView = itemView.findViewById(R.id.lblGrandTotal_trans)
            val lbltimecreate: TextView = itemView.findViewById(R.id.lblTimeCreate_trans)
            lbldocnumber.text = data.DocNumber
            lblcustomer.text = data.NameCustomer
            lblgrantotal.text = data.GrandTotal
            lbltimecreate.text = data.TimeCreated
            card.setOnClickListener { clickListener(data)}
        }
    }
}