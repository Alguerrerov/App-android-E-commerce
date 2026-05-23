package com.example.e_commerceapp.admin

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.R
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.admin.AddUserActivity
import io.github.jan.supabase.postgrest.postgrest
import com.example.e_commerceapp.databinding.ActivityClientesBinding
import com.example.e_commerceapp.model.UsuarioData
import kotlinx.coroutines.launch

data class Cliente(
    val id: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val genero: String,
    val dni: String,
    val fechaRegistro: String,
    val estado: String,
    val direccion: String
)

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
        holder.tvNombre.text = "${cliente.nombre} ${cliente.apellido}"
        holder.tvEmail.text  = cliente.email
        holder.tvRol.text    = "Cliente"
        holder.tvEstado.text = cliente.estado
        when (cliente.estado) {
            "Activo"   -> holder.tvEstado.setTextColor(Color.parseColor("#4CAF50"))
            "Inactivo" -> holder.tvEstado.setTextColor(Color.parseColor("#8899AA"))
            else       -> holder.tvEstado.setTextColor(Color.parseColor("#EF9F27"))
        }
        holder.itemView.setOnClickListener { onClick(cliente) }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<Cliente>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    fun filtrar(texto: String) {
        lista = if (texto.isEmpty()) clientesOriginales
        else clientesOriginales.filter {
            it.nombre.contains(texto, ignoreCase = true) ||
                    it.apellido.contains(texto, ignoreCase = true) ||
                    it.email.contains(texto, ignoreCase = true)
        }
        notifyDataSetChanged()
    }

    fun filtrarPorEstado(estado: String) {
        lista = if (estado == "Todos") clientesOriginales
        else clientesOriginales.filter { it.estado == estado }
        notifyDataSetChanged()
    }
}

class ClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding
    private lateinit var adapter: ClientesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupBuscador()
        setupTabs()
        setupBotones()
        cargarClientes()
    }

    override fun onResume() {
        super.onResume()
        cargarClientes()
    }

    private fun setupRecyclerView() {
        adapter = ClientesAdapter(emptyList()) { cliente ->
            val intent = Intent(this, DetalleClienteActivity::class.java)
            intent.putExtra("id",           cliente.id)
            intent.putExtra("nombre",       cliente.nombre)
            intent.putExtra("apellido",     cliente.apellido)
            intent.putExtra("email",        cliente.email)
            intent.putExtra("telefono",     cliente.telefono)
            intent.putExtra("genero",       cliente.genero)
            intent.putExtra("dni",          cliente.dni)
            intent.putExtra("fechaRegistro",cliente.fechaRegistro)
            intent.putExtra("estado",       cliente.estado)
            intent.putExtra("direccion",    cliente.direccion)
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

    private fun setupTabs() {
        binding.tabTodos.setOnClickListener {
            adapter.filtrarPorEstado("Todos")
            binding.tabTodos.setTextColor(getColor(R.color.accent_blue))
            binding.tabActivos.setTextColor(getColor(R.color.state_inactive))
            binding.tabInactivos.setTextColor(getColor(R.color.state_inactive))
        }
        binding.tabActivos.setOnClickListener {
            adapter.filtrarPorEstado("Activo")
            binding.tabTodos.setTextColor(getColor(R.color.state_inactive))
            binding.tabActivos.setTextColor(getColor(R.color.accent_blue))
            binding.tabInactivos.setTextColor(getColor(R.color.state_inactive))
        }
        binding.tabInactivos.setOnClickListener {
            adapter.filtrarPorEstado("Inactivo")
            binding.tabTodos.setTextColor(getColor(R.color.state_inactive))
            binding.tabActivos.setTextColor(getColor(R.color.state_inactive))
            binding.tabInactivos.setTextColor(getColor(R.color.accent_blue))
        }
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnAddUser.setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }
    }

    private fun cargarClientes() {
        lifecycleScope.launch {
            try {
                val usuarios = SupabaseClient.client
                    .postgrest["Usuarios"]
                    .select()
                    .decodeList<UsuarioData>()

                val clientes = usuarios.map { u ->
                    Cliente(
                        id           = u.id,
                        nombre       = u.nombre,
                        apellido     = u.apellido,
                        email        = u.correo,
                        telefono     = u.telefono     ?: "",
                        genero       = u.genero       ?: "",
                        dni          = u.dni          ?: "",
                        fechaRegistro= u.fechaRegistro?: "",
                        estado       = u.estado       ?: "Activo",
                        direccion    = u.direccion    ?: ""
                    )
                }

                val total     = clientes.size
                val activos   = clientes.count { it.estado == "Activo" }
                val inactivos = clientes.count { it.estado == "Inactivo" }

                runOnUiThread {
                    ClientesAdapter.clientesOriginales = clientes
                    adapter.actualizarLista(clientes)
                    binding.tabTodos.text     = "Todos ($total)"
                    binding.tabActivos.text   = "Activos ($activos)"
                    binding.tabInactivos.text = "Inactivos ($inactivos)"
                }
            } catch (e: Exception) {
                android.util.Log.e("CLIENTES", "Error: ${e.message}")
                runOnUiThread {
                    android.widget.Toast.makeText(this@ClientesActivity,
                        "Error al cargar: ${e.message}",
                        android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}