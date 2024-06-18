package com.binabola.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binabola.app.data.remote.response.GetDailyCalorieItem
import com.binabola.app.databinding.ItemFoodBinding
import com.binabola.app.databinding.ItemMissionBinding

class FoodAdapter : ListAdapter<GetDailyCalorieItem, FoodAdapter.FoodViewHolder>(FoodDiffCallback()){


    inner class FoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: GetDailyCalorieItem) {
//            binding.name.text = exercise.name
            binding.category.text = food.category
            binding.calorie.text = food.total.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodBinding.inflate(inflater, parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class FoodDiffCallback : DiffUtil.ItemCallback<GetDailyCalorieItem>() {
    override fun areItemsTheSame(oldItem: GetDailyCalorieItem, newItem: GetDailyCalorieItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: GetDailyCalorieItem, newItem: GetDailyCalorieItem): Boolean {
        return oldItem == newItem
    }
}