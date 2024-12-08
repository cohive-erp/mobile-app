package br.com.cohive.estoque

import MyBottomNavigation
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import br.com.cohive.DataStoreManager
import br.com.cohive.EstoqueViewModelFactory
import br.com.cohive.R
import br.com.cohive.ui.theme.CohiveTheme

class EditProductActivity : ComponentActivity() {
    private lateinit var estoqueViewModel: EstoqueViewModel
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val productId = intent?.getIntExtra("PRODUCT_ID", -1)
        dataStoreManager = DataStoreManager(applicationContext) // Inicialize a classe auxiliar do DataStore.

        val factory = EstoqueViewModelFactory(dataStoreManager)
        estoqueViewModel = ViewModelProvider(this, factory).get(EstoqueViewModel::class.java)

        setContent {
            CohiveTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomNavigation(navController = navController) }
                ) { innerPadding ->
                    EditProductScreen(
                        productId = productId,
                        estoqueViewModel = estoqueViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: Int?,
    estoqueViewModel: EstoqueViewModel,
    modifier: Modifier = Modifier
) {
    // Estados para os campos
    var nomeProduto by remember { mutableStateOf("") }
    var fabricanteProduto by remember { mutableStateOf("") }
    var categoriaProduto by remember { mutableStateOf("") }
    var descricaoProduto by remember { mutableStateOf("") }
    var precoVendaProduto by remember { mutableStateOf("") }
    var precoCompraProduto by remember { mutableStateOf("") }
    var quantidadeProduto by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.ic_caixa),
                contentDescription = "Product Image",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campos de edição
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

            OutlinedTextField(
                value = fabricanteProduto,
                onValueChange = { fabricanteProduto = it },
                label = { Text("Fabricante") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF9D4FFF),
                    unfocusedBorderColor = Color(0xFF9D4FFF),
                    cursorColor = Color(0xFF9D4FFF),
                    focusedLabelColor = Color(0xFF9D4FFF)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            OutlinedTextField(
                value = descricaoProduto,
                onValueChange = { descricaoProduto = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF9D4FFF),
                    unfocusedBorderColor = Color(0xFF9D4FFF),
                    cursorColor = Color(0xFF9D4FFF),
                    focusedLabelColor = Color(0xFF9D4FFF)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = precoVendaProduto,
                onValueChange = { precoVendaProduto = it },
                label = { Text("Preço de Venda (BRL)") },
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

            OutlinedTextField(
                value = precoCompraProduto,
                onValueChange = { precoCompraProduto = it },
                label = { Text("Preço de Compra (BRL)") },
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

            // Botão para salvar as alterações
            Button(
                onClick = {
                    // Criação do DTO de edição do produto
                    val loja = Loja(
                        idLoja = 1,
                        rua = "Rua Exemplo",
                        bairro = "Bairro Exemplo",
                        cidade = "Cidade Exemplo",
                        estado = "Estado Exemplo",
                        numero = 100,
                        cep = "00000-000",
                        cnpj = "00.000.000/0000-00"
                    )
                    val produtoEdicaoDto = ProdutoEdicaoDto(
                        nome = nomeProduto,
                        fabricante = fabricanteProduto,
                        categoria = categoriaProduto,
                        descricao = descricaoProduto,
                        precoVenda = precoVendaProduto.toDoubleOrNull() ?: 0.0,
                        precoCompra = precoCompraProduto.toDoubleOrNull() ?: 0.0,
                        quantidade = quantidadeProduto.toIntOrNull() ?: 0,
                        loja = loja
                    )

                    estoqueViewModel.editarProduto(productId ?: -1, produtoEdicaoDto) { sucesso ->
                        if (sucesso) {
                            Log.d("EditProductActivity", "Produto editado com sucesso")
                        } else {
                            Log.e("EditProductActivity", "Erro ao editar o produto")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF))
            ) {
                Text(text = "Salvar", color = Color.White)
            }
        }
    }
}
