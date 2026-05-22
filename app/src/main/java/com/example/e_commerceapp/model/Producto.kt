package com.example.e_commerceapp.model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.R

data class Producto(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val stock: String = "",
    val marca: String = "",
    val compatibilidad: String = "",
    val dimensiones: String = "",
    val peso: String = "",
    val codigo: String = "",
    val foto: String = "",
    val estado: String = "Activo",
    val vendedor: String = "AutoParts",
    val tienda: String = "AutoParts"
)

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

    fun actualizarLista(nuevaLista: List<Producto>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

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

