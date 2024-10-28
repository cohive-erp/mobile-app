package br.com.cohive.dashboard

import MyBottomNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.Slider
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import br.com.cohive.ui.theme.CohiveTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.cohive.estoque.EstoqueViewModel

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CohiveTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomNavigation(navController = navController) }
                ) { innerPadding ->
                    DashboardContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DashboardContent(modifier: Modifier = Modifier) {
    // Obtenha a ViewModel
    val estoqueViewModel: EstoqueViewModel = viewModel()

    // Chame a função para buscar faturas mensais ao entrar na tela
    LaunchedEffect(Unit) {
        estoqueViewModel.fetchMonthlyInvoices()
    }

    // Observe o LiveData de faturas mensais
    val monthlyInvoices by estoqueViewModel.monthlyInvoices.observeAsState(emptyList())

    var selectedMonths by remember { mutableStateOf(6) } // Use 6 meses por padrão

    // Converta a lista de BigDecimal para Float para o gráfico
    val dataLine = monthlyInvoices.map { it.toFloat() }
    val labels = listOf("Último Mês", "Mês Anterior", "Mês 3", "Mês 4", "Mês 5", "Mês 6")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Seção de seleção de meses
        Card(
            modifier = Modifier.padding(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Escolha o número de meses:", fontSize = 16.sp)
                Slider(
                    value = selectedMonths.toFloat(),
                    onValueChange = { selectedMonths = it.toInt() },
                    valueRange = 1f..6f,
                    steps = 5
                )
                Text(text = "$selectedMonths mês(es)", fontSize = 14.sp)
            }
        }

        // Botão para baixar relatório
        Button(
            onClick = { /* Ação do botão */ },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF))
        ) {
            Text(text = "Baixar Relatório", color = Color.White)
        }

        // Gráfico de Linha
        Card(
            modifier = Modifier
                .padding(16.dp)
                .height(250.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Relatório de Vendas",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                LineChart(
                    data = dataLine.take(selectedMonths), // Usa os meses selecionados
                    labels = labels.take(selectedMonths), // Usa os meses selecionados
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                // Exibe os valores no gráfico de linha
                ValueLabels(data = dataLine.take(selectedMonths), labels = labels.take(selectedMonths))
            }
        }

        // Gráfico de Barras
        Card(
            modifier = Modifier
                .padding(16.dp)
                .height(250.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Relatório de Produtos",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                BarChart(
                    data = dataLine.take(selectedMonths), // Usa os meses selecionados
                    labels = labels.take(selectedMonths), // Usa os meses selecionados
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                // Exibe os valores no gráfico de barras
                ValueLabels(data = dataLine.take(selectedMonths), labels = labels.take(selectedMonths))
            }
        }
    }
}

@Composable
fun BarChart(data: List<Float>, labels: List<String>, modifier: Modifier = Modifier) {
    val maxValue = data.maxOrNull() ?: 1f

    androidx.compose.foundation.Canvas(modifier = modifier) {
        val barWidth = size.width / data.size * 0.6f
        val spacing = size.width / data.size

        data.forEachIndexed { index, value ->
            val barHeight = (value / maxValue) * size.height * 0.7f
            drawRect(
                color = Color(0xFF9D4FFF),
                topLeft = Offset(
                    x = (index * spacing) + (spacing - barWidth) / 2,
                    y = size.height - barHeight - 30f
                ),
                size = Size(barWidth, barHeight)
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        labels.forEach { label ->
            Text(
                text = label,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun LineChart(data: List<Float>, labels: List<String>, modifier: Modifier = Modifier) {
    val maxValue = data.maxOrNull() ?: 1f

    androidx.compose.foundation.Canvas(modifier = modifier) {
        val spacing = size.width / (data.size - 1)
        val path = androidx.compose.ui.graphics.Path()

        data.forEachIndexed { index, value ->
            val x = index * spacing
            val y = size.height - (value / maxValue) * size.height * 0.7f
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF9D4FFF),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        labels.forEach { label ->
            Text(
                text = label,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ValueLabels(data: List<Float>, labels: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEachIndexed { index, value ->
            Text(
                text = String.format("%.2f", value),
                fontSize = 12.sp,
                modifier = Modifier.weight(1f),
                color = Color(0xFF9D4FFF) // Cor dos valores
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardContentPreview() {
    CohiveTheme {
        DashboardContent()
    }
}
