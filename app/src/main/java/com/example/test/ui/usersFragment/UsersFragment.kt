package com.example.test.ui.usersFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.test.R
import com.example.test.core.baseFragment.BaseFragment
import com.example.test.core.baseStates.BaseStates
import com.example.test.databinding.FragmentUsersBinding
import com.example.test.utils.extensions.gone
import com.example.test.utils.extensions.visible
import com.example.test.ui.usersFragment.usersRecyclerView.UsersRecyclerViewAdapter
import com.example.test.ui.usersFragment.usersViewModel.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment(override val layoutId: Int = R.layout.fragment_users) :
    BaseFragment<FragmentUsersBinding>() {

    private val usersViewModel: UsersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        observeStates()

        val adapter = UsersRecyclerViewAdapter(requireContext()) { createDialog() }
        binding.usersRecyclerView.adapter = adapter

        usersViewModel.users.observeNotNull(viewLifecycleOwner) {
           adapter.submitList(it)
        }
    }

    private fun createDialog() {

    }

    private fun observeStates() {
        usersViewModel.state.observeNotNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { state ->
                onStateChanged(state)
            }
        }
    }

    private fun onStateChanged(state: BaseStates) {
        binding.apply {
            when (state) {
                is BaseStates.ErrorState -> usersLoader.gone()
                is BaseStates.LoadingState -> usersLoader.visible()
                is BaseStates.SuccessState -> usersLoader.gone()
            }
        }
    }

}