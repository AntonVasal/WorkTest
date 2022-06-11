package com.example.test.ui.reposFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.test.R
import com.example.test.core.baseFragment.BaseFragment
import com.example.test.core.baseStates.BaseStates
import com.example.test.databinding.FragmentReposBinding
import com.example.test.ui.reposFragment.reposRecyclerView.ReposRecyclerViewAdapter
import com.example.test.ui.reposFragment.reposViewModel.ReposViewModel
import com.example.test.utils.constants.REPOS_FRAGMENT
import com.example.test.utils.extensions.gone
import com.example.test.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class ReposFragment(
    override val layoutId: Int = R.layout.fragment_repos,
    override val viewModelClass: KClass<ReposViewModel> = ReposViewModel::class
) : BaseFragment<FragmentReposBinding,ReposViewModel,BaseStates>() {

    private val args :ReposFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            imageUrl = args.imageUrl
            login = args.login
        }
        viewModel.getUserReposWithRealm(args.login,args.id)

        val adapter = ReposRecyclerViewAdapter(requireContext())
        binding.reposList.adapter = adapter
        viewModel.repos.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    override fun onStateChanged(state: BaseStates) {
        binding.apply {
            when (state) {
                is BaseStates.ErrorState -> {
                    reposLoader.gone()
                    Log.i(REPOS_FRAGMENT,state.message)
                }
                is BaseStates.LoadingState -> reposLoader.visible()
                is BaseStates.SuccessState -> reposLoader.gone()
            }
        }
    }
}