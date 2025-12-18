package com.example.myapplication.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.ApiClient
import com.example.myapplication.model.Recipe
import kotlinx.coroutines.launch

class RecipeDetailViewModel : ViewModel() {
    
    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: LiveData<Recipe?> = _recipe
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = ApiClient.apiService.getRecipe(id)
                if (response.isSuccessful) {
                    _recipe.value = response.body()
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

