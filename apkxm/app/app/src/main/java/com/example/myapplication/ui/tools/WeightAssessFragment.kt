package com.example.myapplication.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.api.ApiClient
import com.example.myapplication.databinding.FragmentWeightAssessBinding
import kotlinx.coroutines.launch

class WeightAssessFragment : Fragment() {
    private var _binding: FragmentWeightAssessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWeightAssessBinding.inflate(inflater, container, false)

        binding.buttonAssess.setOnClickListener {
            val h = binding.editHeight.text.toString().toFloatOrNull()
            val w = binding.editWeight.text.toString().toFloatOrNull()
            val sex = if (binding.radioMale.isChecked) "male" else "female"
            if (h == null || w == null) {
                android.widget.Toast.makeText(requireContext(), "请输入有效身高体重", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                try {
                    val res = ApiClient.apiService.weightAssess(h, w, sex)
                    if (res.isSuccessful) {
                        val body = res.body()!!
                        binding.textBmi.text = "BMI: ${body.bmi} (${body.level})"
                        binding.textRange.text = "建议体重: ${body.suggest_weight_range.joinToString(" ~ ")} kg"
                        binding.textTips.text = body.tips.joinToString("\n") { "• $it" }
                    } else {
                        android.widget.Toast.makeText(requireContext(), "评估失败: ${res.code()}", android.widget.Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    android.widget.Toast.makeText(requireContext(), "网络错误: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


