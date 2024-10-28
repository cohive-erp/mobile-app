package br.com.cohive.usuario

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cohive.R
import br.com.cohive.loja.CadastrarLojaActivity
import br.com.cohive.ui.theme.CohiveTheme

class CadastroUsuarioActivity : ComponentActivity() {
    private val usuarioViewModel: UsuarioViewModel by viewModels() // Instância do ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CohiveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaCadastroUser(
                        modifier = Modifier.padding(innerPadding),
                        usuarioViewModel = usuarioViewModel // Passando o ViewModel para a tela
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroUser(modifier: Modifier = Modifier, usuarioViewModel: UsuarioViewModel) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var isTermsAccepted by remember { mutableStateOf(false) }
    var showLgpdDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Observando o resultado do cadastro
    usuarioViewModel.isSuccessful.observe(LocalLifecycleOwner.current) { isSuccessful ->
        if (isSuccessful) {
            // Obtenha os dados do usuário
            val usuarioCompleto = usuarioViewModel.cadastrarResult.value ?: return@observe

            // Navega para a próxima atividade após cadastro bem-sucedido
            val intent = Intent(context, CadastrarLojaActivity::class.java).apply {
                putExtra("USUARIO_ID", usuarioCompleto.id)
                putExtra("USUARIO_NOME", usuarioCompleto.nome)
                putExtra("USUARIO_CELULAR", usuarioCompleto.numeroCelular)
                putExtra("USUARIO_EMAIL", usuarioCompleto.email)
                putExtra("USUARIO_SENHA", usuarioCompleto.senha) // Passando a senha criptografada
            }
            context.startActivity(intent)
            // Finaliza a atividade atual para não voltar ao cadastro
            (context as CadastroUsuarioActivity).finish()
        } else {
            // Tratar erro de cadastro (por exemplo, mostrar mensagem de erro)
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

        // Substituindo o título "cohive" pela imagem do logo
        Image(
            painter = painterResource(id = R.drawable.logo_cohive), // Substitua pelo seu logo
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .height(100.dp)
                .width(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.cadastro), // "Inicie seu cadastro"
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text(stringResource(R.string.nome_completo)) }, // "Nome completo"
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
            label = { Text(stringResource(R.string.telefone)) }, // "Telefone"
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
            label = { Text(stringResource(R.string.email)) }, // "E-mail"
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
            label = { Text(stringResource(R.string.senha)) }, // "Senha"
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
                text = stringResource(R.string.aceito_os_termos_da_lgpd), // "Aceito os termos da LGPD"
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
                        Text(stringResource(R.string.fechar)) // "Fechar"
                    }
                },
                title = { Text(stringResource(R.string.termos_da_lgpd)) }, // "Termos da LGPD"
                text = {
                    LazyColumn(
                        modifier = Modifier.height(300.dp)
                    ) {
                        item {
                            Text(text = stringResource(R.string.termos_lgpd)) // Termos LGPD
                        }
                    }
                }
            )
        }

        Button(
            onClick = {
                // Chamando a função de cadastro no ViewModel
                val usuario = UsuarioCriacaoDto(
                    nome = nome,
                    numeroCelular = telefone,
                    email = email,
                    senha = senha
                )
                usuarioViewModel.cadastrar(usuario) // Função de cadastro
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9D4FFF)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = nome.isNotBlank() && telefone.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && isTermsAccepted
        ) {
            Text(text = stringResource(R.string.cadastrar), color = Color.White, fontSize = 18.sp) // "Cadastrar"
        }

        Text(
            text = stringResource(R.string.ja_tenho_uma_conta), // "Já tenho uma conta"
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
        TelaCadastroUser(usuarioViewModel = UsuarioViewModel())
    }
}