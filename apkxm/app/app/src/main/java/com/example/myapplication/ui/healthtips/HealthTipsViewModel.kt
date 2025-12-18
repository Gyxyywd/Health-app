package com.example.myapplication.ui.healthtips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.ApiClient
import com.example.myapplication.model.HealthTip
import kotlinx.coroutines.launch

class HealthTipsViewModel : ViewModel() {
    
    private val _healthTips = MutableLiveData<List<HealthTip>>()
    val healthTips: LiveData<List<HealthTip>> = _healthTips
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadHealthTips() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = ApiClient.apiService.getHealthTips(page = 1, pageSize = 20)
                if (response.isSuccessful) {
                    _healthTips.value = response.body()?.items ?: emptyList()
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

