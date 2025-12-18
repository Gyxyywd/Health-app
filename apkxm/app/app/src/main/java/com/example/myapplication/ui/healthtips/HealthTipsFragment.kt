package com.example.myapplication.ui.healthtips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentHealthTipsBinding

class HealthTipsFragment : Fragment() {
    
    private var _binding: FragmentHealthTipsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: HealthTipsViewModel
    private lateinit var adapter: HealthTipsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HealthTipsViewModel::class.java]
        _binding = FragmentHealthTipsBinding.inflate(inflater, container, false)
        
        adapter = HealthTipsAdapter { tip ->
            val action = HealthTipsFragmentDirections.actionNavHealthTipsToHealthTipDetailFragment(tip.id)
            findNavController().navigate(action)
        }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadHealthTips()
        }
        
        viewModel.healthTips.observe(viewLifecycleOwner) { tips ->
            adapter.submitList(tips)
            binding.swipeRefresh.isRefreshing = false
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }
        
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                android.widget.Toast.makeText(requireContext(), it, android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        viewModel.loadHealthTips()
        
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

