package br.com.cohive.estoque

import MyBottomNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import br.com.cohive.R
import br.com.cohive.ui.theme.CohiveTheme

class EditProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Pegando o ID do produto para ser editado
        val productId = intent?.getIntExtra("PRODUCT_ID", -1)

        setContent {
            CohiveTheme {
                val navController = rememberNavController() // Lembre-se de importar isso

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomNavigation(navController = navController) } // Adicionando a barra de navegação
                ) { innerPadding ->
                    EditProductScreen(
                        productId = productId,
                        modifier = Modifier.padding(innerPadding) // Aplicando padding para o conteúdo não colidir com a barra de navegação
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(productId: Int?, modifier: Modifier = Modifier) {
    // Estados para os campos
    var nomeProduto by remember { mutableStateOf("") }
    var categoriaProduto by remember { mutableStateOf("") }
    var precoProduto by remember { mutableStateOf("") }
    var quantidadeProduto by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagem do produto no topo e centralizada
        Image(
            painter = painterResource(id = R.drawable.ic_caixa),
            contentDescription = "Product Image",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Nome
        OutlinedTextField(
            value = nomeProduto,
            onValueChange = { nomeProduto = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Categoria
        OutlinedTextField(
            value = categoriaProduto,
            onValueChange = { categoriaProduto = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Preço
        OutlinedTextField(
            value = precoProduto,
            onValueChange = { precoProduto = it },
            label = { Text("Preço (BRL)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Quantidade
        OutlinedTextField(
            value = quantidadeProduto,
            onValueChange = { quantidadeProduto = it },
            label = { Text("Quantidade") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botão Salvar
        Button(
            onClick = { /* Ação de cadastro */ },
            modifier = Modifier.fillMaxWidth(0.9f), // Largura reduzida
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Salvar", color = Color.White)
        }
    }
}
