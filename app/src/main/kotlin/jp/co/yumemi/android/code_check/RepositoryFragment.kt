/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.RepositoryFragmentBinding

class RepositoryFragment : Fragment(R.layout.repository_fragment) {

    private val args: RepositoryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        val binding = RepositoryFragmentBinding.bind(view)

        val context = requireContext()

        val repository = args.repository
        binding.ownerIconView.load(repository.owner.ownerIconUrl)
        binding.nameView.text = repository.name
        binding.languageView.text = context.getString(R.string.written_language, repository.language)
        binding.starsView.text = "${repository.stargazersCount} stars"
        binding.watchersView.text = "${repository.watchersCount} watchers"
        binding.forksView.text = "${repository.forksCount} forks"
        binding.openIssuesView.text = "${repository.openIssuesCount} open issues"
    }
}
