package com.myjar.jarassignment.ui.vm

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.DataStoreHelper
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import com.myjar.jarassignment.data.utils.JsonConverters
import com.myjar.jarassignment.networkManager.ConnectivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class JarViewModel : ViewModel() {

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
    val listStringData: StateFlow<List<ComputerItem>>
        get() = _listStringData

    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

    private val _isConnected = MutableStateFlow<Boolean>(false)
    val isConnected = _isConnected.asStateFlow()

    private val _query = MutableStateFlow<String?>(null)
    val query = _query.asStateFlow()

    fun setQuery(query: String?) {
        _query.value = query
    }

    fun fetchData(context: Context) {
        viewModelScope.launch {
            combine(
                _isConnected,
                DataStoreHelper.read(context),
                _query
            ) { connection, result, query ->
                Triple(connection, result, query)
            }.collectLatest { (connection, result, query) ->
                if (!query.isNullOrBlank()) {
                    val searchResult = _listStringData.value.let {
                        it.filter { item ->
                            item.name.lowercase().contains(query.lowercase())
                        }
                    }
                    searchResult.let { searches ->
                        _listStringData.value = searches
                    }
                } else {
                    if (!connection) {
                        val items = result?.let { JsonConverters.convertComputerItemFromJson(it) }
                        items?.let {
                            _listStringData.value = it
                        }
                    } else {
                        repository.fetchResults().collect {
                            _listStringData.value = it
                        }
                    }
                }
            }
        }
    }

    fun updateLocalData(context: Context, computerItems: List<ComputerItem>) {
        viewModelScope.launch {
            val jsonString = JsonConverters.convertComputerItemToJson(computerItems)
            DataStoreHelper.save(context, jsonString)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkConnectivity(context: Context) {
        viewModelScope.launch {
            ConnectivityRepository(context).isConnected.collectLatest {
                _isConnected.value = it
            }
        }
    }
}