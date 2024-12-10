package br.com.cohive.estoque

import MyBottomNavigation
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import br.com.cohive.DataStoreManager
import br.com.cohive.R
import br.com.cohive.ui.theme.CohiveTheme
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CadastroProdutoActivity : ComponentActivity() {
    private lateinit var estoqueViewModel: EstoqueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStoreManager = DataStoreManager(applicationContext)
        val factory = EstoqueViewModelFactory(dataStoreManager)
        estoqueViewModel = ViewModelProvider(this, factory).get(EstoqueViewModel::class.java)

        setContent {
            CohiveTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomNavigation(navController = navController) }
                ) { innerPadding ->
                    TelaCadastroProduto(
                        modifier = Modifier.padding(innerPadding),
                        onCadastrarProduto = { ean, nome, precoVenda, precoCompra, quantidade ->
                            lifecycleScope.launch {
                                estoqueViewModel.cadastrarProdutoComEAN(ean, nome, precoVenda, precoCompra, quantidade)
                            }
                        }
                    )
                }
            }
        }

        estoqueViewModel.produtoCadastroStatus.observe(this, Observer { sucesso ->
            if (sucesso) {
                // Exibe um toast de sucesso
                Toast.makeText(this, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                // Após o cadastro bem-sucedido, redireciona para a EstoqueActivity
                val intent = Intent(this, EstoqueActivity::class.java)
                startActivity(intent)

                // Finaliza a tela de cadastro para não deixar ela na pilha de atividades
                finish()
            } else {
                Toast.makeText(this, "Erro ao cadastrar o produto.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



@Composable
fun TelaCadastroProduto(
    modifier: Modifier = Modifier,
    onCadastrarProduto: (String, String, Double, Double, Int) -> Unit
) {
    var codigoEan by remember { mutableStateOf("") }
    var nomeProduto by remember { mutableStateOf("") }
    var precoVenda by remember { mutableStateOf("") }
    var precoCompra by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Câmera dentro de uma caixa no topo
        Box(
            modifier = Modifier
                .size(300.dp) // Tamanho fixo da visualização da câmera
                .clip(RoundedCornerShape(16.dp)) // Canto arredondado
                .border(4.dp, Color(0xFF9D4FFF), RoundedCornerShape(16.dp)) // Borda roxa
        ) {
            CameraPreviewView(
                onBarcodeDetected = { barcode ->
                    codigoEan = barcode.displayValue ?: ""
                },
                context = LocalContext.current
            )
        }

        // Campos de cadastro do produto abaixo da câmera
        OutlinedTextField(
            value = codigoEan,
            onValueChange = { codigoEan = it },
            label = { Text("Código EAN") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nomeProduto,
            onValueChange = { nomeProduto = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = precoVenda,
            onValueChange = { precoVenda = it },
            label = { Text("Preço de Venda") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = precoCompra,
            onValueChange = { precoCompra = it },
            label = { Text("Preço de Compra") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text("Quantidade") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val precoVendaDouble = precoVenda.toDoubleOrNull() ?: 0.0
                val precoCompraDouble = precoCompra.toDoubleOrNull() ?: 0.0
                val quantidadeInt = quantidade.toIntOrNull() ?: 0
                onCadastrarProduto(codigoEan, nomeProduto, precoVendaDouble, precoCompraDouble, quantidadeInt)
            },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D4FFF)),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Cadastrar Produto")
        }
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreviewView(
    onBarcodeDetected: (Barcode) -> Unit,
    context: android.content.Context
) {
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val executor: ExecutorService = Executors.newSingleThreadExecutor()

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(executor) { imageProxy ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
                processBarcodeImage(image, onBarcodeDetected) { e -> e.printStackTrace() }
            }
            imageProxy.close()
        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageAnalysis
            )
        } catch (exc: Exception) {
            exc.printStackTrace()
        }

        onDispose {
            cameraProvider.unbindAll()
            executor.shutdown()
        }
    }
}

fun processBarcodeImage(image: InputImage, onSuccess: (Barcode) -> Unit, onError: (Exception) -> Unit) {
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    val scanner = BarcodeScanning.getClient(options)

    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            if (barcodes.isNotEmpty()) {
                onSuccess(barcodes[0])
            }
        }
        .addOnFailureListener { e -> onError(e) }
}
