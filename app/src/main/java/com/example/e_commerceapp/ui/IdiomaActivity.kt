package com.example.e_commerceapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityIdiomaBinding

class IdiomaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdiomaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdiomaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.itemEspanol.setOnClickListener {
            binding.radioEspanol.isChecked   = true
            binding.radioEnglish.isChecked   = false
            binding.radioPortugues.isChecked = false
            Toast.makeText(this, "Español seleccionado", Toast.LENGTH_SHORT).show()
        }
        binding.itemEnglish.setOnClickListener {
            binding.radioEspanol.isChecked   = false
            binding.radioEnglish.isChecked   = true
            binding.radioPortugues.isChecked = false
            Toast.makeText(this, "English selected", Toast.LENGTH_SHORT).show()
        }
        binding.itemPortugues.setOnClickListener {
            binding.radioEspanol.isChecked   = false
            binding.radioEnglish.isChecked   = false
            binding.radioPortugues.isChecked = true
            Toast.makeText(this, "Português selecionado", Toast.LENGTH_SHORT).show()
        }
    }
}