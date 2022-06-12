package com.example.test.ui.usersFragment.recyclerViews.usersRecyclerView

import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ItemForUsersRvBinding
import com.example.test.domain.models.UserModel


class UsersRVHolder(val binding: ItemForUsersRvBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(userModel: UserModel) {
        binding.userModel = userModel
    }
}
