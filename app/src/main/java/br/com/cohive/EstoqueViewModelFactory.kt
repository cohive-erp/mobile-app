package br.com.cohive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cohive.estoque.EstoqueViewModel

class EstoqueViewModelFactory(
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstoqueViewModel::class.java)) {
            return EstoqueViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
