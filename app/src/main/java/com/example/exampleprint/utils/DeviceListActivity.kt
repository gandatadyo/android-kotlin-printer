package com.example.exampleprint.utils

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.exampleprint.R
import kotlinx.android.synthetic.main.activity_device_list.*

class DeviceListActivity : AppCompatActivity() {
    private var mBluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setResult(Activity.RESULT_CANCELED)
        paired_devices.onItemClickListener = mDeviceClickListener

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mBluetoothAdapter?.let {
            val mPairedDevices = it.bondedDevices
            if (mPairedDevices.size > 0) {
                val arrayList=ArrayList<String>()
                title_paired_devices.text = ""
                for (mDevice in mPairedDevices) {
                    val nameitem = mDevice.name + "\n" + mDevice.address
                    arrayList.add(nameitem)
                }
                val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList)
                paired_devices.adapter = adapter
            } else {
                title_paired_devices.text = "None Paired"
                val arrayList=ArrayList<String>()
                val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList)
                paired_devices.adapter = adapter
            }
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

    private val mDeviceClickListener = OnItemClickListener { mAdapterView, mView, mPosition, mLong ->
        try {
            mBluetoothAdapter!!.cancelDiscovery()
            val mDeviceInfo = (mView as TextView).text.toString()
            val mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length - 17)
            Toast.makeText(this,"Device_Address $mDeviceAddress",Toast.LENGTH_SHORT).show()
            val mBundle = Bundle()
            mBundle.putString("DeviceAddress", mDeviceAddress)
            val mBackIntent = Intent()
            mBackIntent.putExtras(mBundle)
            setResult(Activity.RESULT_OK, mBackIntent)
            finish()
        } catch (ex: Exception) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBluetoothAdapter?.let {
            it.cancelDiscovery()
        }
    }
}
