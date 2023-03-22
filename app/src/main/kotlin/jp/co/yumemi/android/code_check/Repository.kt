package jp.co.yumemi.android.code_check

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONException
import org.json.JSONObject

/** リポジトリ
 * リポジトリ情報が入ったクラス
 * jsonから変換する関数を含む
 */
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
        /** Jsonからリポジトリリストへ変換
         * @param jsonBody JSONObject形式のリポジトリJSON配列
         * @return リポジトリリスト
         * @throws JSONException JSONパースエラー
         */
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

        /** JsonからRepositoryへ変換
         * @param jsonRepository JSONObject形式のリポジトリJson
         * @return 変換したRepository
         * @throws JSONException JSONパースエラー
         */
        private fun jsonObject2Repository(jsonRepository: JSONObject): Repository {
            val json = Json { ignoreUnknownKeys = true }

            try {
                return json.decodeFromString(jsonRepository.toString())
            }catch (e: Exception){
                throw JSONException("json parse error")
            }

        }
    }
}

/** リポジトリオーナー
 * リポジトリのオーナー情報をもったクラス
 */
@Parcelize
@Serializable
data class Owner(
    @SerialName("avatar_url") val ownerIconUrl: String
) : Parcelable