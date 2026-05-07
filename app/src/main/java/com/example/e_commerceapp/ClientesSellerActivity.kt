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
import com.example.e_commerceapp.databinding.ActivityClientesSellerBinding



class ClientesSellerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientesSellerBinding
    private lateinit var adapter: ClientesSellerAdapter

    private val clienteSeller = listOf(
        ClienteSeller("Juan Pérez", "Pérez Gómez", "juan.perez@gmail.com", "987 654 321",
            "12345678", "12 May 1992", "Av. Los Próceres 123, Lima\nLima, Perú",
            "Visa •••• 4242", "Activo", "Cliente desde: 15 Mar 2024",
            listOf(
                PedidoResumen("#PED-1058", "16 May 2024 · 10:30 a.m.", 3, "S/ 360.00", "Entregado"),
                PedidoResumen("#PED-1023", "30 Abr 2024 · 09:15 a.m.", 2, "S/ 180.00", "Entregado"),
                PedidoResumen("#PED-0987", "18 Abr 2024 · 02:45 p.m.", 4, "S/ 420.00", "Entregado"),
                PedidoResumen("#PED-0912", "05 Abr 2024 · 11:20 a.m.", 1, "S/ 75.00",  "Cancelado"),
                PedidoResumen("#PED-0876", "28 Mar 2024 · 04:10 p.m.", 2, "S/ 220.00", "Entregado")
            )),
        ClienteSeller("María Gómez", "Gómez Torres", "maria.gomez@gmail.com", "986 321 654",
            "87654321", "05 Jun 1988", "Jr. Moquegua 456, Lima", "Mastercard •••• 1234",
            "Activo", "Cliente desde: 20 Ene 2024",
            listOf(PedidoResumen("#PED-1057", "16 May 2024 · 09:15 a.m.", 2, "S/ 180.00", "Entregado"))),
        ClienteSeller("Carlos López", "López Sánchez", "carlos.lopez@gmail.com", "975 222 333",
            "11223344", "18 Mar 1990", "Av. Arequipa 789, Lima", "Visa •••• 5678",
            "Activo", "Cliente desde: 10 Feb 2024",
            listOf(PedidoResumen("#PED-1056", "15 May 2024 · 04:45 p.m.", 4, "S/ 320.00", "Entregado"))),
        ClienteSeller("Ana Torres", "Torres Ríos", "ana.torres@gmail.com", "944 111 222",
            "44556677", "22 Sep 1995", "Calle Las Flores 321, Lima", "Efectivo",
            "Activo", "Cliente desde: 05 Mar 2024",
            listOf(PedidoResumen("#PED-1055", "15 May 2024 · 11:20 a.m.", 1, "S/ 95.00", "Entregado"))),
        ClienteSeller("Luis Martínez", "Martínez Cruz", "luis.martinez@gmail.com", "933 444 555",
            "55667788", "30 Dic 1985", "Av. Brasil 654, Lima", "Visa •••• 4242",
            "Activo", "Cliente desde: 01 Abr 2024",
            listOf(PedidoResumen("#PED-1054", "14 May 2024 · 06:30 p.m.", 2, "S/ 160.00", "Entregado"))),
        ClienteSeller("Laura Sánchez", "Sánchez Vega", "laura.sanchez@gmail.com", "922 666 777",
            "66778899", "14 Jul 1993", "Jr. Cusco 123, Lima", "Mastercard •••• 9876",
            "Inactivo", "Cliente desde: 15 Dic 2023",
            listOf(PedidoResumen("#PED-0850", "20 Feb 2024 · 03:10 p.m.", 1, "S/ 45.00", "Cancelado")))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupBuscador()
        setupTabs()
    }

    private fun setupRecyclerView() {
        adapter = ClientesSellerAdapter(clienteSeller) { cliente ->
            val intent = Intent(this, DetalleClienteSellerActivity::class.java).apply {
                putExtra("nombre",          cliente.nombre)
                putExtra("apellidos",       cliente.apellidos)
                putExtra("email",           cliente.email)
                putExtra("telefono",        cliente.telefono)
                putExtra("dni",             cliente.dni)
                putExtra("fechaNacimiento", cliente.fechaNacimiento)
                putExtra("direccion",       cliente.direccion)
                putExtra("metodoPago",      cliente.metodoPago)
                putExtra("estado",          cliente.estado)
                putExtra("clienteDesde",    cliente.clienteDesde)
            }
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
        setTabActivo(binding.tabTodos)
        binding.tabTodos.setOnClickListener     { adapter.filtrarPorEstado("Todos");    setTabActivo(binding.tabTodos) }
        binding.tabActivos.setOnClickListener   { adapter.filtrarPorEstado("Activo");   setTabActivo(binding.tabActivos) }
        binding.tabInactivos.setOnClickListener { adapter.filtrarPorEstado("Inactivo"); setTabActivo(binding.tabInactivos) }
    }

    private fun setTabActivo(tab: TextView) {
        listOf(binding.tabTodos, binding.tabActivos, binding.tabInactivos)
            .forEach { it.setTextColor(Color.parseColor("#8899AA")) }
        tab.setTextColor(Color.parseColor("#1E90FF"))
    }
}