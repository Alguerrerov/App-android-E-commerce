package com.example.e_commerceapp.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ProductoData(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val marca: String = "",
    val compatibilidad: String? = null,
    val dimensiones: String? = null,
    val peso: String? = null,
    val codigo: String? = null,
    val foto: String? = null,
    val activo: Boolean = true,
    @SerialName("created_at")
    val createdAt: String = ""
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class StockTiendasData(
    val id: String = "",
    @SerialName("productos_id")
    val productoId: String = "",
    @SerialName("tiendas_id")
    val tiendaId: String = "",
    val stock: Int = 0,
    val estado: String? = null    // ← nullable
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class ProductoConStock(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val marca: String = "",
    val compatibilidad: String? = null,   // nullable
    val dimensiones: String? = null,      // nullable
    val peso: String? = null,             // nullable
    val codigo: String? = null,           // nullable
    val foto: String? = null,             // nullable
    val activo: Boolean = true,
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("Stock_tiendas")
    val stockTienda: List<StockTiendasData> = emptyList()
)