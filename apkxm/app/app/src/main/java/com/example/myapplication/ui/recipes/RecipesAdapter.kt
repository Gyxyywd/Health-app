package com.example.myapplication.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemRecipeBinding
import com.example.myapplication.model.Recipe

class RecipesAdapter(
    private val onItemClick: (Recipe) -> Unit
) : ListAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(RecipeDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding, onItemClick)
    }
    
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class RecipeViewHolder(
        private val binding: ItemRecipeBinding,
        private val onItemClick: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(recipe: Recipe) {
            binding.textTitle.text = recipe.title
            binding.textDescription.text = recipe.description ?: "暂无描述"
            binding.textCategory.text = recipe.category ?: "未分类"
            binding.textDifficulty.text = recipe.difficulty
            recipe.calories?.let {
                binding.textCalories.text = "${it.toInt()} 卡路里"
            }
            
            recipe.image_url?.let { url ->
                Glide.with(binding.root.context)
                    .load(url)
                    .into(binding.imageRecipe)
            }
            
            binding.root.setOnClickListener {
                onItemClick(recipe)
            }
        }
    }
    
    class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }
}

