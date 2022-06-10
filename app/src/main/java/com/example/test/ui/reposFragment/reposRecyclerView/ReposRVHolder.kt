package com.example.test.ui.reposFragment.reposRecyclerView

import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ItemForReposRvBinding

class ReposRVHolder (private val binding: ItemForReposRvBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: String) {
        binding.repo = repo
    }
}