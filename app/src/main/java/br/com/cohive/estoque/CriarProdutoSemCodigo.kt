package br.com.cohive.estoque

import MyBottomNavigation
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import br.com.cohive.R
import br.com.cohive.ui.theme.CohiveTheme

class CriarProdutoSemCodigo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val productId = intent?.getIntExtra("PRODUCT_ID", -1)

        setContent {
            CohiveTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomNavigation(navController = rememberNavController()) }
                ) { innerPadding ->
                    CriarProdutoScreen(
                        productId = productId,
                        modifier = Modifier.padding(innerPadding),
                        activity = this // Passando a Activity atual para navegação manual
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarProdutoScreen(
    productId: Int?,
    modifier: Modifier = Modifier,
    activity: ComponentActivity,
    estoqueViewModel: EstoqueViewModel = viewModel() // Obtendo o ViewModel via Jetpack Compose
) {
    // Estados para os campos
    var nomeProduto by remember { mutableStateOf("") }
    var fabricanteProduto by remember { mutableStateOf("") }
    var categoriaProduto by remember { mutableStateOf("") }
    var descricaoProduto by remember { mutableStateOf("") }
    var precoVendaProduto by remember { mutableStateOf("") }
    var precoCompraProduto by remember { mutableStateOf("") }
    var quantidadeProduto by remember { mutableStateOf("") }

    // Seleção da loja (substitua por seu método de seleção de loja)
    val lojaSelecionada = Loja(
        idLoja = 1, // Exemplo, você deve substituir com a loja selecionada pelo usuário
        rua = "Rua Exemplo",
        bairro = "Bairro Exemplo",
        cidade = "Cidade Exemplo",
        estado = "Estado Exemplo",
        numero = 100,
        cep = "00000-000",
        cnpj = "00.000.000/0000-00"
    )

    // Habilita o scroll vertical
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState), // Habilitando rolagem vertical
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagem do produto
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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Fabricante
        OutlinedTextField(
            value = fabricanteProduto,
            onValueChange = { fabricanteProduto = it },
            label = { Text("Fabricante") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Categoria
        OutlinedTextField(
            value = categoriaProduto,
            onValueChange = { categoriaProduto = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Descrição
        OutlinedTextField(
            value = descricaoProduto,
            onValueChange = { descricaoProduto = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Preço de Venda
        OutlinedTextField(
            value = precoVendaProduto,
            onValueChange = { precoVendaProduto = it },
            label = { Text("Preço de Venda (BRL)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Preço de Compra
        OutlinedTextField(
            value = precoCompraProduto,
            onValueChange = { precoCompraProduto = it },
            label = { Text("Preço de Compra (BRL)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Quantidade
        OutlinedTextField(
            value = quantidadeProduto,
            onValueChange = { quantidadeProduto = it },
            label = { Text("Quantidade") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botão Salvar
        Button(
            onClick = {
                val produtoCriacaoDto = ProdutoCriacaoDto(
                    nome = nomeProduto,
                    fabricante = fabricanteProduto,
                    categoria = categoriaProduto,
                    descricao = descricaoProduto,
                    precoVenda = precoVendaProduto.toDouble(),
                    precoCompra = precoCompraProduto.toDouble(),
                    quantidade = quantidadeProduto.toInt(),
                    loja = lojaSelecionada // Loja associada
                )

                // Chamada ao ViewModel para cadastrar o produto
                estoqueViewModel.cadastrarProduto(
                    produtoCriacaoDto,
                    onSuccess = {
                        // Se o cadastro for bem-sucedido, redireciona para EstoqueActivity
                        val intent = Intent(activity, EstoqueActivity::class.java)
                        activity.startActivity(intent) // Navega para EstoqueActivity
                        activity.finish() // Finaliza a activity atual
                    },
                    onError = { errorMessage ->
                        println("Erro: $errorMessage") // Log de erro
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Salvar", color = Color.White)
        }
    }
}
