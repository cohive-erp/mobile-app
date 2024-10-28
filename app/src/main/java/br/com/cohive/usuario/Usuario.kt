package br.com.cohive.usuario

data class LoginRequest(
    val email: String,
    val senha: String
)

data class UsuarioCriacaoDto(
    val nome: String,
    val numeroCelular: String,
    val email: String,
    val senha: String
)

data class UsuarioTokenDto(
    val userId: Int,
    val nome: String,
    val email: String,
    val token: String
)

data class Usuario(
    val id: Int,
    val nome: String,
    val numeroCelular: String,
    val email: String,
    val senha: String,
    val isdeleted: Boolean
)

data class UsuarioResponseDto(
    val id: Int,
    val nome: String,
    val numeroCelular: String,
    val email: String,
    val senha: String
)

