/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.wada811.viewbinding.viewBinding
import jp.co.yumemi.android.code_check.databinding.RepositoriesFragmentBinding
import kotlinx.coroutines.launch
import org.json.JSONException

class RepositoriesFragment: Fragment(R.layout.repositories_fragment){
    private val viewModel by viewModels<RepositoriesViewModel>()
    private val adapter = RepositoryAdapter(object : RepositoryAdapter.OnItemClickListener{
        override fun repositoryClick(repository: Repository){
            gotoRepositoryFragment(repository)
        }
    })
    private val binding:RepositoriesFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        adapter.submitList(viewModel.repositories)

        with(binding){
            initSearchInputText(searchInputText)
            initRepositoriesRecycler(repositoriesRecycler)
        }
    }

    private fun search(searchText: String){
        lifecycleScope.launch{
            val searchResults = viewModel.repositoriesSearch(searchText)
            adapter.submitList(searchResults)
        }
    }

    private fun initSearchInputText(searchInputText: TextInputEditText){
        searchInputText.setOnEditorActionListener{ editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH){
                    UtilCommon.hideSoftKeyBoard(requireContext(), editText)
                    editText.clearFocus()
                    val searchText = editText.text.toString()
                    search(searchText)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
    }

    private fun initRepositoriesRecycler(repositoriesRecycler: RecyclerView){
        val context = requireContext()
        val layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration =
            DividerItemDecoration(context, layoutManager.orientation)

        with(repositoriesRecycler) {
            this.layoutManager = layoutManager
            addItemDecoration(dividerItemDecoration)
            this.adapter = adapter
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



