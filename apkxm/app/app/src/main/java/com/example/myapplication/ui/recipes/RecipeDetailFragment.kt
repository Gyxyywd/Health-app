package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {
    
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: RecipeDetailViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[RecipeDetailViewModel::class.java]
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        
        val recipeId = RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipeId
        
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                binding.textTitle.text = it.title
                binding.textDescription.text = it.description ?: "暂无描述"
                binding.textCategory.text = "分类: ${it.category ?: "未分类"}"
                binding.textDifficulty.text = "难度: ${it.difficulty}"
                binding.textPrepTime.text = "准备时间: ${it.prep_time ?: 0} 分钟"
                binding.textCookTime.text = "烹饪时间: ${it.cook_time ?: 0} 分钟"
                binding.textServings.text = "份数: ${it.servings}"
                
                it.calories?.let { calories ->
                    binding.textCalories.text = "卡路里: ${calories.toInt()}"
                }
                it.protein?.let { protein ->
                    binding.textProtein.text = "蛋白质: ${protein}g"
                }
                it.carbs?.let { carbs ->
                    binding.textCarbs.text = "碳水化合物: ${carbs}g"
                }
                it.fat?.let { fat ->
                    binding.textFat.text = "脂肪: ${fat}g"
                }
                
                // 解析食材和步骤
                try {
                    val ingredients = try {
                        val jsonArray = org.json.JSONArray(it.ingredients)
                        (0 until jsonArray.length()).map { jsonArray.getString(it) }
                    } catch (e: Exception) {
                        it.ingredients.split("\n").filter { it.isNotBlank() }
                    }
                    binding.textIngredients.text = ingredients.joinToString("\n") { "• $it" }
                } catch (e: Exception) {
                    binding.textIngredients.text = it.ingredients
                }
                
                try {
                    val steps = try {
                        val jsonArray = org.json.JSONArray(it.steps)
                        (0 until jsonArray.length()).map { "${it + 1}. ${jsonArray.getString(it)}" }
                    } catch (e: Exception) {
                        it.steps.split("\n").filter { it.isNotBlank() }.mapIndexed { index, step ->
                            "${index + 1}. $step"
                        }
                    }
                    binding.textSteps.text = steps.joinToString("\n")
                } catch (e: Exception) {
                    binding.textSteps.text = it.steps
                }
                
                it.image_url?.let { url ->
                    com.bumptech.glide.Glide.with(requireContext())
                        .load(url)
                        .into(binding.imageRecipe)
                }
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                android.widget.Toast.makeText(requireContext(), it, android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        viewModel.loadRecipe(recipeId)
        
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

