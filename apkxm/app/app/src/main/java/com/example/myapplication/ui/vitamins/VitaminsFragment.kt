package com.example.myapplication.ui.vitamins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentVitaminsBinding

class VitaminsFragment : Fragment() {
    
    private var _binding: FragmentVitaminsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: VitaminsViewModel
    private lateinit var adapter: VitaminsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[VitaminsViewModel::class.java]
        _binding = FragmentVitaminsBinding.inflate(inflater, container, false)
        
        adapter = VitaminsAdapter { vitamin ->
            val action = VitaminsFragmentDirections.actionNavVitaminsToVitaminDetailFragment(vitamin.id)
            findNavController().navigate(action)
        }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadVitamins()
        }
        
        viewModel.vitamins.observe(viewLifecycleOwner) { vitamins ->
            adapter.submitList(vitamins)
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
        
        viewModel.loadVitamins()
        
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

