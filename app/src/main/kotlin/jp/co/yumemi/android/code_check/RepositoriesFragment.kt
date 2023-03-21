/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
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

        val searchInputText = binding.searchInputText
        initSearchInputText(searchInputText)

        val repositoriesRecycler = binding.repositoriesRecycler
        initRepositoriesRecycler(repositoriesRecycler)
    }

    private fun search(searchText: String){
        viewModel.resetRepositories()
        lifecycleScope.launch{
            try {
                val searchResults = viewModel.repositoriesSearch(searchText)
                adapter.submitList(searchResults)
            }catch (e: Exception){
                when(e){
                    is JSONException -> showErrorMessage("JSONパースエラー")
                    else -> showErrorMessage("検索エラー")
                }
                e.printStackTrace()
            }
        }
    }

    private fun showErrorMessage(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initSearchInputText(searchInputText: TextInputEditText){
        searchInputText.setOnEditorActionListener{ editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH){
                    hideSoftKeyBoard(editText)
                    editText.clearFocus()
                    val searchText = editText.text.toString()
                    search(searchText)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
    }

    private fun hideSoftKeyBoard(currentFocus: View){
        val inputMethodService = requireContext().getSystemService(InputMethodManager::class.java)
        inputMethodService.hideSoftInputFromWindow(
            currentFocus.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS,
        )
    }

    private fun initRepositoriesRecycler(repositoriesRecycler: RecyclerView){
        val context = requireContext()
        val layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration =
            DividerItemDecoration(context, layoutManager.orientation)

        repositoriesRecycler.also{
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



