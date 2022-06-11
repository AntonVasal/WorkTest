package com.example.test.ui.usersFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.test.R
import com.example.test.core.baseFragment.BaseFragment
import com.example.test.core.baseStates.BaseStates
import com.example.test.databinding.FragmentUsersBinding
import com.example.test.domain.models.UserModel
import com.example.test.ui.usersFragment.recyclerViews.usersRecyclerView.UsersRecyclerViewAdapter
import com.example.test.ui.usersFragment.usersViewModel.UsersViewModel
import com.example.test.domain.models.MessageModel
import com.example.test.utils.constants.MAIN_FRAGMENT
import com.example.test.utils.extensions.gone
import com.example.test.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.reflect.KClass

@AndroidEntryPoint
class UsersFragment(
    override val layoutId: Int = R.layout.fragment_users,
    override val viewModelClass: KClass<UsersViewModel> = UsersViewModel::class
) : BaseFragment<FragmentUsersBinding,UsersViewModel,BaseStates>(){

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = UsersRecyclerViewAdapter(requireContext()) { openReposFragment(it) }
        binding.usersRecyclerView.adapter = adapter
        viewModel.users.observeNotNull(viewLifecycleOwner) {
           adapter.submitList(it)
        }
    }

    private fun openReposFragment(userModel: UserModel) {
        val action = UsersFragmentDirections.actionUsersFragmentToReposFragment()
        action.id = userModel.id
        action.login = userModel.login
        action.imageUrl = userModel.avatarUrl
        findNavController().navigate(action)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(data: MessageModel) {
        val changesCount = data.changesCount
        if (changesCount!=0){
            viewModel.updateUserInRealm(data.id,data.changesCount)
        }
    }

    override fun onStateChanged(state: BaseStates) {
        binding.apply {
            when (state) {
                is BaseStates.ErrorState -> {
                    usersLoader.gone()
                    Log.i(MAIN_FRAGMENT,state.message)
                }
                is BaseStates.LoadingState -> usersLoader.visible()
                is BaseStates.SuccessState -> usersLoader.gone()
            }
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

}