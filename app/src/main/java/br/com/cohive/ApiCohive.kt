package br.com.cohive

import br.com.cohive.estoque.EANCriacaoDto
import br.com.cohive.estoque.EstoqueAtualizacaoDto
import br.com.cohive.estoque.EstoqueListagemDto
import br.com.cohive.estoque.ProdutoCriacaoDto
import br.com.cohive.estoque.ProdutoEdicaoDto
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
import java.math.BigDecimal

interface ApiCohive {

    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<UsuarioTokenDto>

    @POST("usuarios/cadastro")
    suspend fun cadastrar(@Body usuario: UsuarioCriacaoDto): Response<Usuario>

    // Endpoint para cadastrar uma nova loja
    @POST("lojas")
    suspend fun cadastrarLoja(@Body loja: LojaCriacaoDto): Response<Loja>

    @GET("estoque/trazer-estoque/{lojaId}")
    suspend fun listarEstoque(@Path("lojaId") lojaId: Int): Response<List<EstoqueListagemDto>>

    @PUT("estoque/{id}")
    suspend fun deletarProduto(@Path("id") id: Int): Response<Void>

    @PUT("estoque/baixa-estoque")
    suspend fun darBaixaProduto(@Body estoqueAtualizacaoDto: EstoqueAtualizacaoDto): Response<EstoqueListagemDto>

    @PUT("estoque/entrada-estoque")
    suspend fun darEntradaProduto(@Body estoqueAtualizacaoDto: EstoqueAtualizacaoDto): Response<EstoqueListagemDto>

    @POST("estoque")
    suspend fun cadastrarProduto(@Body produtoCriacaoDto: ProdutoCriacaoDto): Response<Void>

    @POST("estoque/preencher-produto")
    suspend fun preencherProduto(@Body eanCriacaoDto: EANCriacaoDto): Response<Void>

    @PUT("estoque/atualizar-produto/{id}")
    suspend fun editarProduto(
        @Path("id") produtoId: Int,
        @Body produtoEdicaoDto: ProdutoEdicaoDto
    ): Response<Void>

    @GET("estoque/checar-quantidade-dos-produtos/{id}")
    suspend fun checkProductQuantities(@Path("id") userId: Int): Response<Map<String, Any>>

    @GET("relatorios/faturas-mensais/{lojaId}")
    fun getMonthlyInvoicesForLastSixMonthsByLoja(@Path("lojaId") lojaId: Int): Call<List<BigDecimal>>

}
