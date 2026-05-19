package com.example.e_commerceapp.model

import com.example.e_commerceapp.seller.ProductoPedido

// ── Peticiones ──────────────────────────────────────────────
enum class TipoPeticion { CREACION_TIENDA, MODIFICACION_TIENDA, NUEVO_PRODUCTO, MODIFICACION_PRODUCTO, ELIMINACION_PRODUCTO }

data class Peticion(
    val id: String,
    val tipo: String,
    val vendedor: String,
    val emailVendedor: String,
    val telefonoVendedor: String,
    val fecha: String,
    val estado: String,         // Pendiente, En revisión, Aprobada, Rechazada
    val iconColor: String,      // color del ícono
    val tipoPeticion: TipoPeticion,
    // Campos según tipo
    val nombreTienda: String = "",
    val categoria: String = "",
    val direccion: String = "",
    val descripcion: String = "",
    val motivo: String = "",
    val notas: String = "",
    // Para modificaciones (tabla actual vs solicitado)
    val cambiosActual: Map<String, String> = emptyMap(),
    val cambiosSolicitado: Map<String, String> = emptyMap(),
    // Para productos
    val nombreProducto: String = "",
    val marca: String = "",
    val sku: String = "",
    val precio: String = "",
    val stock: String = "",
    val descripcionProducto: String = ""
)

// ── Pedidos de clientes ─────────────────────────────────────
data class PedidoAdmin(
    val numero: String,
    val cliente: String,
    val emailCliente: String,
    val telefonoCliente: String,
    val fecha: String,
    val cantidadProductos: Int,
    val total: String,
    val subtotal: String,
    val envio: String,
    val estado: String,
    val direccion: String,
    val referencia: String,
    val metodoPago: String,
    val productos: List<ProductoPedido>
)

// ── Datos de prueba ─────────────────────────────────────────
object DatosPedidosAdmin {

    val peticiones = listOf(
        Peticion(
            id = "PET-001", tipo = "Solicitud de creación de tienda",
            vendedor = "María Gómez", emailVendedor = "maria.gomez@gmail.com",
            telefonoVendedor = "986 321 654", fecha = "16 May 2024 · 09:20 a.m.",
            estado = "Pendiente", iconColor = "#1E90FF",
            tipoPeticion = TipoPeticion.CREACION_TIENDA,
            nombreTienda = "AutoParts Perú", categoria = "Repuestos y Accesorios",
            direccion = "Av. Los Próceres 123, Lima, Lima, Perú",
            descripcion = "Tienda especializada en repuestos automotrices originales y genéricos.",
            notas = "Adjunto los documentos solicitados para la creación de mi tienda. Quedo atenta a su aprobación."
        ),
        Peticion(
            id = "PET-002", tipo = "Solicitud de modificación de tienda",
            vendedor = "Carlos López", emailVendedor = "carlos.lopez@gmail.com",
            telefonoVendedor = "975 222 333", fecha = "16 May 2024 · 08:15 a.m.",
            estado = "En revisión", iconColor = "#4CAF50",
            tipoPeticion = TipoPeticion.MODIFICACION_TIENDA,
            motivo = "Actualización de información de la tienda y cambio de dirección por nueva sede.",
            cambiosActual = mapOf("Nombre de la tienda" to "AutoParts Lima", "Descripción" to "Tienda de repuestos automotrices.", "Dirección" to "Av. Los Próceres 123, Lima", "Teléfono" to "975 222 333"),
            cambiosSolicitado = mapOf("Nombre de la tienda" to "AutoParts Perú", "Descripción" to "Tienda especializada en repuestos automotrices originales y genéricos.", "Dirección" to "Av. Javier Prado Este 4560, Lima", "Teléfono" to "987 111 444")
        ),
        Peticion(
            id = "PET-003", tipo = "Solicitud de nuevo producto",
            vendedor = "Ana Torres", emailVendedor = "ana.torres@gmail.com",
            telefonoVendedor = "944 111 222", fecha = "15 May 2024 · 05:30 p.m.",
            estado = "Pendiente", iconColor = "#9C27B0",
            tipoPeticion = TipoPeticion.NUEVO_PRODUCTO,
            nombreProducto = "Filtro de aceite Mann W712", categoria = "Filtros",
            marca = "MANN-FILTER", sku = "FIL-MANN-W712", precio = "S/ 45.00",
            stock = "50 unidades",
            descripcionProducto = "Filtro de aceite de alta calidad para motores a gasolina y diésel. Compatible con múltiples modelos de vehículos."
        ),
        Peticion(
            id = "PET-004", tipo = "Solicitud de modificación de producto",
            vendedor = "Luis Martínez", emailVendedor = "luis.martinez@gmail.com",
            telefonoVendedor = "933 444 555", fecha = "15 May 2024 · 04:10 p.m.",
            estado = "Aprobada", iconColor = "#E67E22",
            tipoPeticion = TipoPeticion.MODIFICACION_PRODUCTO,
            motivo = "Ajuste de precio por nueva promoción y actualización de stock.",
            cambiosActual = mapOf("Precio" to "S/ 120.00", "Stock" to "20 unidades", "Descripción" to "Pastillas de freno Brembo P85020."),
            cambiosSolicitado = mapOf("Precio" to "S/ 115.00", "Stock" to "35 unidades", "Descripción" to "Pastillas de freno Brembo P85020. Mejor rendimiento y mayor durabilidad."),
            notas = "Cambios aprobados correctamente."
        ),
        Peticion(
            id = "PET-005", tipo = "Solicitud de eliminación de producto",
            vendedor = "Pedro Ramírez", emailVendedor = "pedro.ramirez@gmail.com",
            telefonoVendedor = "922 333 444", fecha = "15 May 2024 · 03:25 p.m.",
            estado = "Rechazada", iconColor = "#E74C3C",
            tipoPeticion = TipoPeticion.ELIMINACION_PRODUCTO
        )
    )

