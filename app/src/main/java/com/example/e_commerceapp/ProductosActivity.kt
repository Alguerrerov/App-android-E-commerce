package com.example.e_commerceapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.databinding.ActivityProductosBinding

// Modelo
data class Producto(
    val nombre:    String,
    val categoria: String,
    val precio:    String,
    val stock:     String,
    val estado:    String
)

// Adapter
class ProductosAdapter(
    private var lista: List<Producto>,
    private val onClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre:    TextView = view.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoria)
        val tvPrecio:    TextView = view.findViewById(R.id.tvPrecio)
        val tvStock:     TextView = view.findViewById(R.id.tvStock)
        val tvEstado:    TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]
        holder.tvNombre.text    = p.nombre
        holder.tvCategoria.text = p.categoria
        holder.tvPrecio.text    = p.precio
        holder.tvStock.text     = p.stock
        holder.tvEstado.text    = p.estado
        when (p.estado) {
            "Activo"   -> holder.tvEstado.setTextColor(Color.parseColor("#4CAF50"))
            "Inactivo" -> holder.tvEstado.setTextColor(Color.parseColor("#8899AA"))
        }
        holder.itemView.setOnClickListener { onClick(p) }
    }

    override fun getItemCount() = lista.size

    fun filtrar(texto: String) {
        lista = if (texto.isEmpty()) productosOriginales
        else productosOriginales.filter {
            it.nombre.contains(texto, ignoreCase = true) ||
                    it.categoria.contains(texto, ignoreCase = true)
        }
        notifyDataSetChanged()
    }

    fun filtrarPorEstado(estado: String) {
        lista = if (estado == "Todos") productosOriginales
        else productosOriginales.filter { it.estado == estado }
        notifyDataSetChanged()
    }

    companion object {
        var productosOriginales: List<Producto> = emptyList()
    }
}

class ProductosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductosBinding
    private lateinit var adapter: ProductosAdapter

    private val productos = listOf(
        Producto("Filtro de Aceite Mann W610/3", "Filtros",  "$27.900",  "150", "Activo"),
        Producto("Pastillas de Freno Brembo P85045", "Frenos",  "$89.900",  "80",  "Activo"),
        Producto("Aceite Mobil 1 5W-30 Sintético 4L", "Aceites", "$149.900", "60",  "Activo"),
        Producto("Filtro de Aire Mann C29005",        "Baterías","$45.900",  "120", "Activo"),
        Producto("Bujía NGK Iridium BKR6EIX",         "Filtros",  "$12.900",  "200", "Activo"),
        Producto("Amortiguador Bosch Trasero",         "Suspensión","$189.900","30",  "Inactivo")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupBuscador()
        setupTabs()
        setupBotones()
    }

    private fun setupRecyclerView() {
        ProductosAdapter.productosOriginales = productos
        adapter = ProductosAdapter(productos) { producto ->
            val intent = Intent(this, DetalleProductoActivity::class.java)
            intent.putExtra("nombre",    producto.nombre)
            intent.putExtra("categoria", producto.categoria)
            intent.putExtra("precio",    producto.precio)
            intent.putExtra("stock",     producto.stock)
            intent.putExtra("estado",    producto.estado)
            intent.putExtra("vendedor", "Carlos López")
            intent.putExtra("tienda", "AutoParts Store Central")
            startActivity(intent)
        }
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adapter
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
}