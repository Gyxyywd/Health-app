package com.example.myapplication.ui.vitamins

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemVitaminBinding
import com.example.myapplication.model.Vitamin

class VitaminsAdapter(
    private val onItemClick: (Vitamin) -> Unit
) : ListAdapter<Vitamin, VitaminsAdapter.VitaminViewHolder>(VitaminDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VitaminViewHolder {
        val binding = ItemVitaminBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VitaminViewHolder(binding, onItemClick)
    }
    
    override fun onBindViewHolder(holder: VitaminViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class VitaminViewHolder(
        private val binding: ItemVitaminBinding,
        private val onItemClick: (Vitamin) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(vitamin: Vitamin) {
            binding.textName.text = vitamin.name
            binding.textNameEn.text = vitamin.name_en ?: ""
            binding.textFunction.text = vitamin.function.take(100) + if (vitamin.function.length > 100) "..." else ""
            binding.textDailyRequirement.text = vitamin.daily_requirement ?: "未指定"
            
            binding.root.setOnClickListener {
                onItemClick(vitamin)
            }
        }
    }
    
    class VitaminDiffCallback : DiffUtil.ItemCallback<Vitamin>() {
        override fun areItemsTheSame(oldItem: Vitamin, newItem: Vitamin): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Vitamin, newItem: Vitamin): Boolean {
            return oldItem == newItem
        }
    }
}

