package com.example.e_commerceapp.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class AdminData(
    val id: String = "",
    val correo: String = "",
    val rol: String = "",
    @SerialName("created_at")
    val createdAt: String = ""
)