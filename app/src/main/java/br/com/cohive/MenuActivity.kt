package br.com.cohive

import MyBottomNavigation
import br.com.cohive.usuario.UsuarioViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import br.com.cohive.dashboard.DashboardActivity
import br.com.cohive.estoque.CriarProdutoSemCodigo
import br.com.cohive.estoque.EstoqueActivity
import br.com.cohive.ui.theme.CohiveTheme
import br.com.cohive.usuario.LoginActivity

class MenuActivity : ComponentActivity() {
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
                    Home(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Home(modifier: Modifier = Modifier) {
    val context = LocalContext.current // Obtenha o contexto aqui

    // Obtenha a ViewModel
    val userViewModel: UsuarioViewModel = viewModel()

    // Observe o nome do usuário logado
    val userName by userViewModel.nomeUsuario.observeAsState("Usuário")

    // Use LazyColumn para permitir rolagem
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Adiciona um espaço inferior
                contentAlignment = Alignment.Center // Centraliza o conteúdo
            ) {
                Text(
                    text = "Olá! Por onde quer começar?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            // Botão de cadastrar produto
            Button(
                onClick = {
                    val intent = Intent(context, CriarProdutoSemCodigo::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                ButtonContent(text = "Cadastrar produto", iconResId = R.mipmap.addproductwhite)
            }
        }

        item {
            // Botão de relatórios
            Button(
                onClick = {
                    val intent = Intent(context, DashboardActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                ButtonContent(text = "Relatórios", iconResId = R.mipmap.dashboardwhite)
            }
        }

        item {
            // Botão de estoque de produtos
            Button(
                onClick = {
                    val intent = Intent(context, EstoqueActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                ButtonContent(text = "Estoque de produtos", iconResId = R.mipmap.boxwhite)
            }
        }

        item {
            // Botão de buscar tendências
            Button(
                onClick = {
                    val intent = Intent(context, DashboardActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                ButtonContent(text = "Buscar tendências", iconResId = R.mipmap.trendwhite)
            }
        }

        item {
            // Botão de sair
            Button(
                onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.sairwhite),
                        contentDescription = "ícone de Sair",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Sair",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonContent(text: String, iconResId: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "Ícone",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingHome() {
    CohiveTheme {
        Home()
    }
}
