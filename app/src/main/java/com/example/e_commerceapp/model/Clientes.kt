package com.example.e_commerceapp.model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.R

data class ClienteSeller(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val dni: String,
    val fechaNacimiento: String,
    val direccion: String,
    val metodoPago: String,
    val estado: String,
    val clienteDesde: String,
    val pedidos: List<PedidoResumen>
)

data class PedidoResumen(
    val numero: String,
    val fecha: String,
    val cantidadProductos: Int,
    val total: String,
    val estado: String
)

class ClientesSellerAdapter(
    private var lista: List<ClienteSeller>,
    private val onClick: (ClienteSeller) -> Unit
) : RecyclerView.Adapter<ClientesSellerAdapter.ViewHolder>() {

    private var listaOriginal = lista

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre:   TextView = view.findViewById(R.id.tvNombreCliente)
        val tvEmail:    TextView = view.findViewById(R.id.tvEmailCliente)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefonoCliente)
        val tvEstado:   TextView = view.findViewById(R.id.tvEstadoCliente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_clientes, parent, false))

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val c = lista[position]
        holder.tvNombre.text   = c.nombre
        holder.tvEmail.text    = c.email
        holder.tvTelefono.text = "📞 ${c.telefono}"
        holder.tvEstado.text   = c.estado
        val bgColor = if (c.estado == "Activo") "#4CAF50" else "#8899AA"
        holder.tvEstado.setBackgroundColor(Color.parseColor(bgColor))
        holder.itemView.setOnClickListener { onClick(c) }
    }

    fun filtrar(texto: String) {
        lista = if (texto.isEmpty()) listaOriginal
        else listaOriginal.filter {
            it.nombre.contains(texto, ignoreCase = true) ||
                    it.email.contains(texto, ignoreCase = true)
        }
        notifyDataSetChanged()
    }

    fun filtrarPorEstado(estado: String) {
        lista = if (estado == "Todos") listaOriginal
        else listaOriginal.filter { it.estado == estado }
        notifyDataSetChanged()
    }
}