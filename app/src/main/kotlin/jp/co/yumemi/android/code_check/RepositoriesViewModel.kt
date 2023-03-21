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
import kotlinx.coroutines.*
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONException
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
    suspend fun repositoriesSearch(inputText: String): List<Repository> =  withContext(Dispatchers.IO){
        if (inputText == "") return@withContext listOf()

        _repositories = getRepositories(inputText)

        lastSearchDate = Date()

        return@withContext _repositories
    }

    private suspend fun getRepositories(inputText: String): List<Repository> {
        val client = HttpClient(Android)
        val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", inputText)
        }

        try {
            val jsonBody = JSONObject(response.receive<String>())
            return jsonBody2Repositories(jsonBody)
        }catch (e: Exception){
            throw JSONException("json parse error")
        }

    }

    fun resetRepositories() {
        _repositories = listOf()
    }


    private fun jsonBody2Repositories(jsonBody: JSONObject):List<Repository>  {
        val jsonRepositories = jsonBody.optJSONArray("items") ?: throw JSONException("'items' can't get from json")

        val repositories = mutableListOf<Repository>()
        for (i in 0 until jsonRepositories.length()) {
            val jsonRepository = jsonRepositories.optJSONObject(i)
            val repository = jsonObject2Repository(jsonRepository)
            repositories.add(repository)
        }

        return repositories.toList()
    }

    private fun jsonObject2Repository(jsonRepository: JSONObject): Repository {
        val json = Json { ignoreUnknownKeys = true }

         return json.decodeFromString(jsonRepository.toString())
    }
}

@Parcelize
@kotlinx.serialization.Serializable
data class Repository(
    @SerialName("full_name") val name: String,
    @SerialName("owner") val owner: Owner,
    @SerialName("language") val language: String?="",
    @SerialName("stargazers_count") val stargazersCount: Long,
    @SerialName("watchers_count")val watchersCount: Long,
    @SerialName("forks_count") val forksCount: Long,
    @SerialName("open_issues_count") val openIssuesCount: Long,
) : Parcelable

@Parcelize
@kotlinx.serialization.Serializable
data class Owner(
    @SerialName("avatar_url") val ownerIconUrl: String
) :Parcelable