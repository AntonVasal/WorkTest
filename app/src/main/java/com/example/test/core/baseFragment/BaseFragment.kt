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

abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {
    lateinit var binding: Binding
    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<Binding>(inflater, layoutId, container, false).apply {
        binding = this
    }.root

    fun <T> LiveData<T>.observeNotNull(
        owner: LifecycleOwner = viewLifecycleOwner,
        observer: (t: T) -> Unit
    ) {
        this.observe(owner) {
            it?.let(observer)
        }
    }

}