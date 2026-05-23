package com.example.e_commerceapp.client

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.databinding.ActivityMiDireccionBinding
import com.example.e_commerceapp.model.UsuarioData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import java.util.Locale

class MiDireccionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMiDireccionBinding
    private val LOCATION_PERMISSION_REQUEST = 1001
    private var direccionActual = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiDireccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        cargarDireccion()

        // Click en + Agregar dirección → mostrar opciones
        binding.tvAgregarDireccion.setOnClickListener {
            mostrarOpcionesAgregar()
        }

        binding.btnEditarCasa.setOnClickListener {
            mostrarDialogoManual(direccionActual)
        }

        binding.btnEliminarCasa.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar dirección")
                .setMessage("¿Seguro que deseas eliminar tu dirección?")
                .setPositiveButton("Eliminar") { _, _ ->
                    guardarDireccion("")
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        binding.btnGuardar.setOnClickListener { finish() }
    }

    private fun cargarDireccion() {
        lifecycleScope.launch {
            try {
                val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return@launch

                val usuario = SupabaseClient.client.postgrest["Usuarios"]
                    .select { filter { eq("id", userId) } }
                    .decodeSingle<UsuarioData>()

                direccionActual = usuario.direccion ?: ""

                runOnUiThread {
                    actualizarUI()
                }
            } catch (e: Exception) {
                android.util.Log.e("DIRECCION", "Error: ${e.message}")
            }
        }
    }

    private fun actualizarUI() {
        if (direccionActual.isNotEmpty()) {
            binding.layoutVacio.visibility         = android.view.View.GONE
            binding.layoutDireccionCasa.visibility = android.view.View.VISIBLE
            binding.tvDireccionCasa.text           = direccionActual
            binding.tvAgregarDireccion.text        = "+ Cambiar dirección"
        } else {
            binding.layoutVacio.visibility         = android.view.View.VISIBLE
            binding.layoutDireccionCasa.visibility = android.view.View.GONE
            binding.tvAgregarDireccion.text        = "+ Agregar dirección"
        }
    }

    private fun mostrarOpcionesAgregar() {
        AlertDialog.Builder(this)
            .setTitle("Agregar dirección")
            .setItems(arrayOf(
                "📍 Usar mi ubicación actual",
                "✏️ Escribir dirección manualmente"
            )) { _, which ->
                when (which) {
                    0 -> solicitarUbicacion()
                    1 -> mostrarDialogoManual("")
                }
            }
            .show()
    }

    private fun mostrarDialogoManual(textoActual: String) {
        val input = EditText(this).apply {
            setText(textoActual)
            hint = "Ej: Calle 123 #45-67, Bogotá"
            setTextColor(android.graphics.Color.BLACK)
            setHintTextColor(android.graphics.Color.GRAY)
            setBackgroundColor(android.graphics.Color.WHITE)
            setPadding(40, 20, 40, 20)
        }

        AlertDialog.Builder(this)
            .setTitle("Escribe tu dirección")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val texto = input.text.toString().trim()
                if (texto.isNotEmpty()) {
                    guardarDireccion(texto)
                } else {
                    Toast.makeText(this, "Escribe una dirección", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun solicitarUbicacion() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST
            )
            return
        }
        obtenerUbicacion()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion()
            } else {
                Toast.makeText(this,
                    "Permiso de ubicación denegado",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerUbicacion() {
        Toast.makeText(this, "Obteniendo ubicación...", Toast.LENGTH_SHORT).show()

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = android.location.Geocoder(this, Locale.getDefault())
                    val direcciones = geocoder.getFromLocation(
                        location.latitude, location.longitude, 1
                    )
                    val direccionTexto = if (!direcciones.isNullOrEmpty()) {
                        val dir = direcciones[0]
                        buildString {
                            if (!dir.thoroughfare.isNullOrEmpty())     append("${dir.thoroughfare} ")
                            if (!dir.subThoroughfare.isNullOrEmpty())  append("${dir.subThoroughfare}, ")
                            if (!dir.locality.isNullOrEmpty())         append("${dir.locality}, ")
                            if (!dir.adminArea.isNullOrEmpty())        append("${dir.adminArea}, ")
                            if (!dir.countryName.isNullOrEmpty())      append(dir.countryName)
                        }.trim().trimEnd(',')
                    } else {
                        "Lat: ${location.latitude}, Lng: ${location.longitude}"
                    }

                    AlertDialog.Builder(this)
                        .setTitle("Dirección detectada")
                        .setMessage(direccionTexto)
                        .setPositiveButton("Usar esta dirección") { _, _ ->
                            guardarDireccion(direccionTexto)
                        }
                        .setNegativeButton("Escribir manualmente") { _, _ ->
                            mostrarDialogoManual(direccionTexto)
                        }
                        .show()
                } else {
                    Toast.makeText(this,
                        "No se pudo obtener la ubicación, intenta manualmente",
                        Toast.LENGTH_SHORT).show()
                    mostrarDialogoManual("")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,
                    "Error GPS: ${it.message}",
                    Toast.LENGTH_LONG).show()
                mostrarDialogoManual("")
            }
    }

    private fun guardarDireccion(direccion: String) {
        lifecycleScope.launch {
            try {
                val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: return@launch

                SupabaseClient.client.postgrest["Usuarios"]
                    .update({ set("direccion", direccion) }) {
                        filter { eq("id", userId) }
                    }

                direccionActual = direccion

                runOnUiThread {
                    actualizarUI()
                    Toast.makeText(this@MiDireccionActivity,
                        if (direccion.isEmpty()) "Dirección eliminada"
                        else "Dirección guardada ✓",
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MiDireccionActivity,
                        "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}