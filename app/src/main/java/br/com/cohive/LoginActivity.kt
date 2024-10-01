package br.com.cohive

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cohive.ui.theme.CohiveTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CohiveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaLoginUser(
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToMenu = {
                            val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                            startActivity(intent)
                        },
                        onNavigateToCadastro = {
                            val intent = Intent(this@LoginActivity, CadastroUsuarioActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLoginUser(modifier: Modifier = Modifier, onNavigateToMenu: () -> Unit, onNavigateToCadastro: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

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
            modifier = Modifier
                .padding(top = 8.dp)
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
            onClick = { onNavigateToMenu() }, // Navega para o Menu
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLoginUser() {
    CohiveTheme {
        TelaLoginUser()
    }
}


