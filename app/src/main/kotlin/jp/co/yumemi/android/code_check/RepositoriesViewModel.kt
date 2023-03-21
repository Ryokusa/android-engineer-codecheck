/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import jp.co.yumemi.android.code_check.UtilCommon.Companion.lastSearchDate
import kotlinx.coroutines.*
import java.util.*

class RepositoriesViewModel(
    application: Application
) : AndroidViewModel(application) {

    val repositories = MutableLiveData<List<Repository>>()

    val searchText = MutableLiveData("")

    /** リポジトリ検索
     * 検索失敗時はリポジトリを空にし、エラートーストを表示
     */
    suspend fun repositoriesSearch(): List<Repository> = withContext(Dispatchers.Main){
        lastSearchDate = Date()
        resetRepositories()

        val gitHubClient = GithubClient()
        repositories.value = gitHubClient.searchRepositories(searchText.value ?: "")

        return@withContext repositories.value ?: listOf()
    }

    private fun resetRepositories() {
        repositories.value = listOf()
    }
}

