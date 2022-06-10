package com.example.test.ui.reposFragment.reposRecyclerView.recyclerDiffCallback

import androidx.recyclerview.widget.DiffUtil

class RepoRecyclerDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}