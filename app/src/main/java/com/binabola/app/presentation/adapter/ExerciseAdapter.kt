package com.binabola.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binabola.app.data.remote.response.GetExerciseItem
import com.binabola.app.databinding.ItemMissionBinding

class ExerciseAdapter(private val listener: (GetExerciseItem) -> Unit) : ListAdapter<GetExerciseItem, ExerciseAdapter.ExerciseViewHolder>(ExerciseDiffCallback()) {
    inner class ExerciseViewHolder(private val binding: ItemMissionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: GetExerciseItem) {
            binding.tvTitle.text = exercise.name
            binding.tvDesc.text = exercise.category
            binding.root.setOnClickListener{
                listener(exercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMissionBinding.inflate(inflater, parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ExerciseDiffCallback : DiffUtil.ItemCallback<GetExerciseItem>() {
    override fun areItemsTheSame(oldItem: GetExerciseItem, newItem: GetExerciseItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GetExerciseItem, newItem: GetExerciseItem): Boolean {
        return oldItem == newItem
    }
}