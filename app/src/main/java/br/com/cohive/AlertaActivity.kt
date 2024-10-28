package br.com.cohive

import MyBottomNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import br.com.cohive.ui.theme.CohiveTheme

class AlertaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CohiveTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = { MyBottomNavigation(navController = navController) }
                ) { innerPadding ->
                    SearchComponent(
                        modifier = Modifier.padding(innerPadding) ,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComponent(modifier: Modifier = Modifier) {
    var buscar by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = buscar,
                onValueChange = { buscar = it },
                label = { Text("Buscar") },
                modifier = Modifier
                    .width(270.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF9D4FFF),
                    unfocusedBorderColor = Color(0xFF9D4FFF),
                    cursorColor = Color(0xFF9D4FFF),
                    focusedLabelColor = Color(0xFF9D4FFF)
                )
            )
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF9D4EDD))
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.funil), // Substitua por sua imagem
                    contentDescription = "Buscar icon",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        AlertaCritico()
        AlertaEmAtencao()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    CohiveTheme {
        SearchComponent()
    }
}

@Composable
fun AlertaCritico() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFD32F2F))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.xzinho),
                contentDescription = "Alerta Crítico",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alerta Crítico! Ação imediata necessária.",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun AlertaEmAtencao() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFA000))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.trianguloyellow),
                contentDescription = "Alerta em Atenção",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alerta em atenção. Verifique as informações.",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}