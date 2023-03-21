package jp.co.yumemi.android.code_check

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONException
import org.json.JSONObject

@Parcelize
@Serializable
data class Repository(
    @SerialName("full_name") val name: String,
    @SerialName("owner") val owner: Owner,
    @SerialName("language") val language: String?="",
    @SerialName("stargazers_count") val stargazersCount: Long,
    @SerialName("watchers_count")val watchersCount: Long,
    @SerialName("forks_count") val forksCount: Long,
    @SerialName("open_issues_count") val openIssuesCount: Long,
) : Parcelable {
    companion object{
        fun jsonBody2Repositories(jsonBody: JSONObject):List<Repository>  {
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
}

@Parcelize
@Serializable
data class Owner(
    @SerialName("avatar_url") val ownerIconUrl: String
) : Parcelable