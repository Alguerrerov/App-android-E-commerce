package com.example.e_commerceapp.client

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.R
import com.example.e_commerceapp.databinding.ActivityCatalogoBinding
import com.example.e_commerceapp.databinding.DialogFiltrosBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

// Modelo
data class ProductoCatalogo(
    val nombre:    String,
    val categoria: String,
    val marca:     String,
    val precio:    Double,
    val rating:    String,
    val enStock:   Boolean
)

// Adapter
class CatalogoAdapter(
    private var lista: List<ProductoCatalogo>,
    private val onAgregar: (ProductoCatalogo) -> Unit,
    private val onClick: (ProductoCatalogo) -> Unit
) : RecyclerView.Adapter<CatalogoAdapter.ViewHolder>() {

    companion object {
        var productosOriginales: List<ProductoCatalogo> = emptyList()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre:    TextView = view.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoria)
        val tvMarca:     TextView = view.findViewById(R.id.tvMarca)
        val tvPrecio:    TextView = view.findViewById(R.id.tvPrecio)
        val tvRating:    TextView = view.findViewById(R.id.tvRating)
        val tvStock:     TextView = view.findViewById(R.id.tvStock)
        val btnAgregar:  MaterialButton = view.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_catalogo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]
        holder.tvNombre.text    = p.nombre
        holder.tvCategoria.text = p.categoria
        holder.tvMarca.text     = p.marca
        holder.tvPrecio.text    = "$${String.format("%,.0f", p.precio)}"
        holder.tvRating.text    = p.rating
        holder.tvStock.text     = if (p.enStock) "En stock" else "Agotado"
        holder.tvStock.setTextColor(
            Color.parseColor(if (p.enStock) "#4CAF50" else "#E24B4A")
        )
        holder.btnAgregar.setOnClickListener { onAgregar(p) }
        holder.itemView.setOnClickListener { onClick(p) }
    }

    override fun getItemCount(): Int = lista.size

    fun filtrar(texto: String) {
        lista = if (texto.isEmpty()) productosOriginales
        else productosOriginales.filter {
            it.nombre.contains(texto, ignoreCase = true) ||
                    it.marca.contains(texto, ignoreCase = true) ||
                    it.categoria.contains(texto, ignoreCase = true)
        }
        notifyDataSetChanged()
    }

    fun filtrarPorCategoria(categoria: String) {
        lista = if (categoria == "Todos") productosOriginales
        else productosOriginales.filter { it.categoria == categoria }
        notifyDataSetChanged()
    }

    fun filtrarAvanzado(categoria: String, marca: String, precioMin: Double, precioMax: Double) {
        lista = productosOriginales.filter { p ->
            (categoria.isEmpty() || categoria == "Todas las categorías" || p.categoria == categoria) &&
                    (marca.isEmpty() || marca == "Todas las marcas" || p.marca == marca) &&
                    (precioMin == 0.0 || p.precio >= precioMin) &&
                    (precioMax == 0.0 || p.precio <= precioMax)
        }
        notifyDataSetChanged()
    }
}

class CatalogoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogoBinding
    private lateinit var adapter: CatalogoAdapter

    private val productos = listOf(
        ProductoCatalogo("Filtro de Aceite Mann W610/3",      "Filtros",     "Mann",   27900.0,  "4.7", true),
        ProductoCatalogo("Pastillas de Freno Brembo P85045",  "Frenos",      "Brembo", 89900.0,  "4.8", true),
        ProductoCatalogo("Aceite Mobil 1 5W-30 4L",           "Aceites",     "Mobil",  149900.0, "4.9", true),
        ProductoCatalogo("Filtro de Aire Mann C29005",         "Filtros",     "Mann",   45900.0,  "4.5", true),
        ProductoCatalogo("Batería 12V 60Ah",                   "Baterías",    "Bosch",  320900.0, "4.7", true),
        ProductoCatalogo("Bujía NGK Iridium BKR6EIX",         "Filtros",     "NGK",    12900.0,  "4.6", true),
        ProductoCatalogo("Amortiguador Bosch Trasero",         "Suspensión",  "Bosch",  189900.0, "4.3", false),
        ProductoCatalogo("Disco de Freno Ventilado Delantero", "Frenos",      "Brembo", 120900.0, "4.9", true),
        ProductoCatalogo("Correa de Distribución Gates",       "Motor",       "Gates",  65900.0,  "4.4", true),
        ProductoCatalogo("Líquido de Frenos DOT 4",            "Frenos",      "Castrol",18900.0,  "4.6", true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        // Filtrar por categoría si viene desde el inicio
        val categoriaInicial = intent.getStringExtra("categoria")
        if (!categoriaInicial.isNullOrEmpty()) {
            adapter.filtrarPorCategoria(categoriaInicial)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupBuscador()
        setupChips()
        setupBotones()
    }

    private fun setupRecyclerView() {
        CatalogoAdapter.productosOriginales = productos
        adapter = CatalogoAdapter(
            productos,
            onAgregar = { producto ->
                Toast.makeText(this, "${producto.nombre} agregado al carrito ✓",
                    Toast.LENGTH_SHORT).show()
            },
            onClick = { producto ->
                val intent = Intent(this, DetalleProductoClienteActivity::class.java)
                intent.putExtra("nombre",  producto.nombre)
                intent.putExtra("precio",  "$${String.format("%,.0f", producto.precio)}")
                intent.putExtra("rating",  producto.rating)
                intent.putExtra("enStock", producto.enStock)
                startActivity(intent)
            }
        )
        binding.rvCatalogo.layoutManager = GridLayoutManager(this, 2)
        binding.rvCatalogo.adapter = adapter
        binding.tvResultados.text = "${productos.size} productos encontrados"

        val categoriaInicial = intent.getStringExtra("categoria")
        if (!categoriaInicial.isNullOrEmpty()) {
            adapter.filtrarPorCategoria(categoriaInicial)
        }
    }

    private fun setupBuscador() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filtrar(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupChips() {
        binding.chipTodos.setOnClickListener      { adapter.filtrarPorCategoria("Todos") }
        binding.chipFiltros.setOnClickListener    { adapter.filtrarPorCategoria("Filtros") }
        binding.chipFrenos.setOnClickListener     { adapter.filtrarPorCategoria("Frenos") }
        binding.chipAceites.setOnClickListener    { adapter.filtrarPorCategoria("Aceites") }
        binding.chipBaterias.setOnClickListener   { adapter.filtrarPorCategoria("Baterías") }
        binding.chipSuspension.setOnClickListener { adapter.filtrarPorCategoria("Suspensión") }
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnFiltros.setOnClickListener { mostrarFiltros() }
    }

    private fun mostrarFiltros() {
        val dialog = BottomSheetDialog(this)
        val dialogBinding = DialogFiltrosBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)

        // Categorías
        val categorias = listOf("Todas las categorías", "Filtros", "Frenos",
            "Aceites", "Baterías", "Suspensión", "Motor")
        dialogBinding.spinnerCategoria.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categorias)
        )

        // Marcas
        val marcas = listOf("Todas las marcas", "Mann", "Brembo", "Mobil",
            "Bosch", "NGK", "Gates", "Castrol")
        dialogBinding.spinnerMarca.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, marcas)
        )

        dialogBinding.btnLimpiar.setOnClickListener {
            dialogBinding.spinnerCategoria.setText("")
            dialogBinding.spinnerMarca.setText("")
            dialogBinding.etPrecioMin.setText("")
            dialogBinding.etPrecioMax.setText("")
            adapter.filtrarPorCategoria("Todos")
            dialog.dismiss()
        }

        dialogBinding.btnAplicar.setOnClickListener {
            val categoria = dialogBinding.spinnerCategoria.text.toString()
            val marca     = dialogBinding.spinnerMarca.text.toString()
            val precioMin = dialogBinding.etPrecioMin.text.toString().toDoubleOrNull() ?: 0.0
            val precioMax = dialogBinding.etPrecioMax.text.toString().toDoubleOrNull() ?: 0.0
            adapter.filtrarAvanzado(categoria, marca, precioMin, precioMax)
            dialog.dismiss()
        }

        dialog.show()
    }
}