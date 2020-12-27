package com.example.exampleprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.exampleprint.modul.ModuleGlobal
import kotlinx.android.synthetic.main.activity_testing.*

class TestingActivity : AppCompatActivity() {
    val moduleGlobal = ModuleGlobal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        edtIP_Console.setText(moduleGlobal.GetSharedPreference(this,"ipsharedpreference"))
        btnSave_Console.setOnClickListener {
            moduleGlobal.SaveSharedPreference(this,"ipsharedpreference",edtIP_Console.text.toString())
            Toast.makeText(this,"Saved", Toast.LENGTH_SHORT).show()
        }
        btnReload_Console.setOnClickListener {
            edtIP_Console.setText(moduleGlobal.GetSharedPreference(this,"ipsharedpreference"))
            Toast.makeText(this,"Done", Toast.LENGTH_SHORT).show()
        }

    }
}
