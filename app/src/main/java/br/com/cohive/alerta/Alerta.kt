package br.com.cohive.alerta

data class AlertResponse(
    val quantities: Map<String, Int>,
    val alert: Alerta? // Você pode criar a classe Alerta se necessário
)

data class Alerta(
    val tipo: String,
    val data: String, // Pode ser LocalDateTime ou String dependendo do que você quiser
    val mensagem: String,
)
