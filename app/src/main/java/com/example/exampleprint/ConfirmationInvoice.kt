package com.example.exampleprint

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.exampleprint.modul.ModuleGlobal
import com.example.exampleprint.utils.DeviceListActivity
import com.example.exampleprint.utils.UnicodeFormatter
import kotlinx.android.synthetic.main.activity_confirmation_invoice.*
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

class ConfirmationInvoice : AppCompatActivity(),Runnable {
    private var doubleBackToExitPressedOnce = false

    private val modulGlobal = ModuleGlobal()
    // variable for print
    private val applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val REQUEST_CONNECT_DEVICE = 1
    private val REQUEST_ENABLE_BT = 2
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothSocket: BluetoothSocket? = null
    private var mBluetoothDevice: BluetoothDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation_invoice)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val docnumber = intent.getStringExtra("docnumber")
        lblDocNumber_Confrimation.text = docnumber
        btnPrintInvoice.setOnClickListener {
            if(!ListPairedDevices()){
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Media Printer tidak ditemukan, Hubungkan ?")
                builder.setCancelable(false)
                builder.setPositiveButton("Ya") { dialog, _ ->
                    dialog.dismiss()
                    ScanDevice()
                }
                builder.setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }else{
                GetDataPrint(docnumber)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this,MenuActivity::class.java))
                finishAffinity()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            startActivity(Intent(this,MenuActivity::class.java))
            finishAffinity()
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan tombol kembali lagi untuk keluar dari menu ini", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    // PROCEDURE PRINTING
    private fun ScanDevice(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooh Not Found", Toast.LENGTH_SHORT).show()
        } else {
            mBluetoothAdapter?.let {
                if (it.isEnabled) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT)
                } else {
                    val connectIntent = Intent(this@ConfirmationInvoice, DeviceListActivity::class.java)
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE)
                }
            }
        }
    }

    override fun onActivityResult(mRequestCode: Int, mResultCode: Int, mDataIntent: Intent?) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent)

        when (mRequestCode) {
            REQUEST_CONNECT_DEVICE -> if (mResultCode == Activity.RESULT_OK) {
                val mExtra = mDataIntent!!.extras
                val mDeviceAddress = mExtra!!.getString("DeviceAddress")
//                Log.v(MainActivity.TAG, "Coming incoming address $mDeviceAddress")
                mBluetoothAdapter?.let {
                    mBluetoothDevice = it.getRemoteDevice(mDeviceAddress)
                }
//                mBluetoothConnectProgressDialog = ProgressDialog.show(this,"Connecting...", mBluetoothDevice.getName() + " : "+ mBluetoothDevice.getAddress(), true, false)
                val mBlutoothConnectThread = Thread(this)
                mBlutoothConnectThread.start()
                // pairToDevice(mBluetoothDevice); This method is replaced by
                // progress dialog with thread
            }
            REQUEST_ENABLE_BT -> if (mResultCode == Activity.RESULT_OK) {
                val connectIntent = Intent(this@ConfirmationInvoice, DeviceListActivity::class.java)
                startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE)
            } else {
                Toast.makeText(this@ConfirmationInvoice, "Message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ListPairedDevices():Boolean {
        var falgpaired = false
        mBluetoothAdapter?.let {
            val mPairedDevices = it.bondedDevices
            if (mPairedDevices.size > 0) {
                for (mDevice in mPairedDevices) {
                    falgpaired = true
//                    Toast.makeText(this,"PairedDevices: " + mDevice.name + "  "+ mDevice.address,Toast.LENGTH_SHORT).show()
                }
            }
        }
        return falgpaired
    }

    private fun GetDataPrint(docnumber:String){
        if(!modulGlobal.isNetworkAvailable(this)) {
            Toast.makeText(this,getString(R.string.resulterrorconnection), Toast.LENGTH_LONG).show()
        }else {
            val queue = Volley.newRequestQueue(this)
            val url =  modulGlobal.GetIPServer(this) + "/api/get_printdata"
            val postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener<String> { response ->
                    PrintOutput(response)
                }, Response.ErrorListener {
                    Toast.makeText(this,getString(R.string.resulterror), Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["docnumber"] = docnumber
                    return params
                }
            }
            queue.add(postRequest)
        }
    }

    private fun PrintOutput(datafileprint:String){
        val t: Thread = object : Thread() {
            override fun run() {
                try {
                    mBluetoothSocket?.let {
                        val os = it.outputStream
                        os.write(datafileprint.toByteArray())

                        // Setting height
                        val gs = 29
                        os.write(intToByteArray(gs).toInt())
                        val h = 104
                        os.write(intToByteArray(h).toInt())
                        val n = 162
                        os.write(intToByteArray(n).toInt())
                        // Setting Width
                        val gs_width = 29
                        os.write(intToByteArray(gs_width).toInt())
                        val w = 119
                        os.write(intToByteArray(w).toInt())
                        val n_width = 2
                        os.write(intToByteArray(n_width).toInt())
                    }
                } catch (e: Exception) {
//                    Log.e("MainActivity", "Exe ", e)
                }
            }
        }
        t.start()
    }

    private fun intToByteArray(value: Int): Byte {
        val b = ByteBuffer.allocate(4).putInt(value).array()
        for (k in b.indices) {
            println(
                "Selva  [" + k + "] = " + "0x" + UnicodeFormatter.byteToHex(b[k])
            )
        }
        return b[3]
    }

    override fun run() {
        try {
            mBluetoothDevice?.let {
                mBluetoothSocket = it.createRfcommSocketToServiceRecord(applicationUUID)
            }
            mBluetoothAdapter?.cancelDiscovery()
            mBluetoothSocket?.connect()
            mHandler.sendEmptyMessage(0)
        } catch (eConnectException: IOException) {
//            Log.d(MainActivity.TAG, "CouldNotConnectToSocket", eConnectException)
            try {
                mBluetoothSocket?.close()
//                Log.d(MainActivity.TAG, "SocketClosed")
            } catch (ex: IOException) {
//                Log.d(MainActivity.TAG, "CouldNotCloseSocket")
            }
            return
        }
    }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            Toast.makeText(this@ConfirmationInvoice, "DeviceConnected", Toast.LENGTH_SHORT).show()
        }
    }
}