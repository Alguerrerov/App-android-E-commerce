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
    val telefono: String = "",
    @SerialName("created_at")
    val createdAt: String = ""
)