/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.google.android.material.textfield.TextInputEditText
import jp.co.yumemi.android.code_check.databinding.RepositoriesFragmentBinding

object RepositoriesBindingAdapter {
    @BindingAdapter("onEditorAction")
    @JvmStatic
    fun setOnInputEditorAction(view: TextInputEditText, listener: TextView.OnEditorActionListener){
        view.setOnEditorActionListener(listener)
    }

    @BindingAdapter("divider")
    @JvmStatic
    fun setDivider(view: RecyclerView, dividerItemDecoration: DividerItemDecoration){
        view.addItemDecoration(dividerItemDecoration)
    }
}

class RepositoriesFragment: Fragment(R.layout.repositories_fragment){
    private val viewModel: RepositoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RepositoriesFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRepositoriesRecycler(binding)

        return binding.root
    }

    private fun initRepositoriesRecycler(binding: RepositoriesFragmentBinding){
        binding.divider = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)

        val adapter = RepositoryAdapter(object : RepositoryAdapter.OnItemClickListener{
            override fun repositoryClick(repository: Repository){
                gotoRepositoryFragment(repository)
            }
        }, viewLifecycleOwner)
        binding.adapter = adapter

        viewModel.repositories.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    fun gotoRepositoryFragment(repository: Repository)
    {
        val action = RepositoriesFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(repository)
        findNavController().navigate(action)
    }
}



