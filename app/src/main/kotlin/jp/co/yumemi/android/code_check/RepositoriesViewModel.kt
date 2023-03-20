/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import java.util.*

class RepositoriesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    private var _repositories = listOf<Repository>()
    val repositories: List<Repository>
        get() = _repositories

    // 検索結果
    fun repositoriesSearch(inputText: String): List<Repository> = runBlocking {
        if (inputText == "") return@runBlocking listOf()

        return@runBlocking async {
            _repositories = getRepositories(inputText)

            lastSearchDate = Date()

            return@async _repositories
        }.await()
    }

    private suspend fun getRepositories(inputText: String): List<Repository> {
        //TODO: 例外処理
        val client = HttpClient(Android)
        val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", inputText)
        }

        val jsonBody = JSONObject(response.receive<String>())
        return jsonBody2Repositories(jsonBody)
    }

    private fun jsonBody2Repositories(jsonBody: JSONObject):List<Repository>  {
        val jsonRepositories = jsonBody.optJSONArray("items") ?: throw Error("'items' can't get from json")

        val repositories = mutableListOf<Repository>()
        for (i in 0 until jsonRepositories.length()) {
            val jsonRepository = jsonRepositories.optJSONObject(i)
            val repository = jsonObject2Repository(jsonRepository)
            repositories.add(repository)
        }

        return repositories.toList()
    }

    private fun jsonObject2Repository(jsonRepository: JSONObject): Repository {
        val name = jsonRepository.optString("full_name")
        val ownerIconUrl = jsonRepository.optJSONObject("owner")?.optString("avatar_url")
            ?: throw Error("'owner' can't get from json")
        val language = jsonRepository.optString("language")
        val stargazersCount = jsonRepository.optLong("stargazers_count")
        val watchersCount = jsonRepository.optLong("watchers_count")
        val forksCount = jsonRepository.optLong("forks_count")
        val openIssuesCount = jsonRepository.optLong("open_issues_count")

        return Repository(
            name,
            ownerIconUrl,
            context.getString(R.string.written_language, language),
            stargazersCount,
            watchersCount,
            forksCount,
            openIssuesCount
        )
    }
}

@Parcelize
data class Repository(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable