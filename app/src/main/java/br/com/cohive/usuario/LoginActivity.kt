package br.com.cohive.usuario

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import br.com.cohive.DataStoreManager
import br.com.cohive.MenuActivity
import br.com.cohive.R
import br.com.cohive.ui.theme.CohiveTheme

class LoginActivity : ComponentActivity() {
    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)

        // Inicializando o DataStoreManager
        dataStoreManager = DataStoreManager(applicationContext)

        setContent {
            CohiveTheme {
                TelaLoginUser(
                    onLogin = { email, senha ->
                        usuarioViewModel.login(LoginRequest(email, senha), dataStoreManager)
                    },
                    onNavigateToCadastro = {
                        val intent = Intent(this@LoginActivity, CadastroUsuarioActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }

        // Observando o resultado do login
        usuarioViewModel.loginResult.observe(this) { tokenDto ->
            // Navega para o MenuActivity após login bem-sucedido
            if (tokenDto != null) {
                startActivity(Intent(this, MenuActivity::class.java))
                finish() // opcional: fecha a LoginActivity
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLoginUser(
    modifier: Modifier = Modifier,
    onLogin: (String, String) -> Unit,
    onNavigateToCadastro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_cohive),
            contentDescription = "Logo COHIVE",
            modifier = Modifier
                .height(100.dp)
                .width(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Bem vindo, faça seu login!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Entre para gerenciar seus produtos.",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
        )

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = { onLogin(email, senha) }, // Chama a função de login
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9D4FFF)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Entrar", color = Color.White, fontSize = 18.sp)
        }

        // Exibir mensagem de erro, se houver
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Text(
            text = "Ainda não tenho uma conta",
            fontSize = 16.sp,
            color = Color(0xFF9D4FFF),
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onNavigateToCadastro() } // Navega para a tela de cadastro
        )

        Text(
            text = "Esqueci minha senha",
            fontSize = 16.sp,
            color = Color(0xFF9D4FFF),
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }
}
