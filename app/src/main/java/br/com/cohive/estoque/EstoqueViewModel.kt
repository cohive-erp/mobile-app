package br.com.cohive.estoque

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cohive.RetrofitService
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class EstoqueViewModel : ViewModel() {
    private val _estoqueList = MutableLiveData<List<EstoqueListagemDto>>()
    val estoqueList: LiveData<List<EstoqueListagemDto>> = _estoqueList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _productQuantities = MutableLiveData<Map<String, Int>>()
    val productQuantities: LiveData<Map<String, Int>> get() = _productQuantities

    private val gson = Gson()

    private val _monthlyInvoices = MutableLiveData<List<BigDecimal>>()
    val monthlyInvoices: LiveData<List<BigDecimal>> get() = _monthlyInvoices

    // Função para buscar estoque
    fun fetchEstoque() {
        viewModelScope.launch {
            try {
                val response = RetrofitService.api.listarEstoque()
                if (response.isSuccessful) {
                    val originalList = response.body() ?: emptyList()

                    // Log da lista original recebida da API
                    Log.d("EstoqueViewModel", "Lista original recebida da API: $originalList")

                    // Filtrar os produtos que não estão marcados como deletados (campo deleted = false)
                    val filteredProducts = originalList.filter { estoque ->
                        val isEstoqueDeleted = estoque.isDeleted
                        val isProdutoDeleted = estoque.produto.deleted

                        // Log de verificação de cada item
                        Log.d("EstoqueViewModel", "Verificando Estoque: ${estoque.produto.nome}, isEstoqueDeleted: $isEstoqueDeleted, isProdutoDeleted: $isProdutoDeleted")

                        // Filtrar apenas quando ambos estão ativos (não deletados)
                        !isEstoqueDeleted && !isProdutoDeleted
                    }

                    // Log da lista filtrada
                    Log.d("EstoqueViewModel", "Lista filtrada (deleted = false): $filteredProducts")

                    // Posta a nova lista filtrada
                    _estoqueList.postValue(filteredProducts)
                } else {
                    handleError(response.errorBody()?.string() ?: "Erro desconhecido")
                }
            } catch (e: Exception) {
                handleError(e.message ?: "Erro desconhecido")
            }
        }
    }

    // Função para cadastrar um novo produto
    fun cadastrarProduto(produtoCriacaoDto: ProdutoCriacaoDto, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitService.api.cadastrarProduto(produtoCriacaoDto)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Erro ao cadastrar produto")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }

    // Função para deletar um produto
    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitService.api.deletarProduto(productId)
                if (response.isSuccessful) {
                    fetchEstoque() // Atualiza a lista de estoque após a exclusão
                } else {
                    handleError(response.errorBody()?.string() ?: "Erro desconhecido")
                }
            } catch (e: Exception) {
                handleError(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun editarProduto(produtoId: Int, produtoEdicaoDto: ProdutoEdicaoDto, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Log dos dados que estão sendo enviados
                Log.d("EstoqueViewModel", "ProdutoId: $produtoId, Dados: $produtoEdicaoDto")

                // Faz a requisição para editar o produto
                val response: Response<Void> = RetrofitService.api.editarProduto(produtoId, produtoEdicaoDto)
                if (response.isSuccessful) {
                    // Produto editado com sucesso
                    onResult(true)
                } else {
                    // Log do código de erro e mensagem
                    Log.e("EstoqueViewModel", "Erro ao editar o produto: ${response.code()} - ${response.message()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                // Lidar com erro de requisição
                Log.e("EstoqueViewModel", "Erro de requisição: ${e.message}")
                onResult(false)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun darEntradaProduto(produto: Produto, loja: Loja, quantidade: Int, dataEntrada: String) {
        viewModelScope.launch {
            // Usa a data que você passa como argumento
            val estoqueAtualizacaoDto = EstoqueAtualizacaoDto(
                dataEntradaInicial = dataEntrada, // Utilize a data fornecida
                produto = produto,
                loja = loja,
                quantidade = quantidade
            )

            Log.d("EstoqueViewModel", "Enviando JSON de entrada: ${gson.toJson(estoqueAtualizacaoDto)}")

            try {
                val response = RetrofitService.api.darEntradaProduto(estoqueAtualizacaoDto)
                if (response.isSuccessful) {
                    Log.d("EstoqueViewModel", "Entrada de produto bem-sucedida!")
                    fetchEstoque() // Atualiza a lista após a entrada
                } else {
                    Log.e("Erro na entrada", response.errorBody()?.string() ?: "Erro desconhecido")
                }
            } catch (e: Exception) {
                Log.e("Erro na entrada", e.message ?: "Erro desconhecido")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Função para dar baixa no produto
    fun darBaixaProduto(produto: Produto, loja: Loja, quantidade: Int, dataEntrada1: String) {
        viewModelScope.launch {
            // Subtrai 1 minuto da data atual
            val estoqueAtualizacaoDto = EstoqueAtualizacaoDto(
                dataEntradaInicial = dataEntrada1,
                produto = produto,
                loja = loja,
                quantidade = quantidade
            )

            // Log da data que está sendo enviada
            Log.d("EstoqueViewModel", "Enviando DTO para baixa: $estoqueAtualizacaoDto")

            try {
                val response = RetrofitService.api.darBaixaProduto(estoqueAtualizacaoDto)
                if (response.isSuccessful) {
                    Log.d("EstoqueViewModel", "Baixa de produto bem-sucedida!")
                    fetchEstoque() // Atualiza a lista após a baixa
                } else {
                    Log.e("Erro na baixa", response.errorBody()?.string() ?: "Erro desconhecido")
                }
            } catch (e: Exception) {
                Log.e("Erro na baixa", e.message ?: "Erro desconhecido")
            }
        }
    }

    fun checkProductQuantities(userId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<Map<String, Any>> = RetrofitService.api.checkProductQuantities(userId)
                if (response.isSuccessful) {
                    _productQuantities.value = response.body()?.get("quantities") as? Map<String, Int>
                }
            } catch (e: Exception) {
                // Lidar com erro de requisição
            }
        }
    }

    fun fetchMonthlyInvoices() {
        // Faça a chamada para a API utilizando Retrofit
        RetrofitService.api.getMonthlyInvoicesForLastSixMonths().enqueue(object :
            Callback<List<BigDecimal>> {
            override fun onResponse(call: Call<List<BigDecimal>>, response: Response<List<BigDecimal>>) {
                if (response.isSuccessful) {
                    // Atualiza o LiveData com a lista de faturas mensais
                    _monthlyInvoices.value = response.body() ?: emptyList()
                } else {
                    // Trate o erro de resposta não bem-sucedida, se necessário
                }
            }

            override fun onFailure(call: Call<List<BigDecimal>>, t: Throwable) {
                // Trate o erro da chamada, se necessário
            }
        })
    }

    // Função para lidar com erros
    private fun handleError(message: String) {
        Log.e("EstoqueViewModel", message)
        _errorMessage.postValue(message) // Atualiza a LiveData com a mensagem de erro
    }
}
