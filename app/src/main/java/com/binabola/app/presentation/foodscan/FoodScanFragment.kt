package com.binabola.app.presentation.foodscan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.binabola.app.R
import com.binabola.app.databinding.FragmentFoodBinding
import com.binabola.app.databinding.FragmentFoodScanBinding
import android.view.LayoutInflater as LayoutInflater1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FoodScanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoodScanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentFoodScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater1, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodScanBinding.inflate(inflater, container, false)
        val view = binding.root

        initView()
        return view
    }

    private fun initView() {
        binding.btnaddfood.setOnClickListener {
            val intent = Intent(requireContext(), FoodScanFragment::class.java)
            startActivity(intent)
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FoodScanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FoodScanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}