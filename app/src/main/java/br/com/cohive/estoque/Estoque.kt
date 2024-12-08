package br.com.cohive.estoque

data class EstoqueListagemDto(
    val dataEntradaInicial: String, // LocalDateTime formatado como String
    val produto: Produto,           // Relacionado à entidade Produto
    val loja: Loja,                 // Relacionado à entidade Loja
    val quantidade: Int,
    val isDeleted: Boolean // Continua para o EstoqueListagemDto
)

data class Produto(
    val idProduto: Int,
    val nome: String,
    val fabricante: String,
    val categoria: String,
    val descricao: String,
    val precoVenda: Double,
    val precoCompra: Double,
    val deleted: Boolean // Adicionando campo deleted
)

data class EANCriacaoDto(
    val ean: String, // Usando LocalDateTime como String
    val nome: String, // Relacionado à entidade Produto
    val precoVenda: Double,
    val precoCompra: Double,// Relacionado à entidade Loja
    val quantidade: Int,
    val loja: Loja // Relacionado à entidade Loja
    )

data class ProdutoEdicaoDto(
    val nome: String,
    val fabricante: String,
    val categoria: String,
    val descricao: String,
    val precoVenda: Double,
    val precoCompra: Double,
    val quantidade: Int,
    val loja: Loja // A loja associada ao produto
)

data class ProdutoCriacaoDto(
    val nome: String,
    val fabricante: String,
    val categoria: String,
    val descricao: String,
    val precoVenda: Double,
    val precoCompra: Double,
    val quantidade: Int,
    val loja: Loja // A loja associada ao produto
)


data class Loja(
    val idLoja: Int,
    val rua: String,
    val bairro: String,
    val cidade: String,
    val estado: String,
    val numero: Int,
    val cep: String,
    val cnpj: String
)

data class EstoqueAtualizacaoDto(
    val dataEntradaInicial: String, // Usando LocalDateTime como String
    val produto: Produto, // Certifique-se de que a classe Produto esteja definida corretamente
    val loja: Loja?, // Certifique-se de que a classe Loja esteja definida corretamente
    val quantidade: Int
)


