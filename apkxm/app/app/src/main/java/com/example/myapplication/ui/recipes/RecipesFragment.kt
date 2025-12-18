package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {
    
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: RecipesViewModel
    private lateinit var adapter: RecipesAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[RecipesViewModel::class.java]
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        
        adapter = RecipesAdapter { recipe ->
            val action = RecipesFragmentDirections.actionNavRecipesToRecipeDetailFragment(recipe.id)
            findNavController().navigate(action)
        }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadRecipes()
        }
        
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
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
        
        viewModel.loadRecipes()
        
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

