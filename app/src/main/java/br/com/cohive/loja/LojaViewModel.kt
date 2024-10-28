package br.com.cohive.loja

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cohive.RetrofitService
import kotlinx.coroutines.launch
import retrofit2.Response

class LojaViewModel : ViewModel() {

    // LiveData para observar o resultado do cadastro da loja
    private val _cadastrarResult = MutableLiveData<Loja?>()
    val cadastrarResult: LiveData<Loja?> get() = _cadastrarResult

    // Função para cadastro de loja
    fun cadastrar(loja: LojaCriacaoDto) {
        viewModelScope.launch {
            try {
                Log.d("br.com.cohive.usuario.LojaViewModel", "Iniciando cadastro da loja...")
                val response: Response<Loja> = RetrofitService.api.cadastrarLoja(loja)
                if (response.isSuccessful) {
                    response.body()?.let { lojaResponse ->
                        Log.d("br.com.cohive.usuario.LojaViewModel", "Cadastro de loja bem-sucedido. ID da loja: ${lojaResponse.id}")
                        _cadastrarResult.postValue(lojaResponse) // Posta o objeto completo
                    } ?: run {
                        Log.e("br.com.cohive.usuario.LojaViewModel", "Corpo da resposta nulo no cadastro da loja.")
                        _cadastrarResult.postValue(null) // Posta null em caso de erro
                    }
                } else {
                    Log.e("br.com.cohive.usuario.LojaViewModel", "Erro no cadastro da loja: ${response.code()}")
                    _cadastrarResult.postValue(null) // Posta null em caso de erro
                }
            } catch (e: Exception) {
                Log.e("br.com.cohive.usuario.LojaViewModel", "Exceção no cadastro da loja: ${e.message}", e)
                _cadastrarResult.postValue(null) // Posta null em caso de exceção
            }
        }
    }
}
