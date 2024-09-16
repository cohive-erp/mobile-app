package br.com.cohive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

class CadastroUsuarioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CohiveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaCadastroUser(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroUser(modifier: Modifier = Modifier) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var isTermsAccepted by remember { mutableStateOf(false) }
    var showLgpdDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Substituindo o título "cohive" pela imagem do logo
        Image(
            painter = painterResource(id = R.drawable.logo_cohive), // Substitua pelo seu logo
            contentDescription = "Logo COHIVE",
            modifier = Modifier
                .height(100.dp)
                .width(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Inicie seu cadastro",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome completo") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
        )

        OutlinedTextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9D4FFF),
                unfocusedBorderColor = Color(0xFF9D4FFF),
                cursorColor = Color(0xFF9D4FFF),
                focusedLabelColor = Color(0xFF9D4FFF)
            )
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isTermsAccepted,
                onCheckedChange = { isTermsAccepted = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF9D4FFF))
            )
            Text(
                text = "Aceito os termos da LGPD",
                color = Color(0xFF9D4FFF),
                modifier = Modifier
                    .clickable { showLgpdDialog = true }
                    .padding(start = 8.dp)
            )
        }

        if (showLgpdDialog) {
            AlertDialog(
                onDismissRequest = { showLgpdDialog = false },
                confirmButton = {
                    TextButton(onClick = { showLgpdDialog = false }) {
                        Text("Fechar")
                    }
                },
                title = { Text("Termos da LGPD") },
                text = {
                    LazyColumn(
                        modifier = Modifier.height(300.dp)
                    ) {
                        item {
                            Text(
                                text = """
                                A COHIVE valoriza a privacidade dos seus usuários e se compromete a proteger os dados pessoais de acordo com a Lei Geral de Proteção de Dados (LGPD), Lei nº 13.709/2018. Esta política de privacidade explica como coletamos, utilizamos e protegemos suas informações.

                                1. Coleta de Dados
                                Durante o uso do app COHIVE, podemos coletar as seguintes informações:
                                - Dados pessoais: Nome, e-mail, telefone e outros dados fornecidos voluntariamente.
                                - Dados de navegação: Informações como endereço IP, tipo de dispositivo, sistema operacional e geolocalização, quando autorizados.

                                2. Uso dos Dados
                                Utilizamos os dados coletados para:
                                - Oferecer e melhorar nossos serviços.
                                - Enviar atualizações, notificações e informações relevantes sobre o evento ou outros serviços da COHIVE.
                                - Personalizar a interface e o conteúdo ao perfil de uso do usuário.

                                3. Compartilhamento de Dados
                                A COHIVE não compartilha dados pessoais com terceiros, exceto em casos de:
                                - Cumprimento de obrigações legais.
                                - Fornecimento de serviços essenciais por terceiros.

                                4. Direitos do Usuário
                                - Acessar seus dados: Solicitar uma cópia.
                                - Retificar informações: Corrigir dados incorretos.
                                - Excluir dados: Solicitar exclusão, conforme legalidade.

                                5. Armazenamento e Segurança
                                - Armazenamento seguro e proteção técnica e administrativa.

                                6. Alterações na Política
                                - Notificaremos sobre mudanças por meio do app.

                                7. Contato
                                - Dúvidas? Entre em contato: cohive.you@gmail.com
                                """.trimIndent()
                            )
                        }
                    }
                }
            )
        }

        Button(
            onClick = { /* Ação de avançar */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9D4FFF)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = isTermsAccepted
        ) {
            Text(text = "Avançar", color = Color.White, fontSize = 18.sp)
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
fun GreetingCadastroUser() {
    CohiveTheme {
        TelaCadastroUser()
    }
}
