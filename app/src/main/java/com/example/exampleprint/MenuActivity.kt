package com.example.exampleprint

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.exampleprint.database.DatabaseModul
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnMasterCustomer.setOnClickListener { startActivity(Intent(this,MasterCustomer::class.java)) }
        btnMasterProduct.setOnClickListener { startActivity(Intent(this,MasterProduct::class.java)) }
        btnCreateInvoice.setOnClickListener { startActivity(Intent(this,InputInvoice::class.java)) }
        btnReportTransaction.setOnClickListener { startActivity(Intent(this,ReportTransaction::class.java)) }

        btnTestPrint.setOnClickListener {
            val objIntent = Intent(this,PrintActivity()::class.java)
            objIntent.putExtra("namecustomer","-")
            objIntent.putExtra("daterent","-")
            startActivity(objIntent)
        }
        btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Keluar Akun ?")
            builder.setCancelable(false)
            builder.setPositiveButton("Ya") { dialog, _ ->
                dialog.dismiss()
                Logout()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun Logout() {
        val dbmodul = DatabaseModul()
        dbmodul.ExeqQuery(this, "Delete From dbmadmin")
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan tombol kembali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}
