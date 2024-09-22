package br.com.cohive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import br.com.cohive.ui.theme.CohiveTheme
import kotlin.random.Random

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CohiveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DashboardContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DashboardContent(modifier: Modifier = Modifier) {
    var selectedMonths by remember { mutableStateOf(5) }

    val dataBar = List(selectedMonths) { Random.nextFloat() * 100 }
    val dataLine = List(selectedMonths) { Random.nextFloat() * 100 }
    val labels = listOf("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro").take(selectedMonths)

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val cardWidth = if (isLandscape) 800.dp else 400.dp // Ajusta a largura das caixas

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Adicionando rolagem vertical
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Caixa de seleção de meses
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .width(cardWidth),
                elevation = elevatedCardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Escolha o número de meses:", fontSize = 16.sp)
                    Slider(
                        value = selectedMonths.toFloat(),
                        onValueChange = { selectedMonths = it.toInt() },
                        valueRange = 1f..12f,
                        steps = 11
                    )
                    Text(
                        text = "$selectedMonths mês(es)",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Botão para baixar relatório
            Button(
                onClick = { /* Ação do botão */ },
                modifier = Modifier
                    .padding(16.dp)
                    .width(cardWidth),
                shape = MaterialTheme.shapes.small, // Deixando o botão arredondado
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF))
            ) {
                Text(text = "Baixar Relatório", color = Color.White)
            }

            // Gráfico de linha
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .width(cardWidth)
                    .height(250.dp),
                elevation = elevatedCardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Gráfico de Linhas",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    LineChart(
                        data = dataLine,
                        labels = labels,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                }
            }

            // Gráfico de barras
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .width(cardWidth)
                    .height(250.dp),
                elevation = elevatedCardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Gráfico de Barras",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    BarChart(
                        data = dataBar,
                        labels = labels,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                }
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
                textAlign = TextAlign.Center,
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
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
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
