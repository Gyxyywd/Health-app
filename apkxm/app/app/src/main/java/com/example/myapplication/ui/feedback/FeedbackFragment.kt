package com.example.myapplication.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.api.ApiClient
import com.example.myapplication.databinding.FragmentFeedbackBinding
import com.example.myapplication.model.FeedbackCreate
import kotlinx.coroutines.launch

class FeedbackFragment : Fragment() {
    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FeedbackListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)

        adapter = FeedbackListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.buttonSubmit.setOnClickListener {
            val title = binding.editTitle.text.toString().trim()
            val content = binding.editContent.text.toString().trim()
            val contact = binding.editContact.text.toString().trim().ifEmpty { null }
            if (title.isEmpty() || content.isEmpty()) {
                android.widget.Toast.makeText(requireContext(), "请输入完整反馈", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                try {
                    val res = ApiClient.apiService.createFeedback(FeedbackCreate(title, content, contact))
                    if (res.isSuccessful) {
                        android.widget.Toast.makeText(requireContext(), "提交成功", android.widget.Toast.LENGTH_SHORT).show()
                        binding.editTitle.setText("")
                        binding.editContent.setText("")
                        binding.editContact.setText("")
                        loadList()
                    } else {
                        android.widget.Toast.makeText(requireContext(), "提交失败: ${res.code()}", android.widget.Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    android.widget.Toast.makeText(requireContext(), "网络错误: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }

        loadList()
        return binding.root
    }

    private fun loadList() {
        lifecycleScope.launch {
            try {
                val res = ApiClient.apiService.getFeedbacks(1, 20)
                if (res.isSuccessful) {
                    adapter.submitList(res.body()?.items ?: emptyList())
                }
            } catch (_: Exception) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


