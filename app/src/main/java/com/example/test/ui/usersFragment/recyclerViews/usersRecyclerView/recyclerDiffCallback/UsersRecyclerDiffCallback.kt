package com.example.test.ui.usersFragment.recyclerViews.usersRecyclerView.recyclerDiffCallback

import androidx.recyclerview.widget.DiffUtil
import com.example.test.domain.models.UserModel

class UsersRecyclerDiffCallback : DiffUtil.ItemCallback<UserModel>() {

    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel) =
        oldItem == newItem
}