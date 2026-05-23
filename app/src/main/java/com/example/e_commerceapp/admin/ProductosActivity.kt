package com.example.e_commerceapp.admin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.admin.AgregarProductoActivity
import com.example.e_commerceapp.model.Producto
import com.example.e_commerceapp.model.ProductosAdapter
import com.example.e_commerceapp.databinding.ActivityProductosBinding
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import com.example.e_commerceapp.model.ProductoConStock
import io.github.jan.supabase.postgrest.query.Columns
class ProductosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductosBinding
    private lateinit var adapter: ProductosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupBuscador()
        setupTabs()
        setupBotones()
        cargarProductos()
    }

    private fun setupRecyclerView() {
        adapter = ProductosAdapter(emptyList()) { producto ->
            val intent = Intent(this, DetalleProductoActivity::class.java)
            intent.putExtra("id",            producto.id)
            intent.putExtra("nombre",        producto.nombre)
            intent.putExtra("categoria",     producto.categoria)
            intent.putExtra("precio",        producto.precio)
            intent.putExtra("stock",         producto.stock)
            intent.putExtra("estado",        producto.estado)
            intent.putExtra("marca",         producto.marca)
            intent.putExtra("descripcion",   producto.descripcion)
            intent.putExtra("compatibilidad",producto.compatibilidad)
            intent.putExtra("dimensiones",   producto.dimensiones)
            intent.putExtra("peso",          producto.peso)
            intent.putExtra("codigo",        producto.codigo)
            intent.putExtra("vendedor",      producto.vendedor)
            intent.putExtra("tienda",        producto.tienda)
            intent.putExtra("foto", producto.foto)
            startActivity(intent)
        }
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adapter
    }

    private fun cargarProductos() {
        lifecycleScope.launch {
            try {
                android.util.Log.d("PRODUCTOS", "Cargando productos...")

                val productosDB = SupabaseClient.client
                    .postgrest["Productos"]
                    .select(columns = Columns.raw("*, Stock_tiendas(*)"))
                    .decodeList<ProductoConStock>()

                android.util.Log.d("PRODUCTOS", "Total: ${productosDB.size}")

                val productos = productosDB.map { p ->
                    val stockRow = p.stockTienda.firstOrNull()
                    Producto(
                        id             = p.id,
                        nombre         = p.nombre,
                        categoria      = p.categoria,
                        descripcion    = p.descripcion,
                        precio         = "S/ ${p.precio}",
                        stock          = (stockRow?.stock ?: 0).toString(),
                        marca          = p.marca,
                        compatibilidad = p.compatibilidad ?: "",   // null → string vacío
                        dimensiones    = p.dimensiones    ?: "",
                        peso           = p.peso           ?: "",
                        codigo         = p.codigo         ?: "",
                        foto           = p.foto           ?: "",
                        estado = if (stockRow?.estado == "Activo") "Activo" else "Inactivo",
                        vendedor       = "AutoParts",
                        tienda         = "AutoParts"
                    )
                }

                val total     = productos.size
                val activos   = productos.count { it.estado == "Activo" }
                val inactivos = productos.count { it.estado == "Inactivo" }

                runOnUiThread {
                    ProductosAdapter.productosOriginales = productos
                    adapter.actualizarLista(productos)
                    binding.tabTodos.text     = "Todos ($total)"
                    binding.tabActivos.text   = "Activos ($activos)"
                    binding.tabInactivos.text = "Inactivos ($inactivos)"
                }

            } catch (e: Exception) {
                android.util.Log.e("PRODUCTOS", "Error: ${e.message}")
                runOnUiThread {
                    android.widget.Toast.makeText(this@ProductosActivity,
                        "Error al cargar: ${e.message}",
                        android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupBuscador() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { adapter.filtrar(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupTabs() {
        binding.tabTodos.setOnClickListener {
            adapter.filtrarPorEstado("Todos")
            setTabActivo(binding.tabTodos)
        }
        binding.tabActivos.setOnClickListener {
            adapter.filtrarPorEstado("Activo")
            setTabActivo(binding.tabActivos)
        }
        binding.tabInactivos.setOnClickListener {
            adapter.filtrarPorEstado("Inactivo")
            setTabActivo(binding.tabInactivos)
        }
    }

    private fun setTabActivo(tab: TextView) {
        listOf(binding.tabTodos, binding.tabActivos, binding.tabInactivos).forEach {
            it.setTextColor(Color.parseColor("#8899AA"))
        }
        tab.setTextColor(Color.parseColor("#1E90FF"))
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnNuevoProducto.setOnClickListener {
            startActivity(Intent(this, AgregarProductoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }
}