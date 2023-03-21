package jp.co.yumemi.android.code_check

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

/** Github APIクラス
 * GithubAPIを簡単にアクセスするためのクラス
 */
class GithubClient {
    companion object{
        private const val API_URL = "https://api.github.com/"
        const val SEARCH_API_URL = API_URL + "search/"
    }

    private val client = HttpClient(Android)

    /** リポジトリ検索
     * 検索文字がない場合は空のリストを返す
     * @param searchText 検索値
     * @return リポジトリ配列
     * @throws JSONException パースエラー
     */
    suspend fun searchRepositories(searchText: String): List<Repository> = withContext(Dispatchers.IO){
        if (searchText == "") return@withContext listOf()

        val url = SEARCH_API_URL + "repositories"

        val response: HttpResponse = client.get(url) {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", searchText)
        }

        try {
            val jsonBody = JSONObject(response.receive<String>())
            return@withContext Repository.jsonBody2Repositories(jsonBody)
        }catch (e: Exception){
            throw JSONException("json parse error")
        }
    }
}