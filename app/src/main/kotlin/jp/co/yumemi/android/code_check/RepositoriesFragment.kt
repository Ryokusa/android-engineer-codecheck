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

    /** 検索ビューのEditorActionListenerを設定するBindingAdapter
     * @param view 検索欄のビュー
     * @param listener OnEditorActionListener
     */
    @BindingAdapter("onEditorAction")
    @JvmStatic
    fun setOnInputEditorAction(view: TextInputEditText, listener: TextView.OnEditorActionListener){
        view.setOnEditorActionListener(listener)
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

        initRepositoriesRecycler(binding.repositoriesRecycler)

        return binding.root
    }

    /** リサイクラービュー初期化
     * dataBindingにdividerとadapterをセット＆検索結果リポジトリ自動更新設定
     * @param repositoryRecycler 初期化したいリポジトリリサイクラービュー
     */
    private fun initRepositoriesRecycler(repositoryRecycler: RecyclerView){
        val context = requireContext()
        val layoutManager = LinearLayoutManager(context)
        val divider = DividerItemDecoration(context, layoutManager.orientation)
        repositoryRecycler.layoutManager = layoutManager
        repositoryRecycler.addItemDecoration(divider)

        val adapter = RepositoryAdapter(object : RepositoryAdapter.OnItemClickListener{
            override fun repositoryClick(repository: Repository){
                gotoRepositoryFragment(repository)
            }
        }, viewLifecycleOwner)
        repositoryRecycler.adapter = adapter

        viewModel.repositories.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    /** リポジトリ画面へ遷移
     * @param repository 遷移先に送るリポジトリ情報
     */

    fun gotoRepositoryFragment(repository: Repository)
    {
        val action = RepositoriesFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(repository)
        findNavController().navigate(action)
    }
}



