package com.example.test.core.baseFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.test.core.baseStates.BaseState
import com.example.test.core.baseViewModel.BaseViewModel
import kotlin.reflect.KClass

abstract class BaseFragment<Binding : ViewDataBinding,
        VM : BaseViewModel<S>,  S : BaseState> : Fragment() {

    lateinit var viewModel: VM
    lateinit var binding: Binding
    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<Binding>(inflater, layoutId, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.state.observeNotNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { state ->
                onStateChanged(state)
            }
        }
    }

    protected abstract fun onStateChanged(state: S)

    protected open fun initViewModel() {
        viewModel = ViewModelProvider(this)[viewModelClass.javaObjectType]
    }

    fun <T> LiveData<T>.observeNotNull(
        owner: LifecycleOwner = viewLifecycleOwner,
        observer: (t: T) -> Unit
    ) {
        this.observe(owner) {
            it?.let(observer)
        }
    }

}