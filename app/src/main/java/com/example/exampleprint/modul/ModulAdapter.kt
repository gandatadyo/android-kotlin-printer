package com.example.exampleprint.modul

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exampleprint.R
import java.util.ArrayList

class AdapterProduct(private val listData: ArrayList<ModelListProduct>, val clickListener: (Context, ModelListProduct) -> Unit) : RecyclerView.Adapter<AdapterProduct.CardViewHolder>() {
    val moduleGlobal = ModuleGlobal()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_product, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data: ModelListProduct) {
            val txtCode: TextView = itemView.findViewById(R.id.lblCodeProduct)
            val txtName: TextView = itemView.findViewById(R.id.lblNameProduct)
            val txtPrice: TextView = itemView.findViewById(R.id.lblPriceProduct)
            txtCode.setText(data.CodeProduct)
            txtName.setText(data.NameProduct)
            txtPrice.setText("Harga : "+ data.Price?.let { moduleGlobal.CurrencyFormat(it) })
            itemView.setOnClickListener {clickListener(itemView.context,data)}
        }
    }
}

class AdapterOrder(private val listData: ArrayList<ModelListTrans>, val clickListener: (Context, ModelListTrans) -> Unit) : RecyclerView.Adapter<AdapterOrder.CardViewHolder>() {
    val moduleGlobal = ModuleGlobal()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_order, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data: ModelListTrans) {
            val lblNoOrder: TextView = itemView.findViewById(R.id.lblNoOrder)
            val lblNameCustomer: TextView = itemView.findViewById(R.id.lblNameCustomer)
            val lblTelpCustomer: TextView = itemView.findViewById(R.id.lblTelpCustomer)
            val lblDateOrder: TextView = itemView.findViewById(R.id.lblDateOrder)
            lblNoOrder.setText(data.DocNumber)
            lblNameCustomer.setText(data.CustomerName)
            lblTelpCustomer.setText(data.CustomerTelp)
            lblDateOrder.setText(data.DocDate)
            itemView.setOnClickListener {clickListener(itemView.context,data)}
        }
    }
}