package com.example.exampleprint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.exampleprint.database.DatabaseModul
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnMaster.setOnClickListener {
            startActivity(Intent(this,MasterItemActivity::class.java))
        }
        btnCreateOrder.setOnClickListener {
            startActivity(Intent(this,CreateOrderActivity::class.java))
        }
        btnHistoryOrder.setOnClickListener {
            startActivity(Intent(this,HistoryOrderActivity::class.java))
        }
        btnTestPrint.setOnClickListener {
            val objIntent = Intent(this,PrintActivity()::class.java)
            objIntent.putExtra("namecustomer","-")
            objIntent.putExtra("daterent","-")
            startActivity(objIntent)
        }
        btnLogout.setOnClickListener {
            Logout()
        }
    }

    private fun Logout() {
        val dbmodul = DatabaseModul()
        dbmodul.ExeqQuery(this, "Delete From dbmadmin")
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }
}
