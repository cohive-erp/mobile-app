package br.com.cohive
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "user_data_store")

class DataStoreManager(private val context: Context) {

    private val LOJA_ID_KEY = intPreferencesKey("loja_id")

    suspend fun getLojaId(): Int {
        val preferences = context.dataStore.data.first()
        return preferences[LOJA_ID_KEY] ?: 0 // Retorna 0 se não encontrar o ID
    }

    suspend fun saveLojaId(lojaId: Int) {
        context.dataStore.edit { preferences ->
            preferences[LOJA_ID_KEY] = lojaId
        }
    }
}

