package com.example.test.ui.usersFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.test.R
import com.example.test.core.baseFragment.BaseFragment
import com.example.test.core.baseStates.BaseStates
import com.example.test.databinding.DialogBottomSheetForUserBinding
import com.example.test.databinding.FragmentUsersBinding
import com.example.test.domain.models.MainUserModel
import com.example.test.domain.models.UserModel
import com.example.test.ui.usersFragment.recyclerViews.reposRecyclerView.ReposRecyclerViewAdapter
import com.example.test.ui.usersFragment.recyclerViews.usersRecyclerView.UsersRecyclerViewAdapter
import com.example.test.ui.usersFragment.usersViewModel.UsersViewModel
import com.example.test.utils.constants.MAIN_FRAGMENT
import com.example.test.utils.extensions.gone
import com.example.test.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class UsersFragment(override val layoutId: Int = R.layout.fragment_users) :
    BaseFragment<FragmentUsersBinding>() {

    private val usersViewModel: UsersViewModel by viewModels()
    private lateinit var bottomSheetDialog : BottomSheetDialog
    private lateinit var dialogBinding:DialogBottomSheetForUserBinding
    private lateinit var dialogAdapter:ReposRecyclerViewAdapter

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        observeStates()

        getChangesCount()

        configureDialog()
        observeRepos()

        val adapter = UsersRecyclerViewAdapter(requireContext()) { createDialog(it) }
        binding.usersRecyclerView.adapter = adapter

        usersViewModel.users.observeNotNull(viewLifecycleOwner) {
           adapter.submitList(it)
        }
    }

    private fun getChangesCount() {
        val mainUserModel = usersViewModel.getMainUserModelFromRealm()
        if (mainUserModel.changesCount!= 0){
            binding.changesCountCard.visible()
            binding.changesCountText.text = mainUserModel.changesCount.toString()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(mainUserModel: MainUserModel ) {
        val changesCount = mainUserModel.changesCount
        if (changesCount!=0){
            binding.changesCountCard.visible()
            binding.changesCountText.text = changesCount.toString()
            usersViewModel.getUsersWithRetrofit(binding.usersRecyclerView.isEmpty())
            usersViewModel.setMainUserToRealm(mainUserModel)
        }
    }

    private fun configureDialog() {
        dialogAdapter = ReposRecyclerViewAdapter(requireContext())
        bottomSheetDialog = BottomSheetDialog(requireContext())
        dialogBinding = DialogBottomSheetForUserBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(dialogBinding.root)
        dialogBinding.reposList.adapter = dialogAdapter
    }

    private fun observeRepos() {
        usersViewModel.repos.observeNotNull(viewLifecycleOwner){
            if (bottomSheetDialog.isShowing){
                dialogAdapter.submitList(it)
            }
        }
    }

    private fun createDialog(userModel: UserModel) {
        usersViewModel.getUserReposWithRealm(userModel.login,userModel.id)
        dialogBinding.dialogText.text = userModel.login
        Glide.with(requireContext()).load(userModel.avatarUrl).placeholder(R.drawable.ic_user_main)
            .error(R.drawable.ic_user_main).into(dialogBinding.dialogImage)
        bottomSheetDialog.show()
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