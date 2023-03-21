/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import kotlinx.coroutines.*
import org.json.JSONException
import java.util.*

class RepositoriesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    private var _repositories = listOf<Repository>()
    val repositories: List<Repository>
        get() = _repositories

    /** リポジトリ検索
     * 検索失敗時はリポジトリを空にし、エラートーストを表示
     * @param searchText 検索文字
     */
    suspend fun repositoriesSearch(searchText: String): List<Repository> =  withContext(Dispatchers.IO){
        try {
            val gitHubClient = GithubClient()
            _repositories = gitHubClient.searchRepositories(searchText)
        }catch (e: Exception) {
            resetRepositories()
            showSearchError(e)
        }

        lastSearchDate = Date()

        return@withContext _repositories
    }

    /** 検索エラー表示
     * @param e 検索時に発生したException
     */
    private suspend fun showSearchError(e: Exception) = withContext(Dispatchers.Main){
        when (e) {
            is JSONException -> UtilCommon.showErrorMessage(context, "JSONパースエラー")
            else -> UtilCommon.showErrorMessage(context, "検索エラー")
        }
        e.printStackTrace()
    }

    private fun resetRepositories() {
        _repositories = listOf()
    }
}

