package br.com.cohive

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private var token: String? = null

    // Função para salvar o token após o login
    fun setToken(authToken: String) {
        token = "Bearer $authToken"
    }

    // Interceptor para adicionar o token nas requisições
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()

        // Adiciona o token, exceto para login e cadastro
        if (token != null && original.url.encodedPath != "/api/usuarios/login"
            && original.url.encodedPath != "/api/usuarios/cadastro") {
            requestBuilder.addHeader("Authorization", token!!)
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val api: ApiCohive by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCohive::class.java)
    }
}