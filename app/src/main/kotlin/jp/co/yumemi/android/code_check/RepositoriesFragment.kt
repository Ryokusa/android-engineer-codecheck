/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.google.android.material.textfield.TextInputEditText
import jp.co.yumemi.android.code_check.databinding.RepositoriesFragmentBinding

class RepositoriesFragment: Fragment(R.layout.repositories_fragment){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val binding = RepositoriesFragmentBinding.bind(view)

        val adapter = ItemAdapter(object : ItemAdapter.OnItemClickListener{
            override fun itemClick(item: Item){
                gotoRepositoryFragment(item)
            }
        })

        val searchInputText = binding.searchInputText
        initSearchInputText(searchInputText, adapter)

        val repositoriesRecycler = binding.repositoriesRecycler
        initRepositoriesRecycler(repositoriesRecycler, adapter)
    }

    private fun initSearchInputText(searchInputText: TextInputEditText, adapter: ItemAdapter){
        val viewModel = RepositoriesViewModel(context!!)
        searchInputText.setOnEditorActionListener{ editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH){
                    val searchText = editText.text.toString()
                    val searchResults = viewModel.repositoriesSearch(searchText)
                    adapter.submitList(searchResults)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
    }

    private fun initRepositoriesRecycler(repositoriesRecycler: RecyclerView, adapter: ItemAdapter){
        val layoutManager = LinearLayoutManager(context!!)
        val dividerItemDecoration =
            DividerItemDecoration(context!!, layoutManager.orientation)

        repositoriesRecycler.also{
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }
    }

    fun gotoRepositoryFragment(item: Item)
    {
        val action = RepositoriesFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(item)
        findNavController().navigate(action)
    }
}

val diffUtil = object: DiffUtil.ItemCallback<Item>(){
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean
    {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean
    {
        return oldItem == newItem
    }
}

class ItemAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<Item, ItemAdapter.ViewHolder>(diffUtil){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnItemClickListener{
    	fun itemClick(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
    	val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)
    	return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
    	val item = getItem(position)
        (holder.itemView.findViewById(R.id.repositoryNameView) as TextView).text =
            item.name

        holder.itemView.setOnClickListener{
     		itemClickListener.itemClick(item)
    	}
    }
}
