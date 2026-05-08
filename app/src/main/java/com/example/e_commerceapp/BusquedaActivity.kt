package com.example.e_commerceapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.databinding.ActivityBusquedaBinding
import com.example.e_commerceapp.databinding.DialogFiltrosBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

// Adapter búsqueda
class BusquedaAdapter(
    private var lista: List<ProductoCatalogo>,
    private val onClick: (ProductoCatalogo) -> Unit
) : RecyclerView.Adapter<BusquedaAdapter.ViewHolder>() {

    companion object {
        var productosOriginales: List<ProductoCatalogo> = emptyList()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre:  TextView = view.findViewById(R.id.tvNombre)
        val tvMarca:   TextView = view.findViewById(R.id.tvMarca)
        val tvPrecio:  TextView = view.findViewById(R.id.tvPrecio)
        val tvRating:  TextView = view.findViewById(R.id.tvRating)
        val tvOferta:  TextView = view.findViewById(R.id.tvOferta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_busqueda, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]
        holder.tvNombre.text  = p.nombre
        holder.tvMarca.text   = p.marca
        holder.tvPrecio.text  = "$${String.format("%,.0f", p.precio)}"
        holder.tvRating.text  = p.rating
        holder.itemView.setOnClickListener { onClick(p) }
    }

    override fun getItemCount(): Int = lista.size

    fun buscar(texto: String) {
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

    fun filtrarAvanzado(categoria: String, marca: String,
                        precioMin: Double, precioMax: Double) {
        lista = productosOriginales.filter { p ->
            (categoria.isEmpty() || categoria == "Todas las categorías" || p.categoria == categoria) &&
                    (marca.isEmpty() || marca == "Todas las marcas" || p.marca == marca) &&
                    (precioMin == 0.0 || p.precio >= precioMin) &&
                    (precioMax == 0.0 || p.precio <= precioMax)
        }
        notifyDataSetChanged()
    }
}

class BusquedaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusquedaBinding
    private lateinit var adapter: BusquedaAdapter

    private val productos = listOf(
        ProductoCatalogo("Filtro de Aceite Mann W610/3",      "Filtros",    "Mann",    27900.0,  "4.7", true),
        ProductoCatalogo("Pastillas de Freno Brembo P85045",  "Frenos",     "Brembo",  89900.0,  "4.8", true),
        ProductoCatalogo("Aceite Mobil 1 5W-30 4L",           "Aceites",    "Mobil",   149900.0, "4.9", true),
        ProductoCatalogo("Filtro de Aire Mann C29005",         "Filtros",    "Mann",    45900.0,  "4.5", true),
        ProductoCatalogo("Batería 12V 60Ah",                   "Baterías",   "Bosch",   320900.0, "4.7", true),
        ProductoCatalogo("Bujía NGK Iridium BKR6EIX",         "Filtros",    "NGK",     12900.0,  "4.6", true),
        ProductoCatalogo("Amortiguador Bosch Trasero",         "Suspensión", "Bosch",   189900.0, "4.3", false),
        ProductoCatalogo("Disco de Freno Ventilado Delantero", "Frenos",     "Brembo",  120900.0, "4.9", true),
        ProductoCatalogo("Correa de Distribución Gates",       "Motor",      "Gates",   65900.0,  "4.4", true),
        ProductoCatalogo("Líquido de Frenos DOT 4",            "Frenos",     "Castrol", 18900.0,  "4.6", true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupBuscador()
        setupChips()
        setupBotones()
    }

    private fun setupRecyclerView() {
        BusquedaAdapter.productosOriginales = productos
        adapter = BusquedaAdapter(productos) { producto ->
            val intent = Intent(this, DetalleProductoClienteActivity::class.java)
            intent.putExtra("nombre",  producto.nombre)
            intent.putExtra("precio",  "$${String.format("%,.0f", producto.precio)}")
            intent.putExtra("rating",  producto.rating)
            intent.putExtra("enStock", producto.enStock)
            startActivity(intent)
        }
        binding.rvResultados.layoutManager = LinearLayoutManager(this)
        binding.rvResultados.adapter = adapter
        actualizarResultados(productos.size)
    }

    private fun setupBuscador() {
        // Buscar al escribir
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.buscar(s.toString())
                binding.tvQueryLabel.text =
                    if (s.isNullOrEmpty()) "Todos los productos"
                    else "Resultado para \"${s}\""
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Buscar al presionar Enter
        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                adapter.buscar(binding.etSearch.text.toString())
                true
            } else false
        }

        // Botón lupa
        binding.btnBuscar.setOnClickListener {
            adapter.buscar(binding.etSearch.text.toString())
        }
    }

    private fun setupChips() {
        binding.chipTodos.setOnClickListener    { adapter.filtrarPorCategoria("Todos") }
        binding.chipFrenos.setOnClickListener   { adapter.filtrarPorCategoria("Frenos") }
        binding.chipFiltros.setOnClickListener  { adapter.filtrarPorCategoria("Filtros") }
        binding.chipAceites.setOnClickListener  { adapter.filtrarPorCategoria("Aceites") }
        binding.chipMas.setOnClickListener      { mostrarFiltros() }
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnNotif.setOnClickListener {
            Toast.makeText(this, "Notificaciones", Toast.LENGTH_SHORT).show()
        }
        binding.btnFiltrar.setOnClickListener { mostrarFiltros() }
    }

    private fun actualizarResultados(total: Int) {
        binding.tvResultados.text = "$total productos encontrados"
    }

    private fun mostrarFiltros() {
        val dialog = BottomSheetDialog(this)
        val dialogBinding = DialogFiltrosBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)

        val categorias = listOf("Todas las categorías", "Filtros", "Frenos",
            "Aceites", "Baterías", "Suspensión", "Motor")
        dialogBinding.spinnerCategoria.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categorias)
        )

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