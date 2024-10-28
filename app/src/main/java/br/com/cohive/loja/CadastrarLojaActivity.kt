package br.com.cohive.loja

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cohive.R
import br.com.cohive.ui.theme.CohiveTheme
import br.com.cohive.usuario.LoginActivity
import br.com.cohive.usuario.Usuario

class CadastrarLojaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Recupera todas as informações do usuário do Intent
        val userId = intent.getIntExtra("USUARIO_ID", -1)
        val userName = intent.getStringExtra("USUARIO_NOME")
        val userCelular = intent.getStringExtra("USUARIO_CELULAR")
        val userEmail = intent.getStringExtra("USUARIO_EMAIL")
        val userSenha = intent.getStringExtra("USUARIO_SENHA")

        setContent {
            CohiveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaCadastroLoja(
                        modifier = Modifier.padding(innerPadding),
                        userId = userId,
                        userName = userName,
                        userEmail = userEmail,
                        userCelular = userCelular,
                        userSenha = userSenha
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroLoja(
    modifier: Modifier = Modifier,
    userId: Int?,
    userName: String?,
    userEmail: String?,
    userCelular: String?,
    userSenha: String?
) {
    var numero by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }

    val context = LocalContext.current
    val lojaViewModel: LojaViewModel = remember { LojaViewModel() }

    // Observe o resultado do cadastro
    val cadastrarResult by lojaViewModel.cadastrarResult.observeAsState()

    LaunchedEffect(cadastrarResult) {
        cadastrarResult?.let { loja ->
            if (loja != null) {
                // Cadastro realizado com sucesso, redireciona para a tela de login
                context.startActivity(Intent(context, LoginActivity::class.java))
                // Opcional: finalizar a atividade atual
                (context as? Activity)?.finish()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo_cohive),
            contentDescription = "Logo Cohive",
            modifier = Modifier
                .height(100.dp)
                .width(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Cadastrar loja",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        OutlinedTextField(
            value = numero,
            onValueChange = { numero = it },
            label = { Text("Número") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
        )

        OutlinedTextField(
            value = cep,
            onValueChange = { cep = it },
            label = { Text("CEP") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
        )

        OutlinedTextField(
            value = cnpj,
            onValueChange = { cnpj = it },
            label = { Text("CNPJ") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Cadastrar
        Button(
            onClick = {
                // Verifica se as informações do usuário não são nulas antes de criar a loja
                val usuario = Usuario(
                    id = userId ?: throw IllegalArgumentException("User ID não pode ser nulo"),
                    nome = userName ?: throw IllegalArgumentException("Nome do usuário não pode ser nulo"),
                    numeroCelular = userCelular ?: throw IllegalArgumentException("Número de celular não pode ser nulo"),
                    email = userEmail ?: throw IllegalArgumentException("E-mail do usuário não pode ser nulo"),
                    senha = userSenha ?: throw IllegalArgumentException("Senha não pode ser nula"),
                    isdeleted = false
                )

                // Cria o DTO da loja, passando o objeto UsuarioResponseDto
                val loja = LojaCriacaoDto(
                    numero = numero.toIntOrNull() ?: throw IllegalArgumentException("Número deve ser um inteiro válido"),
                    cep = cep,
                    cnpj = cnpj,
                    usuario = usuario  // Passa o objeto completo do usuário
                )

                // Cadastra a loja usando o ViewModel
                lojaViewModel.cadastrar(loja)
                // Lógica adicional para lidar com o resultado do cadastro, se necessário
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9D4FFF)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Cadastrar", color = Color.White, fontSize = 18.sp)
        }


        // Botão Voltar
        Button(
            onClick = {
                // Ação ao clicar no botão Voltar
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Voltar", color = Color.White, fontSize = 18.sp)
        }

        Text(
            text = "Já tenho uma conta",
            fontSize = 16.sp,
            color = Color(0xFF9D4FFF),
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { /* Ação de login */ }
        )
    }
}
