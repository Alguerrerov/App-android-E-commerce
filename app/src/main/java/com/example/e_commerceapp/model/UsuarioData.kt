package com.example.e_commerceapp.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class UsuarioData(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val telefono: String? = null,
    @SerialName("Genero")
    val genero: String? = null,
    @SerialName("DNI")
    val dni: String? = null,
    @SerialName("fecha de registro")
    val fechaRegistro: String? = null,
    val estado: String? = null,
    val direccion: String? = null,
    @SerialName("created_at")
    val createdAt: String = ""
)