    val pedidos = listOf(
        PedidoAdmin("#PED-1058", "Juan Pérez", "juan.perez@gmail.com", "987 654 321",
            "16 May 2024 · 10:30 a.m.", 3, "S/ 360.00", "S/ 350.00", "S/ 10.00",
            "Pendiente", "Av. Los Próceres 123, Lima, Lima, Perú",
            "Frente al parque principal", "Visa terminada en 4242",
            listOf(
                ProductoPedido("Filtro de aceite Mann W712", "S/ 45.00", 1, "S/ 45.00"),
                ProductoPedido("Pastillas de freno Brembo P85020", "S/ 120.00", 2, "S/ 240.00"),
                ProductoPedido("Aceite Mobil 1 5W-30 (1L)", "S/ 65.00", 1, "S/ 65.00")
            )),
        PedidoAdmin("#PED-1057", "María Gómez", "maria.gomez@gmail.com", "976 543 210",
            "16 May 2024 · 09:15 a.m.", 2, "S/ 180.00", "S/ 170.00", "S/ 10.00",
            "En proceso", "Jr. Moquegua 456, Lima", "Edificio azul", "Mastercard terminada en 1234",
            listOf(ProductoPedido("Filtro de Aire Mann", "S/ 85.00", 2, "S/ 170.00"))),
        PedidoAdmin("#PED-1056", "Carlos López", "carlos.lopez@gmail.com", "965 432 109",
            "15 May 2024 · 04:45 p.m.", 4, "S/ 320.00", "S/ 310.00", "S/ 10.00",
            "En proceso", "Av. Arequipa 789, Lima", "Cerca al semáforo", "Visa terminada en 5678",
            listOf(ProductoPedido("Bujía NGK Iridium", "S/ 77.50", 4, "S/ 310.00"))),
        PedidoAdmin("#PED-1055", "Ana Torres", "ana.torres@gmail.com", "954 321 098",
            "15 May 2024 · 11:20 a.m.", 1, "S/ 95.00", "S/ 85.00", "S/ 10.00",
            "Completado", "Calle Las Flores 321, Lima", "Casa con reja verde", "Efectivo",
            listOf(ProductoPedido("Aceite Mobil 1 5W-30 Sintético 4L", "S/ 85.00", 1, "S/ 85.00"))),
        PedidoAdmin("#PED-1054", "Luis Martínez", "luis.martinez@gmail.com", "943 210 987",
            "14 May 2024 · 06:30 p.m.", 2, "S/ 160.00", "S/ 150.00", "S/ 10.00",
            "Completado", "Av. Brasil 654, Lima", "Frente al banco", "Visa terminada en 4242",
            listOf(ProductoPedido("Amortiguador Bosch Trasero", "S/ 75.00", 2, "S/ 150.00")))
    )
}