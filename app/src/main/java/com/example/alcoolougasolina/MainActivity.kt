package com.example.alcoolougasolina

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {
  private lateinit var alcoholPrice: EditText
  private lateinit var gasolinePrice: EditText
  private lateinit var result: TextView
  private lateinit var switch: SwitchCompat
  private lateinit var sharedPreferences: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    alcoholPrice = findViewById(R.id.alcool_label)
    gasolinePrice = findViewById(R.id.gasolina_label)
    result = findViewById(R.id.resultado)
    switch = findViewById(R.id.toggleSwitch)

    sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    alcoholPrice.setText(sharedPreferences.getString("alcoholPrice", ""))
    gasolinePrice.setText(sharedPreferences.getString("gasolinePrice", ""))
    result.setText(sharedPreferences.getString("result", ""))
    switch.isChecked = sharedPreferences.getBoolean("switch", false)

    val textWatcher =
        object : TextWatcher {
          override fun afterTextChanged(s: Editable?) {
            calculate()
          }

          override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

          override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

    switch.setOnCheckedChangeListener { _, _ -> calculate() }

    alcoholPrice.addTextChangedListener(textWatcher)
    gasolinePrice.addTextChangedListener(textWatcher)
  }

  override fun onPause() {
    super.onPause()
    saveData()
  }

  override fun onStop() {
    super.onStop()
    saveData()
  }

  override fun onDestroy() {
    super.onDestroy()
    saveData()
  }

  private fun saveData() {
    val editor = sharedPreferences.edit()
    editor.putString("alcoholPrice", alcoholPrice.text.toString())
    editor.putString("gasolinePrice", gasolinePrice.text.toString())
    editor.putString("result", result.text.toString())
    editor.putBoolean("switch", switch.isChecked)
    editor.apply()
  }

  private fun calculate() {
    val alcohol = alcoholPrice.text.toString().toDoubleOrNull()
    val gasoline = gasolinePrice.text.toString().toDoubleOrNull()
    if (alcohol != null && gasoline != null) {
      val ratio = if (switch.isChecked) 0.7 else 0.75
      result.text =
          if (alcohol <= gasoline * ratio) getString(R.string.alcohol_profitable)
          else getString(R.string.gasoline_profitable)
    }
  }
}
