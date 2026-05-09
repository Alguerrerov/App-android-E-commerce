package com.example.e_commerceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import com.example.e_commerceapp.databinding.ActivityClientsMainBinding

class ClientsMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientsMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientsMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer()
        setupBotones()

        onBackPressedDispatcher.addCallback(this) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                finish()
            }
        }
    }

    private fun setupDrawer() {
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener { item ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.nav_cli_inicio    -> { /* ya estamos aquí */ }
                R.id.nav_cli_pedidos   -> Toast.makeText(this, "Mis pedidos", Toast.LENGTH_SHORT).show()
                R.id.nav_cli_vehiculo  -> Toast.makeText(this, "Mi vehículo", Toast.LENGTH_SHORT).show()
                R.id.nav_cli_favoritos -> Toast.makeText(this, "Favoritos", Toast.LENGTH_SHORT).show()
                R.id.nav_cli_tiendas   -> Toast.makeText(this, "Tiendas cercanas", Toast.LENGTH_SHORT).show()
                R.id.nav_cli_ofertas   -> Toast.makeText(this, "Ofertas y promociones", Toast.LENGTH_SHORT).show()
                R.id.nav_cli_soporte   -> Toast.makeText(this, "Soporte", Toast.LENGTH_SHORT).show()
                R.id.nav_cli_cerrar    -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
    }

    private fun setupBotones() {
        binding.btnNotif.setOnClickListener {
            Toast.makeText(this, "Notificaciones", Toast.LENGTH_SHORT).show()
        }
        binding.btnVerMas.setOnClickListener {
            Toast.makeText(this, "Ver más productos de frenos", Toast.LENGTH_SHORT).show()
        }

        // Ver todas las categorías → abre el catálogo
        binding.tvVerCategorias.setOnClickListener {
            startActivity(Intent(this, CatalogoActivity::class.java))
        }

        binding.tvVerProductos.setOnClickListener {
            startActivity(Intent(this, CatalogoActivity::class.java))
        }

        binding.cardProducto1.setOnClickListener {
            Toast.makeText(this, "Batería 12V 60Ah", Toast.LENGTH_SHORT).show()
        }
        binding.cardProducto2.setOnClickListener {
            Toast.makeText(this, "Disco de Freno Ventilado", Toast.LENGTH_SHORT).show()
        }

        // Categorías con filtro
        binding.catFiltros.setOnClickListener {
            val intent = Intent(this, CatalogoActivity::class.java)
            intent.putExtra("categoria", "Filtros")
            startActivity(intent)
        }
        binding.catBaterias.setOnClickListener {
            val intent = Intent(this, CatalogoActivity::class.java)
            intent.putExtra("categoria", "Baterías")
            startActivity(intent)
        }
        binding.catSuspension.setOnClickListener {
            val intent = Intent(this, CatalogoActivity::class.java)
            intent.putExtra("categoria", "Suspensión")
            startActivity(intent)
        }
        binding.catAceites.setOnClickListener {
            val intent = Intent(this, CatalogoActivity::class.java)
            intent.putExtra("categoria", "Aceites")
            startActivity(intent)
        }


        //categoria menu botom
        // Bottom Navigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    true
                }
                //categoria menu botom
                R.id.nav_categorias -> {
                    startActivity(Intent(this, CatalogoActivity::class.java))
                    true
                }
                //navbar buscar
                R.id.nav_buscar -> {
                    startActivity(Intent(this, BusquedaActivity::class.java))
                    true
                }
                //carrito menu botom
                R.id.nav_carrito -> {
                    startActivity(Intent(this, CarritoActivity::class.java))
                    true
                }
                //perfil
                R.id.nav_perfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}