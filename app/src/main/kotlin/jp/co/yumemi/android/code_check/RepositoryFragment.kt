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

        val repository = args.repository
        binding.ownerIconView.load(repository.owner.ownerIconUrl)
        binding.nameView.text = repository.name
        binding.languageView.text = getString(R.string.written_language, repository.language) ?: getString(R.string.nothing_language)
        binding.starsView.text = getString(R.string.stars, repository.stargazersCount)
        binding.watchersView.text = getString(R.string.watchers, repository.watchersCount)
        binding.forksView.text = getString(R.string.forks, repository.forksCount)
        binding.openIssuesView.text = getString(R.string.open_issues, repository.openIssuesCount)
    }
}
