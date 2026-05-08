package com.example.e_commerceapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.databinding.ActivityCarritoBinding

// Modelo item carrito
data class ItemCarrito(
    val nombre: String,
    val sku: String,
    val precioUnitario: Double,
    var cantidad: Int
)

// Adapter carrito
class CarritoAdapter(
    private val lista: MutableList<ItemCarrito>,
    private val onCambio: () -> Unit
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre:   TextView = view.findViewById(R.id.tvNombre)
        val tvSku:      TextView = view.findViewById(R.id.tvSku)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val tvPrecio:   TextView = view.findViewById(R.id.tvPrecio)
        val btnMenos:   TextView = view.findViewById(R.id.btnMenos)
        val btnMas:     TextView = view.findViewById(R.id.btnMas)
        val btnEliminar: android.widget.ImageButton = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.tvNombre.text   = item.nombre
        holder.tvSku.text      = "SKU: ${item.sku}"
        holder.tvCantidad.text = item.cantidad.toString()
        holder.tvPrecio.text   = "S/ ${String.format("%.2f", item.precioUnitario * item.cantidad)}"

        holder.btnMenos.setOnClickListener {
            if (item.cantidad > 1) {
                item.cantidad--
                holder.tvCantidad.text = item.cantidad.toString()
                holder.tvPrecio.text = "S/ ${String.format("%.2f", item.precioUnitario * item.cantidad)}"
                onCambio()
            }
        }

        holder.btnMas.setOnClickListener {
            item.cantidad++
            holder.tvCantidad.text = item.cantidad.toString()
            holder.tvPrecio.text = "S/ ${String.format("%.2f", item.precioUnitario * item.cantidad)}"
            onCambio()
        }

        holder.btnEliminar.setOnClickListener {
            lista.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            onCambio()
        }
    }

    override fun getItemCount(): Int = lista.size

    fun getTotal(): Double = lista.sumOf { it.precioUnitario * it.cantidad }
    fun getTotalItems(): Int = lista.sumOf { it.cantidad }
    fun getLista(): List<ItemCarrito> = lista
}

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private lateinit var adapter: CarritoAdapter

    private val itemsCarrito = mutableListOf(
        ItemCarrito("Filtro de aceite Mann W712",       "FIL-MANN-W712",    45.0,  1),
        ItemCarrito("Pastillas de freno Brembo P85020", "BRM-P85020",       120.0, 2),
        ItemCarrito("Aceite Mobil 1 5W-30 (1L)",        "ACE-MOBIL1-5W30",  65.0,  1),
        ItemCarrito("Filtro de aire Mann C 27 009",     "FIL-MANN-C27009",  55.0,  1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupBotones()
        actualizarResumen()
    }

    private fun setupRecyclerView() {
        adapter = CarritoAdapter(itemsCarrito) { actualizarResumen() }
        binding.rvCarrito.layoutManager = LinearLayoutManager(this)
        binding.rvCarrito.adapter = adapter
    }

    private fun actualizarResumen() {
        val subtotal = adapter.getTotal()
        val envio    = 15.0
        val total    = subtotal + envio
        val items    = adapter.getTotalItems()

        binding.tvSubtotalLabel.text = "Subtotal ($items productos)"
        binding.tvSubtotal.text      = "S/ ${String.format("%.2f", subtotal)}"
        binding.tvTotal.text         = "S/ ${String.format("%.2f", total)}"
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnVaciar.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Vaciar carrito")
                .setMessage("¿Seguro que deseas eliminar todos los productos?")
                .setPositiveButton("Vaciar") { _, _ ->
                    itemsCarrito.clear()
                    adapter.notifyDataSetChanged()
                    actualizarResumen()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        binding.btnProcederPago.setOnClickListener {
            if (itemsCarrito.isEmpty()) {
                Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this, ResumenCompraActivity::class.java))
        }
    }
}