package com.example.test.ui.reposFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.test.R
import com.example.test.core.baseFragment.BaseFragment
import com.example.test.core.baseStates.BaseStates
import com.example.test.databinding.FragmentReposBinding
import com.example.test.domain.models.UserModel
import com.example.test.ui.reposFragment.reposViewModel.ReposViewModel
import com.example.test.ui.reposFragment.reposRecyclerView.ReposRecyclerViewAdapter
import com.example.test.utils.constants.REPOS_FRAGMENT
import com.example.test.utils.extensions.gone
import com.example.test.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReposFragment(override val layoutId: Int = R.layout.fragment_repos) : BaseFragment<FragmentReposBinding>() {

    private val reposViewModel: ReposViewModel by viewModels()
    private val args :ReposFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        observeStates()

        binding.apply {
            imageUrl = args.imageUrl
            login = args.login
        }
        reposViewModel.getUserReposWithRealm(args.login,args.id)

        val adapter = ReposRecyclerViewAdapter(requireContext())
        binding.reposList.adapter = adapter
        reposViewModel.repos.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    private fun observeStates() {
        reposViewModel.state.observeNotNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { state ->
                onStateChanged(state)
            }
        }
    }

    private fun onStateChanged(state: BaseStates) {
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