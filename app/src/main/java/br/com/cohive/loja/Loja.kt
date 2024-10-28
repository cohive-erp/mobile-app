package br.com.cohive.loja

import br.com.cohive.usuario.Usuario

data class LojaCriacaoDto(
    val numero: Int,
    val cep: String,
    val cnpj: String,
    val usuario: Usuario// ID do usuário que será associado à loja
)

data class Loja(
    val id: Int,
    val numero: Int,
    val cep: String,
    val cnpj: String,
    val usuario: Usuario,
    val isdeleted: Boolean
)

data class LojaDto(
    val id: Int, // ID da loja
    val numero: Int,
    val cep: String,
    val cnpj: String,
    val usuarioId: Int // ID do usuário associado à loja
)