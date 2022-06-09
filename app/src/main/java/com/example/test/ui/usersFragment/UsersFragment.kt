package com.example.test.ui.usersFragment

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.test.R
import com.example.test.core.baseFragment.BaseFragment
import com.example.test.core.baseStates.BaseStates
import com.example.test.databinding.FragmentUsersBinding
import com.example.test.domain.models.UserModel
import com.example.test.ui.usersFragment.usersRecyclerView.UsersRecyclerViewAdapter
import com.example.test.ui.usersFragment.usersViewModel.UsersViewModel
import com.example.test.utils.extensions.gone
import com.example.test.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment(override val layoutId: Int = R.layout.fragment_users) :
    BaseFragment<FragmentUsersBinding>() {

    private val usersViewModel: UsersViewModel by viewModels()
    private lateinit var bottomSheetDialog : BottomSheetDialog
    private lateinit var listView:ListView
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        observeStates()

        configureDialog()
        observeRepos()

        val adapter = UsersRecyclerViewAdapter(requireContext()) { createDialog(it) }
        binding.usersRecyclerView.adapter = adapter

        usersViewModel.users.observeNotNull(viewLifecycleOwner) {
           adapter.submitList(it)
        }
    }

    private fun configureDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet_for_user)
        listView = bottomSheetDialog.findViewById(R.id.repos_list)!!
        imageView= bottomSheetDialog.findViewById(R.id.dialog_image)!!
        textView = bottomSheetDialog.findViewById (R.id.dialog_text)!!
    }

    private fun observeRepos() {
        usersViewModel.repos.observeNotNull(viewLifecycleOwner){
            if (bottomSheetDialog.isShowing){
                listView.adapter = usersViewModel.repos.value?.let {
                    ArrayAdapter( requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, it.toArray())
                }
            }
        }
    }

    private fun createDialog(userModel: UserModel) {
        usersViewModel.getUserReposWithRealm(userModel.login,userModel.id)
        textView.text = userModel.login
        Glide.with(requireContext()).load(userModel.avatarUrl).placeholder(R.drawable.ic_user_main)
            .error(R.drawable.ic_user_main).into(imageView)
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
                    Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show()
                }
                is BaseStates.LoadingState -> usersLoader.visible()
                is BaseStates.SuccessState -> usersLoader.gone()
            }
        }
    }

}