package com.example.myapplication.ui.feedback

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemFeedbackBinding
import com.example.myapplication.model.Feedback

class FeedbackListAdapter : ListAdapter<Feedback, FeedbackListAdapter.VH>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(private val binding: ItemFeedbackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Feedback) {
            binding.textTitle.text = item.title
            binding.textContent.text = item.content
            binding.textStatus.text = item.status
            binding.textDate.text = item.created_at
        }
    }

    class Diff : DiffUtil.ItemCallback<Feedback>() {
        override fun areItemsTheSame(oldItem: Feedback, newItem: Feedback) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Feedback, newItem: Feedback) = oldItem == newItem
    }
}


