package br.com.cohive

import br.com.cohive.estoque.EstoqueAtualizacaoDto
import br.com.cohive.estoque.EstoqueListagemDto
import br.com.cohive.estoque.ProdutoCriacaoDto
import br.com.cohive.loja.Loja
import br.com.cohive.loja.LojaCriacaoDto
import br.com.cohive.usuario.LoginRequest
import br.com.cohive.usuario.Usuario
import br.com.cohive.usuario.UsuarioCriacaoDto
import br.com.cohive.usuario.UsuarioTokenDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiCohive {
    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<UsuarioTokenDto>

    @POST("usuarios/cadastro")
    suspend fun cadastrar(@Body usuario: UsuarioCriacaoDto): Response<Usuario>

    // Endpoint para cadastrar uma nova loja
    @POST("lojas")
    suspend fun cadastrarLoja(@Body loja: LojaCriacaoDto): Response<Loja>

    @GET("estoque")
    suspend fun listarEstoque(): Response<List<EstoqueListagemDto>>

    @PUT("estoque/{id}")
    suspend fun deletarProduto(@Path("id") id: Int): Response<Void>

    @PUT("estoque/baixa-estoque")
    suspend fun darBaixaProduto(@Body estoqueAtualizacaoDto: EstoqueAtualizacaoDto): Response<EstoqueListagemDto>

    @PUT("estoque/entrada-estoque")
    suspend fun darEntradaProduto(@Body estoqueAtualizacaoDto: EstoqueAtualizacaoDto): Response<EstoqueListagemDto>

    @POST("estoque")
    suspend fun cadastrarProduto(@Body produtoCriacaoDto: ProdutoCriacaoDto): Response<Void>


}