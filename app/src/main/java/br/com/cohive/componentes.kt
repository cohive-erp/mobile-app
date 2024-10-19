import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.cohive.CadastroProdutoActivity
import br.com.cohive.DashboardActivity
import br.com.cohive.MenuActivity
import java.util.Locale

@Composable
fun MyBottomNavigation(navController: NavHostController) {
    data class NavItem(val name: String, val route: String, val icon: Int)

    val navItems = listOf(
        NavItem("Home", "menu", br.com.cohive.R.drawable.ic_home), // Mudamos a rota para "menu"
        NavItem("Analytics", "dashboard", br.com.cohive.R.drawable.ic_analytics), // Mudamos a rota para "dashboard"
        NavItem("QR Code", "qr_code", br.com.cohive.R.drawable.ic_qr_code),
        NavItem("Notifications", "notifications", br.com.cohive.R.drawable.ic_notifications),
        NavItem("Profile", "profile", br.com.cohive.R.drawable.ic_profile)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        color = Color.LightGray.copy(alpha = 0.8f) // Aumentando a clareza da barra
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ) {
            val currentBackStackEntry = navController.currentBackStackEntry
            val currentRoute = currentBackStackEntry?.destination?.route

            navItems.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(8.dp) // Padding ao redor do ícone
                        ) {
                            if (item.route == "qr_code") {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp) // Ajuste o tamanho conforme necessário
                                        .background(Color(0xFF9D4FFF), shape = RoundedCornerShape(15.dp)) // Fundo roxo
                                        .padding(10.dp), // Espaço interno
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = item.name,
                                        tint = Color.White
                                    )
                                }
                            } else {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.name,
                                    tint = Color(0xFF9D4FFF) // Ícones roxos
                                )
                            }
                        }
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        when (item.route) {
                            "menu" -> { // Corrigido para MenuActivity
                                val intent = Intent(navController.context, MenuActivity::class.java)
                                navController.context.startActivity(intent)
                            }
                            "dashboard" -> { // Corrigido para DashboardActivity
                                val intent = Intent(navController.context, DashboardActivity::class.java)
                                navController.context.startActivity(intent)
                            }
                            "qr_code" -> {
                                val intent = Intent(navController.context, CadastroProdutoActivity::class.java)
                                navController.context.startActivity(intent)
                            }
                        }
                    },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF9D4FFF),
                        unselectedIconColor = Color(0xFF9D4FFF)
                    )
                )

                // Barra de seleção
                if (currentRoute == item.route) {
                    Box(
                        modifier = Modifier
                            .size(24.dp, 4.dp)
                            .background(Color(0xFF9D4FFF), shape = RoundedCornerShape(2.dp))
                            .align(Alignment.Bottom)
                    )
                }
            }
        }
    }
}

