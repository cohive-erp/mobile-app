package br.com.cohive.usuario

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cohive.RetrofitService
import kotlinx.coroutines.launch
import retrofit2.Response

class UsuarioViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<UsuarioTokenDto>()
    val loginResult: LiveData<UsuarioTokenDto> = _loginResult

    private val _cadastrarResult = MutableLiveData<Usuario?>()
    val cadastrarResult: LiveData<Usuario?> = _cadastrarResult

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int> = _userId

    private val _nomeUsuario = MutableLiveData<String>()
    val nomeUsuario: LiveData<String> = _nomeUsuario

    // Função para login
    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            try {
                Log.d("br.com.cohive.usuario.UsuarioViewModel", "Iniciando login...")
                val response = RetrofitService.api.login(loginRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d(
                            "br.com.cohive.usuario.UsuarioViewModel",
                            "Login bem-sucedido. Token: ${it.token}, Nome: ${it.nome}"
                        )
                        RetrofitService.setToken(it.token)
                        _nomeUsuario.postValue(it.nome)
                        _loginResult.postValue(it)
                    } ?: run {
                        Log.e(
                            "br.com.cohive.usuario.UsuarioViewModel",
                            "Corpo da resposta nulo no login."
                        )
                    }
                } else {
                    Log.e(
                        "br.com.cohive.usuario.UsuarioViewModel",
                        "Erro no login: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("br.com.cohive.usuario.UsuarioViewModel", "Exceção no login: ${e.message}", e)
            }
        }
    }

    // Função para cadastro
    fun cadastrar(usuario: UsuarioCriacaoDto) {
        viewModelScope.launch {
            try {
                Log.d("br.com.cohive.usuario.UsuarioViewModel", "Iniciando cadastro...")
                val response: Response<Usuario> = RetrofitService.api.cadastrar(usuario)
                if (response.isSuccessful) {
                    response.body()?.let { usuarioResponse ->
                        Log.d("br.com.cohive.usuario.UsuarioViewModel", "Cadastro bem-sucedido. ID do usuário: ${usuarioResponse.id}")
                        _cadastrarResult.postValue(usuarioResponse) // Posta o objeto completo
                        _isSuccessful.postValue(true) // Indica sucesso
                    } ?: run {
                        Log.e("br.com.cohive.usuario.UsuarioViewModel", "Corpo da resposta nulo no cadastro.")
                        _cadastrarResult.postValue(null) // Posta null em caso de erro
                        _isSuccessful.postValue(false) // Indica falha
                    }
                } else {
                    Log.e("br.com.cohive.usuario.UsuarioViewModel", "Erro no cadastro: ${response.code()}")
                    _cadastrarResult.postValue(null) // Posta null em caso de erro
                    _isSuccessful.postValue(false) // Indica falha
                }
            } catch (e: Exception) {
                Log.e("br.com.cohive.usuario.UsuarioViewModel", "Exceção no cadastro: ${e.message}", e)
                _cadastrarResult.postValue(null) // Posta null em caso de exceção
                _isSuccessful.postValue(false) // Indica falha
            }
        }
    }
}

