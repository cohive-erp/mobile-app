package br.com.cohive.estoque

import MyBottomNavigation
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import br.com.cohive.R
import br.com.cohive.ui.theme.CohiveTheme

class EstoqueActivity : ComponentActivity() {
    private val estoqueViewModel: EstoqueViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CohiveTheme {
                val navController = rememberNavController()
                val estoqueList by estoqueViewModel.estoqueList.observeAsState(emptyList())

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomNavigation(navController = navController) }
                ) { innerPadding ->
                    ProductScreen(
                        modifier = Modifier.padding(innerPadding),
                        products = estoqueList,
                        onEditClick = { productId ->
                            val intent = Intent(this, EditProductActivity::class.java).apply {
                                putExtra("PRODUCT_ID", productId)
                            }
                            startActivity(intent)
                        },
                        onDeleteClick = { productId ->
                            estoqueViewModel.deleteProduct(productId)
                        },
                        onEntradaClick = { produto, loja ->
                            val dataEntrada = estoqueList.first { it.produto.idProduto == produto.idProduto }.dataEntradaInicial
                            estoqueViewModel.darEntradaProduto(produto, loja, 1, dataEntrada) // Passando a data de entrada correta
                        },
                        onBaixaClick = { produto, loja ->
                            val dataEntrada1 = estoqueList.first(){ it.produto.idProduto == produto.idProduto }.dataEntradaInicial
                            estoqueViewModel.darBaixaProduto(produto, loja, 1, dataEntrada1)
                        }
                    )
                }

                LaunchedEffect(Unit) {
                    estoqueViewModel.fetchEstoque() // Chama a função para buscar os dados
                }
            }
        }
    }
}

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    products: List<EstoqueListagemDto>,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onEntradaClick: (Produto, Loja) -> Unit,
    onBaixaClick: (Produto, Loja) -> Unit
) {
    Column(modifier = modifier) {
        SearchBar()
        Spacer(modifier = Modifier.height(16.dp))
        CreateProductButton()
        Spacer(modifier = Modifier.height(16.dp))
        ProductList(products, onEditClick, onDeleteClick, onEntradaClick, onBaixaClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text("Buscar") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        IconButton(onClick = { /* Ação do filtro */ }) {
            Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color(0xFF9C27B0))
        }
    }
}

@Composable
fun CreateProductButton() {
    val context = LocalContext.current

    Button(
        onClick = {
            val intent = Intent(context, CriarProdutoSemCodigo::class.java)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = "Criar Novo Produto", color = Color.White)
    }
}

@Composable
fun ProductList(
    products: List<EstoqueListagemDto>,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onEntradaClick: (Produto, Loja) -> Unit,
    onBaixaClick: (Produto, Loja) -> Unit
) {
    LazyColumn {
        items(products) { productEstoque ->
            ProductItem(
                title = productEstoque.produto.nome,
                category = productEstoque.produto.categoria,
                fabricante = productEstoque.produto.fabricante,
                quantidade = productEstoque.quantidade,
                produto = productEstoque.produto,
                loja = productEstoque.loja,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onEntradaClick = onEntradaClick,
                onBaixaClick = onBaixaClick
            )
        }
    }
}

@Composable
fun ProductItem(
    title: String,
    category: String,
    fabricante: String,
    quantidade: Int,
    produto: Produto,
    loja: Loja,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onEntradaClick: (Produto, Loja) -> Unit,
    onBaixaClick: (Produto, Loja) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                onDeleteClick(produto.idProduto)
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_caixa),
            contentDescription = "Product Image",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Categoria: $category", fontSize = 14.sp, color = Color.Gray)
            Text(text = "Fabricante: $fabricante", fontSize = 14.sp, color = Color.Gray)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Qtd: $quantidade", fontWeight = FontWeight.Bold, color = Color(0xFF9C27B0))
            Row {
                IconButton(onClick = { onEntradaClick(produto, loja) }) {
                    Icon(Icons.Default.Add, contentDescription = "Incrementar", tint = Color.Green)
                }
                IconButton(onClick = { onBaixaClick(produto, loja) }) {
                    Icon(Icons.Default.Remove, contentDescription = "Baixar", tint = Color.Red)
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = { onEditClick(produto.idProduto) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF9C27B0))
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Excluir Item") },
        text = { Text("Você tem certeza que deseja excluir este item?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sim")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Não")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    CohiveTheme {
        ProductScreen(
            products = emptyList(),
            onEditClick = {},
            onDeleteClick = {},
            onEntradaClick = { _, _ -> },
            onBaixaClick = { _, _ -> }
        )
    }
}
