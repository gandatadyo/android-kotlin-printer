package com.example.exampleprint

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

class MainActivity : AppCompatActivity(),Runnable {

    private val REQUEST_CONNECT_DEVICE = 1
    private val REQUEST_ENABLE_BT = 2
    private val applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothSocket: BluetoothSocket? = null
    var mBluetoothDevice: BluetoothDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnScan.setOnClickListener {ScanDevice()}
        btnPrint.setOnClickListener { PrintDocument() }
        btnDisable.setOnClickListener { DisablePrint() }
    }

    private fun DisablePrint(){
        mBluetoothAdapter?.let {
            it.disable()
        }
    }

    private fun PrintDocument() {
        val t: Thread = object : Thread() {
            override fun run() {
                try {
                    mBluetoothSocket?.let {
                        val os = it.outputStream
                        var BILL = ""
                        BILL = ("                   XXXX MART    \n"
                                + "                   XX.AA.BB.CC.     \n " +
                                "                 NO 25 ABC ABCDE    \n" +
                                "                  XXXXX YYYYYY      \n" +
                                "                   MMM 590019091      \n")
                        BILL = (BILL
                                + "-----------------------------------------------\n")
                        BILL += String.format(
                            "%1$-10s %2$10s %3$13s %4$10s",
                            "Item",
                            "Qty",
                            "Rate",
                            "Totel"
                        )
                        BILL += "\n"
                        BILL = (BILL
                                + "-----------------------------------------------")
                        BILL = "$BILL\n " + String.format(
                            "%1$-10s %2$10s %3$11s %4$10s",
                            "item-001",
                            "5",
                            "10",
                            "50.00"
                        )
                        BILL = "$BILL\n " + String.format(
                            "%1$-10s %2$10s %3$11s %4$10s",
                            "item-002",
                            "10",
                            "5",
                            "50.00"
                        )
                        BILL = "$BILL\n " + String.format(
                            "%1$-10s %2$10s %3$11s %4$10s",
                            "item-003",
                            "20",
                            "10",
                            "200.00"
                        )
                        BILL = "$BILL\n " + String.format(
                            "%1$-10s %2$10s %3$11s %4$10s",
                            "item-004",
                            "50",
                            "10",
                            "500.00"
                        )
                        BILL = (BILL
                                + "\n-----------------------------------------------")
                        BILL = "$BILL\n\n "
                        BILL = "$BILL                   Total Qty:      85\n"
                        BILL = "$BILL                   Total Value:     700.00\n"
                        BILL = (BILL
                                + "-----------------------------------------------\n")
                        BILL = "$BILL\n\n "


                        val printdata = "gandatadyosurya \n Hello World"
                        os.write(BILL.toByteArray())

                        //This is printer specific code you can comment ==== > Start
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

    private fun ScanDevice(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Message1", Toast.LENGTH_SHORT).show()
        } else {
            mBluetoothAdapter?.let {
                if (it.isEnabled) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT)
                } else {
                    ListPairedDevices()
                    val connectIntent = Intent(this@MainActivity, DeviceListActivity::class.java)
                    startActivityForResult(connectIntent,REQUEST_CONNECT_DEVICE)
                }
            }
        }
    }

    private fun ListPairedDevices() {
        mBluetoothAdapter?.let {
            val mPairedDevices = it.bondedDevices
            if (mPairedDevices.size > 0) {
                for (mDevice in mPairedDevices) {
                    Toast.makeText(this,"PairedDevices: " + mDevice.name + "  "+ mDevice.address,Toast.LENGTH_SHORT).show()
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
                ListPairedDevices()
                val connectIntent = Intent(this@MainActivity, DeviceListActivity::class.java)
                startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE)
            } else {
                Toast.makeText(this@MainActivity, "Message", Toast.LENGTH_SHORT).show()
            }
        }
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
            Toast.makeText(this@MainActivity, "DeviceConnected", Toast.LENGTH_SHORT).show()
        }
    }

    fun intToByteArray(value: Int): Byte {
        val b = ByteBuffer.allocate(4).putInt(value).array()
        for (k in b.indices) {
            println(
                "Selva  [" + k + "] = " + "0x" + UnicodeFormatter.byteToHex(b[k])
            )
        }
        return b[3]
    }
}
