package com.myjar.jarassignment.ui.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.DataStoreHelper
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import com.myjar.jarassignment.data.utils.JsonConverters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class JarViewModel : ViewModel() {

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
    val listStringData: StateFlow<List<ComputerItem>>
        get() = _listStringData

    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

    fun fetchData() {
        viewModelScope.launch {
            repository.fetchResults().collectLatest {
                _listStringData.value = it
            }
        }
    }

    fun updateLocalData(context: Context, computerItems: List<ComputerItem>) {
        viewModelScope.launch {
            val jsonString = JsonConverters.convertComputerItemToJson(computerItems)
            DataStoreHelper.save(context, jsonString)
        }
    }
}