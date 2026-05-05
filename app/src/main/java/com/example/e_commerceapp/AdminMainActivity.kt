package com.example.e_commerceapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.e_commerceapp.databinding.ActivityAdminMainBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.content.Intent

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Manejo del botón atrás
        onBackPressedDispatcher.addCallback(this) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                finish()
            }
        }

        setupDrawer()
        setupLineChart()
        setupPieChart()
        setupMetrics()
    }

    private fun setupDrawer() {
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener { item ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.nav_dashboard   -> binding.tvTitle.text = "Resumen"
                R.id.nav_productos   -> binding.tvTitle.text = "Productos"
                R.id.nav_pedidos     -> binding.tvTitle.text = "Pedidos"
                R.id.nav_clientes    -> binding.tvTitle.text = "Clientes"
                R.id.nav_vendedores  -> binding.tvTitle.text = "Vendedores"
                R.id.nav_promociones -> binding.tvTitle.text = "Promociones"
                R.id.nav_reportes    -> binding.tvTitle.text = "Reportes"
                R.id.nav_config      -> binding.tvTitle.text = "Configuración"

                // cerrar sesión
                R.id.nav_cerrar_sesion -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
            true
        }

    }

    private fun cerrarSesion() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    private fun setupMetrics() {
        binding.cardVentas.tvMetricTitle.text   = "Ventas este mes"
        binding.cardVentas.tvMetricValue.text   = "$ 25.450.000"
        binding.cardVentas.tvMetricSub.text     = "+18% vs mes anterior"

        binding.cardPedidos.tvMetricTitle.text  = "Pedidos este mes"
        binding.cardPedidos.tvMetricValue.text  = "142"
        binding.cardPedidos.tvMetricSub.text    = "+10% vs mes anterior"

        binding.cardClientes.tvMetricTitle.text = "Clientes"
        binding.cardClientes.tvMetricValue.text = "89"
        binding.cardClientes.tvMetricSub.text   = "+12% vs mes anterior"

        binding.cardComision.tvMetricTitle.text = "Comisión"
        binding.cardComision.tvMetricValue.text = "$ 1.272.500"
        binding.cardComision.tvMetricSub.text   = "5% de ventas"
        binding.cardComision.tvMetricSub.setTextColor(Color.parseColor("#EF9F27"))
    }

    private fun setupLineChart() {
        val entries = listOf(
            Entry(0f, 2f), Entry(1f, 3.5f), Entry(2f, 2.8f),
            Entry(3f, 5f), Entry(4f, 4.2f), Entry(5f, 6.5f), Entry(6f, 7.8f)
        )
        val dataSet = LineDataSet(entries, "Ventas").apply {
            color = Color.parseColor("#1E90FF")
            setCircleColor(Color.parseColor("#1E90FF"))
            lineWidth = 2.5f
            circleRadius = 4f
            setDrawFilled(true)
            fillColor = Color.parseColor("#1E90FF")
            fillAlpha = 40
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        binding.lineChart.apply {
            data = LineData(dataSet)
            setBackgroundColor(Color.TRANSPARENT)
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.textColor = Color.WHITE
            axisLeft.textColor = Color.WHITE
            axisRight.isEnabled = false
            invalidate()
        }
    }

    private fun setupPieChart() {
        val entries = listOf(
            PieEntry(18f, ""), PieEntry(35f, ""),
            PieEntry(62f, ""), PieEntry(5f,  "")
        )
        val colors = listOf(
            Color.parseColor("#EF9F27"), Color.parseColor("#1E90FF"),
            Color.parseColor("#4CAF50"), Color.parseColor("#E24B4A")
        )
        val dataSet = PieDataSet(entries, "").apply {
            this.colors = colors
            sliceSpace = 2f
        }
        binding.pieChart.apply {
            data = PieData(dataSet)
            isDrawHoleEnabled = true
            holeRadius = 55f
            setHoleColor(Color.parseColor("#0D2137"))
            centerText = "120\nTotal"
            setCenterTextColor(Color.WHITE)
            setCenterTextSize(12f)
            description.isEnabled = false
            legend.isEnabled = false
            setBackgroundColor(Color.TRANSPARENT)
            invalidate()
        }
        binding.legendPendientes.legendColor.setBackgroundColor(Color.parseColor("#EF9F27"))
        binding.legendPendientes.legendLabel.text = "Pendientes"
        binding.legendPendientes.legendCount.text = "18"

        binding.legendEnviados.legendColor.setBackgroundColor(Color.parseColor("#1E90FF"))
        binding.legendEnviados.legendLabel.text = "Enviados"
        binding.legendEnviados.legendCount.text = "35"

        binding.legendEntregados.legendColor.setBackgroundColor(Color.parseColor("#4CAF50"))
        binding.legendEntregados.legendLabel.text = "Entregados"
        binding.legendEntregados.legendCount.text = "62"

        binding.legendCancelados.legendColor.setBackgroundColor(Color.parseColor("#E24B4A"))
        binding.legendCancelados.legendLabel.text = "Cancelados"
        binding.legendCancelados.legendCount.text = "5"
    }
}