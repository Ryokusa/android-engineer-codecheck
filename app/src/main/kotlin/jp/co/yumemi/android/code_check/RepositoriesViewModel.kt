/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import kotlinx.coroutines.*
import java.util.*

class RepositoriesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    private var _repositories = listOf<Repository>()
    val repositories: List<Repository>
        get() = _repositories

    // 検索結果
    suspend fun repositoriesSearch(inputText: String): List<Repository> =  withContext(Dispatchers.IO){
        val gitHubClient = GithubClient()
        _repositories = gitHubClient.searchRepositories(inputText)

        lastSearchDate = Date()

        return@withContext _repositories
    }

    fun resetRepositories() {
        _repositories = listOf()
    }
}

