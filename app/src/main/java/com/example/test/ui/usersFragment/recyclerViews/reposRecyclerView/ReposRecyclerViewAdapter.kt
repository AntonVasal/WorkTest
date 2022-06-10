package com.example.test.ui.usersFragment.recyclerViews.reposRecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.ItemForReposRvBinding
import com.example.test.ui.usersFragment.recyclerViews.reposRecyclerView.recyclerDiffCallback.RepoRecyclerDiffCallback

class ReposRecyclerViewAdapter(private val context: Context): RecyclerView.Adapter<ReposRVHolder>() {

    private val differ = AsyncListDiffer(this, RepoRecyclerDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposRVHolder {
        val binding = DataBindingUtil.inflate<ItemForReposRvBinding>(
            LayoutInflater.from(context),
            R.layout.item_for_repos_rv,parent,false)
        return ReposRVHolder(binding)
    }

    override fun onBindViewHolder(holder: ReposRVHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: ArrayList<String>) {
        differ.submitList(list)
    }

    fun currentList(): List<String> {
        return differ.currentList
    }

}