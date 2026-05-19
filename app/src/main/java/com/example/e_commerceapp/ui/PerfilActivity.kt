package com.example.e_commerceapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.auth.LoginActivity
import com.example.e_commerceapp.client.MiDireccionActivity
import com.example.e_commerceapp.client.MisDatosPagoActivity
import com.example.e_commerceapp.client.MisPedidosActivity
import com.example.e_commerceapp.databinding.ActivityPerfilBinding

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //perfil
        binding.tvEditarPerfil.setOnClickListener {
            startActivity(Intent(this, InformacionPersonalActivity::class.java))
        }
        //pedidos
        binding.itemMisPedidos.setOnClickListener {
            startActivity(Intent(this, MisPedidosActivity::class.java))
        }
        //direccion
        binding.itemMiDireccion.setOnClickListener {
            startActivity(Intent(this, MiDireccionActivity::class.java))
        }
        //datos de pago
        binding.itemDatosPago.setOnClickListener {
            startActivity(Intent(this, MisDatosPagoActivity::class.java))
        }
        //configuracion
        binding.itemConfiguracion.setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }

        binding.itemCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}