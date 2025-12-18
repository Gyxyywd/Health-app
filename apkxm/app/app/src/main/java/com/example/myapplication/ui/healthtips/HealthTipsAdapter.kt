package com.example.myapplication.ui.healthtips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemHealthTipBinding
import com.example.myapplication.model.HealthTip

class HealthTipsAdapter(
    private val onItemClick: (HealthTip) -> Unit
) : ListAdapter<HealthTip, HealthTipsAdapter.HealthTipViewHolder>(HealthTipDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthTipViewHolder {
        val binding = ItemHealthTipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HealthTipViewHolder(binding, onItemClick)
    }
    
    override fun onBindViewHolder(holder: HealthTipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class HealthTipViewHolder(
        private val binding: ItemHealthTipBinding,
        private val onItemClick: (HealthTip) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(tip: HealthTip) {
            binding.textTitle.text = tip.title
            binding.textContent.text = tip.content.take(150) + if (tip.content.length > 150) "..." else ""
            binding.textCategory.text = tip.category ?: "未分类"
            
            tip.image_url?.let { url ->
                Glide.with(binding.root.context)
                    .load(url)
                    .into(binding.imageTip)
            }
            
            binding.root.setOnClickListener {
                onItemClick(tip)
            }
        }
    }
    
    class HealthTipDiffCallback : DiffUtil.ItemCallback<HealthTip>() {
        override fun areItemsTheSame(oldItem: HealthTip, newItem: HealthTip): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: HealthTip, newItem: HealthTip): Boolean {
            return oldItem == newItem
        }
    }
}

