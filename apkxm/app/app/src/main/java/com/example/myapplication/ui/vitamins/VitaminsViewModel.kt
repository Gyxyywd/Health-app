package com.example.myapplication.ui.vitamins

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.ApiClient
import com.example.myapplication.model.Vitamin
import kotlinx.coroutines.launch

class VitaminsViewModel : ViewModel() {
    
    private val _vitamins = MutableLiveData<List<Vitamin>>()
    val vitamins: LiveData<List<Vitamin>> = _vitamins
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadVitamins() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = ApiClient.apiService.getVitamins(page = 1, pageSize = 20)
                if (response.isSuccessful) {
                    _vitamins.value = response.body()?.items ?: emptyList()
                } else {
                    _error.value = "加载失败: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "网络错误: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

