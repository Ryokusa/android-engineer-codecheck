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

    private var _repositories = listOf<Repository>()
    val repositories: List<Repository>
        get() = _repositories

    val searchText = MutableLiveData("")

    /** リポジトリ検索
     * 検索失敗時はリポジトリを空にし、エラートーストを表示
     */
    suspend fun repositoriesSearch(): List<Repository> =  withContext(Dispatchers.IO){
        lastSearchDate = Date()

        try {
            val gitHubClient = GithubClient()
            _repositories = gitHubClient.searchRepositories(searchText.value ?: "")
        }catch (e: Exception) {
            resetRepositories()
            throw e
        }

        return@withContext _repositories
    }

    private fun resetRepositories() {
        _repositories = listOf()
    }
}

