package com.example.android.hearthstoneproject.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.hearthstoneproject.databinding.ListItemHearthstoneClassBinding
import com.example.android.hearthstoneproject.mainscreen.data.ClassEntity

class ClassesAdapter: ListAdapter<ClassEntity, ClassesAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ListItemHearthstoneClassBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(classEntity: ClassEntity) {
            binding.hearthstoneClass = classEntity
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemHearthstoneClassBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val classFeed = getItem(position)
        holder.itemView.setOnClickListener { v: View ->
            v.findNavController().navigate(
                MainScreenFragmentDirections.actionMainScreenFragmentToListCardsFragment(
                    classFeed.className,
                    useClassSearch = 2)
            )
        }
        holder.bind(classFeed)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ClassEntity>() {
        override fun areItemsTheSame(oldItem: ClassEntity, newItem: ClassEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ClassEntity, newItem: ClassEntity): Boolean {
            return oldItem.classId == newItem.classId
        }
    }

}