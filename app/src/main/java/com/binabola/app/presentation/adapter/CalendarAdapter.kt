package com.binabola.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binabola.app.R
import com.binabola.app.databinding.ItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarAdapter(private val listener: (Calendar) -> Unit) :
    ListAdapter<Calendar, CalendarAdapter.CalendarViewHolder>(CalendarDiffCallback()) {

    private var currentIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCalendarBinding.inflate(inflater, parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class CalendarViewHolder(private val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(calendar: Calendar, index: Int) {
            val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
            val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

            val dateString = dateFormat.format(calendar.time)
            val dayString = dayFormat.format(calendar.time)
            binding.tvDate.text = dateString
            binding.tvDay.text = dayString

            // Highlight current date
            val currentDate = Calendar.getInstance()
            if (((calendar.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) && currentIndex == -1
            ) || currentIndex == index) {
                binding.container.setBackgroundResource(R.drawable.bg_calendar_active)
                binding.tvDate.setTextColor(binding.root.context.getColor(R.color.white))
                binding.tvDay.setTextColor(binding.root.context.getColor(R.color.white))
            } else {
                binding.container.setBackgroundResource(android.R.color.transparent)
                binding.tvDate.setTextColor(binding.root.context.getColor(R.color.grey_700))
                binding.tvDay.setTextColor(binding.root.context.getColor(R.color.grey_700))
            }

            binding.root.setOnClickListener {
                listener(calendar)
                currentIndex = index
                notifyDataSetChanged()
            }
        }
    }
}

class CalendarDiffCallback : DiffUtil.ItemCallback<Calendar>() {
    override fun areItemsTheSame(oldItem: Calendar, newItem: Calendar): Boolean {
        return oldItem.timeInMillis == newItem.timeInMillis
    }

    override fun areContentsTheSame(oldItem: Calendar, newItem: Calendar): Boolean {
        return oldItem == newItem
    }
}