package br.com.cohive.usuario

import br.com.cohive.estoque.Loja

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
    val token: String,
    val loja: Loja
)

data class Usuario(
    val id: Int,
    val nome: String,
    val numeroCelular: String,
    val email: String,
    val senha: String,
    val loja: Loja?,
    val isdeleted: Boolean,
)

data class UsuarioResponseDto(
    val id: Int,
    val nome: String,
    val numeroCelular: String,
    val email: String,
    val senha: String
)

