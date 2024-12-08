package br.com.cohive.alerta

import MyBottomNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import br.com.cohive.DataStoreManager
import br.com.cohive.EstoqueViewModelFactory
import br.com.cohive.R
import br.com.cohive.estoque.EstoqueViewModel
import br.com.cohive.ui.theme.CohiveTheme
import kotlinx.coroutines.delay

class AlertaActivity : ComponentActivity() {
    private lateinit var estoqueViewModel: EstoqueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStoreManager = DataStoreManager(applicationContext)
        val factory = EstoqueViewModelFactory(dataStoreManager)
        estoqueViewModel = ViewModelProvider(this, factory).get(EstoqueViewModel::class.java)

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

@Composable
fun SearchComponent(modifier: Modifier = Modifier, estoqueViewModel: EstoqueViewModel) {
    var isLoading by remember { mutableStateOf(false) }  // Controle de carregamento
    var showSecondMessage by remember { mutableStateOf(false) } // Controle da segunda mensagem
    val productQuantities by estoqueViewModel.productQuantities.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top  // Alinhamento no topo
    ) {
        // Título centralizado
        Text(
            text = "Cheque as quantidades dos produtos aqui!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), // Adiciona um espaçamento abaixo do título
            textAlign = TextAlign.Center  // Garantindo que o texto esteja centralizado
        )

        // Botão centralizado
        Button(
            onClick = {
                isLoading = true  // Inicia o carregamento ao clicar no botão
                estoqueViewModel.checkProductQuantities()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Checar Quantidades")
        }

        // Exibe as mensagens com delay
        LaunchedEffect(isLoading) {
            if (isLoading) {
                // Primeiro, espera 6 segundos para exibir a primeira mensagem
                delay(6000)
                showSecondMessage = true  // Exibe a segunda mensagem

                // Depois, espera mais 6 segundos para remover a segunda mensagem
                delay(6000)
                showSecondMessage = false

                // Finaliza o carregamento
                isLoading = false
            }
        }

        // Exibe a primeira mensagem de "Carregando..." por 6 segundos
        if (isLoading && !showSecondMessage) {
            ShowLoadingMessage()
        }

        // Exibe a segunda mensagem "Estamos quase lá!" por mais 6 segundos
        if (showSecondMessage) {
            ShowLoadingMessage2()
        }

        // Exibe os alertas de quantidade dos produtos após as mensagens
        if (!isLoading && !showSecondMessage) {
            productQuantities?.let { quantities ->
                quantities.forEach { (productName, quantity) ->
                    if (quantity == 3) {
                        AlertaCritico(productName)
                    } else if (quantity in 4..5) {
                        AlertaEmAtencao()
                    }
                }
            }
        }
    }
}

@Composable
fun ShowLoadingMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Carregando...\n",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShowLoadingMessage2() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Estamos quase lá!",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
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
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.mipmap.xzinho),
                contentDescription = "Alerta Crítico",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alerta Crítico! A quantidade do produto: $productName está em 3.",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
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
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.mipmap.trianguloyellow),
                contentDescription = "Alerta em Atenção",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alerta em atenção. Verifique as informações.",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
