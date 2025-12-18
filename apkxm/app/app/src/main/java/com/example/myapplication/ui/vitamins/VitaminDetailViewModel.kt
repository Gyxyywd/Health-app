package com.example.myapplication.ui.vitamins

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.ApiClient
import com.example.myapplication.model.Vitamin
import kotlinx.coroutines.launch

class VitaminDetailViewModel : ViewModel() {
    
    private val _vitamin = MutableLiveData<Vitamin?>()
    val vitamin: LiveData<Vitamin?> = _vitamin
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadVitamin(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = ApiClient.apiService.getVitamin(id)
                if (response.isSuccessful) {
                    _vitamin.value = response.body()
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

