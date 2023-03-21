/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.google.android.material.textfield.TextInputEditText
import jp.co.yumemi.android.code_check.databinding.RepositoriesFragmentBinding

class RepositoriesFragment: Fragment(R.layout.repositories_fragment){
    private val viewModel by viewModels<RepositoriesViewModel>()

    private var _binding: RepositoriesFragmentBinding? = null
    private val binding
        get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RepositoriesFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        with(binding){
            initSearchInputText(searchInputText)
            initRepositoriesRecycler(repositoriesRecycler)
        }
    }

    private fun initSearchInputText(searchInputText: TextInputEditText){
        searchInputText.setOnEditorActionListener{ editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH){
                    UtilCommon.hideSoftKeyBoard(requireContext(), editText)
                    editText.clearFocus()
                    viewModel.searchRepositories()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
    }

    private fun initRepositoriesRecycler(repositoriesRecycler: RecyclerView){
        val adapter = RepositoryAdapter(object : RepositoryAdapter.OnItemClickListener{
            override fun repositoryClick(repository: Repository){
                gotoRepositoryFragment(repository)
            }
        }, viewLifecycleOwner)

        viewModel.repositories.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val context = requireContext()
        val layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration =
            DividerItemDecoration(context, layoutManager.orientation)

        repositoriesRecycler.let{
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }
    }

    fun gotoRepositoryFragment(repository: Repository)
    {
        val action = RepositoriesFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(repository)
        findNavController().navigate(action)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding.repositoriesRecycler.adapter = null
    }
}



