package com.example.myapplication.ui.vitamins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentVitaminDetailBinding

class VitaminDetailFragment : Fragment() {
    
    private var _binding: FragmentVitaminDetailBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: VitaminDetailViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[VitaminDetailViewModel::class.java]
        _binding = FragmentVitaminDetailBinding.inflate(inflater, container, false)
        
        val vitaminId = VitaminDetailFragmentArgs.fromBundle(requireArguments()).vitaminId
        
        viewModel.vitamin.observe(viewLifecycleOwner) { vitamin ->
            vitamin?.let {
                binding.textName.text = it.name
                binding.textNameEn.text = it.name_en ?: ""
                binding.textAlias.text = it.alias ?: ""
                binding.textDescription.text = it.description ?: "暂无描述"
                binding.textFunction.text = it.function
                binding.textBenefits.text = it.benefits ?: "暂无"
                binding.textDailyRequirement.text = it.daily_requirement ?: "未指定"
                binding.textDeficiencySymptoms.text = it.deficiency_symptoms ?: "暂无"
                binding.textOverdoseSymptoms.text = it.overdose_symptoms ?: "暂无"
                
                it.food_sources?.let { sources ->
                    try {
                        val jsonArray = org.json.JSONArray(sources)
                        val foodList = (0 until jsonArray.length()).map { jsonArray.getString(it) }
                        binding.textFoodSources.text = foodList.joinToString("\n") { "• $it" }
                    } catch (e: Exception) {
                        binding.textFoodSources.text = sources.split("\n").filter { it.isNotBlank() }
                            .joinToString("\n") { "• $it" }
                    }
                } ?: run {
                    binding.textFoodSources.text = "暂无"
                }
                
                it.image_url?.let { url ->
                    com.bumptech.glide.Glide.with(requireContext())
                        .load(url)
                        .into(binding.imageVitamin)
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
        
        viewModel.loadVitamin(vitaminId)
        
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

