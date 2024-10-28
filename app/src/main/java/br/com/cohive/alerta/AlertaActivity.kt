package br.com.cohive.alerta

import MyBottomNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import br.com.cohive.R
import br.com.cohive.estoque.EstoqueViewModel
import br.com.cohive.ui.theme.CohiveTheme

class AlertaActivity : ComponentActivity() {
    private val estoqueViewModel: EstoqueViewModel by viewModels()

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
                    SearchComponent(
                        modifier = Modifier.padding(innerPadding),
                        estoqueViewModel = estoqueViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComponent(modifier: Modifier = Modifier, estoqueViewModel: EstoqueViewModel) {
    var buscar by remember { mutableStateOf("") }
    val productQuantities by estoqueViewModel.productQuantities.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = buscar,
                onValueChange = { buscar = it },
                label = { Text("Buscar") },
                modifier = Modifier.width(270.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF9D4FFF),
                    unfocusedBorderColor = Color(0xFF9D4FFF),
                    cursorColor = Color(0xFF9D4FFF),
                    focusedLabelColor = Color(0xFF9D4FFF)
                )
            )
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF9D4EDD)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.funil),
                    contentDescription = "Buscar icon",
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Botão para checar as quantidades
        Button(
            onClick = {
                val userId = 1 // Substitua pelo ID do usuário correto
                estoqueViewModel.checkProductQuantities(userId)
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Checar Quantidades")
        }

        // Exibir alerta se alguma quantidade for igual a 3
        productQuantities?.let { quantities ->
            quantities.forEach { (productName, quantity) ->
                if (quantity == 3) {
                    AlertaCritico(productName)
                }
            }
        }
    }
}

@Composable
fun AlertaCritico(productName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFD32F2F))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.xzinho),
                contentDescription = "Alerta Crítico",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alerta Crítico! A quantidade do produto: $productName está em 3.",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun AlertaEmAtencao() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFA000))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.trianguloyellow),
                contentDescription = "Alerta em Atenção",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alerta em atenção. Verifique as informações.",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}
