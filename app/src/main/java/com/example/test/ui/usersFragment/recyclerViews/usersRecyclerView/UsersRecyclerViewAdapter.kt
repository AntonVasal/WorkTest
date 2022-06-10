package com.example.test.ui.usersFragment.recyclerViews.usersRecyclerView
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.ItemForUsersRvBinding
import com.example.test.domain.models.UserModel
import com.example.test.ui.usersFragment.recyclerViews.usersRecyclerView.recyclerDiffCallback.UsersRecyclerDiffCallback
import com.example.test.utils.extensions.clickWithDebounce


class UsersRecyclerViewAdapter(
    private val context: Context,
    private val itemClick: (UserModel) -> Unit
) : RecyclerView.Adapter<UsersRVHolder>() {

    private val differ = AsyncListDiffer(this, UsersRecyclerDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersRVHolder {
        val binding = DataBindingUtil.inflate<ItemForUsersRvBinding>(LayoutInflater.from(context),
            R.layout.item_for_users_rv,parent,false)
        return UsersRVHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersRVHolder, position: Int) {
        val user = differ.currentList[position]
        holder.bind(user)
        holder.itemView.clickWithDebounce {
            itemClick.invoke(user)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: ArrayList<UserModel>) {
        differ.submitList(list)
    }

    fun currentList(): List<UserModel> {
        return differ.currentList
    }

}


