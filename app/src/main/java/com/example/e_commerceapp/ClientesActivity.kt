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
import com.example.e_commerceapp.databinding.ActivityClientesBinding

// Modelo
data class Cliente(
    val nombre: String,
    val email: String,
    val rol: String,
    val estado: String
)

// Adapter
class ClientesAdapter(
    private var lista: List<Cliente>,
    private val onClick: (Cliente) -> Unit
) : RecyclerView.Adapter<ClientesAdapter.ViewHolder>() {

    companion object {
        var clientesOriginales: List<Cliente> = emptyList()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvEmail:  TextView = view.findViewById(R.id.tvEmail)
        val tvRol:    TextView = view.findViewById(R.id.tvRol)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cliente = lista[position]
        holder.tvNombre.text = cliente.nombre
        holder.tvEmail.text  = cliente.email
        holder.tvRol.text    = cliente.rol
        holder.tvEstado.text = cliente.estado
        when (cliente.estado) {
            "Activo"    -> holder.tvEstado.setTextColor(Color.parseColor("#4CAF50"))
            "Inactivo"  -> holder.tvEstado.setTextColor(Color.parseColor("#8899AA"))
            "Pendiente" -> holder.tvEstado.setTextColor(Color.parseColor("#EF9F27"))
            "Bloqueado" -> holder.tvEstado.setTextColor(Color.parseColor("#E24B4A"))
        }
        holder.itemView.setOnClickListener { onClick(cliente) }
    }

    override fun getItemCount(): Int = lista.size

    fun filtrar(texto: String) {
        lista = if (texto.isEmpty()) clientesOriginales
        else clientesOriginales.filter {
            it.nombre.contains(texto, ignoreCase = true) ||
                    it.email.contains(texto, ignoreCase = true)
        }
        notifyDataSetChanged()
    }
}

// Activity
class ClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding
    private lateinit var adapter: ClientesAdapter

    private val clientes = listOf(
        Cliente("Juan Pérez",    "juanperez@gmail.com",    "Cliente",   "Activo"),
        Cliente("María Gómez",   "mariagomez@gmail.com",   "Vendedor",  "Activo"),
        Cliente("Carlos López",  "carloslopez@gmail.com",  "Cliente",   "Activo"),
        Cliente("Ana Torres",    "anatorres@gmail.com",    "Vendedora", "Pendiente"),
        Cliente("Luis Martínez", "luismartinez@gmail.com", "Cliente",   "Bloqueado"),
        Cliente("Pedro Ramírez", "pedroramirez@gmail.com", "Vendedor",  "Activo"),
        Cliente("Laura Sánchez", "laurasanchez@gmail.com", "Cliente",   "Activo"),
        Cliente("Diego Herrera", "diegoherrera@gmail.com", "Cliente",   "Inactivo")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupBuscador()
        setupBotones()
    }

    private fun setupRecyclerView() {
        ClientesAdapter.clientesOriginales = clientes
        adapter = ClientesAdapter(clientes) { cliente ->
            val intent = Intent(this, DetalleClienteActivity::class.java)
            intent.putExtra("nombre", cliente.nombre)
            intent.putExtra("email",  cliente.email)
            intent.putExtra("rol",    cliente.rol)
            intent.putExtra("estado", cliente.estado)
            startActivity(intent)
        }
        binding.rvClientes.layoutManager = LinearLayoutManager(this)
        binding.rvClientes.adapter = adapter
    }

    private fun setupBuscador() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { adapter.filtrar(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnAddUser.setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }
    }
}