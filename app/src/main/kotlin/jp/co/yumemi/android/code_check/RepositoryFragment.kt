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
import jp.co.yumemi.android.code_check.UtilCommon.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.RepositoryFragmentBinding

class RepositoryFragment : Fragment(R.layout.repository_fragment) {

    private val args: RepositoryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        val binding = RepositoryFragmentBinding.bind(view)
        setRepository(binding)
    }

    private fun setRepository(binding: RepositoryFragmentBinding){
        val repository = args.repository

        with(binding) {
            ownerIconView.load(repository.owner.ownerIconUrl)
            nameView.text = repository.name
            languageView.text = getString(R.string.written_language, repository.language) ?: getString(R.string.nothing_language)
            starsView.text = getString(R.string.stars, repository.stargazersCount)
            watchersView.text = getString(R.string.watchers, repository.watchersCount)
            forksView.text = getString(R.string.forks, repository.forksCount)
            openIssuesView.text = getString(R.string.open_issues, repository.openIssuesCount)
        }
    }
}
