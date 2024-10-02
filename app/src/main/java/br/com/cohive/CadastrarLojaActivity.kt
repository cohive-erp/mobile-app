package br.com.cohive

import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cohive.ui.theme.CohiveTheme

class CadastrarLojaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CohiveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaCadastroLoja(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroLoja(modifier: Modifier = Modifier) {
    var endereco by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    val context = LocalContext.current

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
            painter = painterResource(id = R.drawable.logo_cohive), // Substitua pelo logo da sua aplicação
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
            value = endereco,
            onValueChange = { endereco = it },
            label = { Text("Endereço") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
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
                // Ação ao clicar no botão Cadastrar
                val intent = Intent(context, LoginActivity::class.java) // Altere conforme necessário
                context.startActivity(intent)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCadastrarLoja() {
    CohiveTheme {
        TelaCadastroLoja()
    }
}
