package com.example.myapplication.ui.healthtips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentHealthTipDetailBinding

class HealthTipDetailFragment : Fragment() {
    
    private var _binding: FragmentHealthTipDetailBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: HealthTipDetailViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HealthTipDetailViewModel::class.java]
        _binding = FragmentHealthTipDetailBinding.inflate(inflater, container, false)
        
        val tipId = HealthTipDetailFragmentArgs.fromBundle(requireArguments()).tipId
        
        viewModel.healthTip.observe(viewLifecycleOwner) { tip ->
            tip?.let {
                binding.textTitle.text = it.title
                binding.textContent.text = it.content
                binding.textCategory.text = it.category ?: "未分类"
                
                it.image_url?.let { url ->
                    com.bumptech.glide.Glide.with(requireContext())
                        .load(url)
                        .into(binding.imageTip)
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
        
        viewModel.loadHealthTip(tipId)
        
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

