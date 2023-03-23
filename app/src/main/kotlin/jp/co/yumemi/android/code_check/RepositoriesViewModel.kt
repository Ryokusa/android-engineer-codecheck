/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.app.Application
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.UtilCommon.Companion.lastSearchDate
import kotlinx.coroutines.*
import org.json.JSONException
import java.util.*

class RepositoriesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    val repositories = MutableLiveData<List<Repository>>()
    val searchText = MutableLiveData("")
    val searching = MutableLiveData(false)

    val onEditorAction = OnEditorActionListener{editText, action, _ ->
        if (action == EditorInfo.IME_ACTION_SEARCH){
            UtilCommon.hideSoftKeyBoard(context, editText)
            editText.clearFocus()
            searchRepositories()
            return@OnEditorActionListener true
        }
        return@OnEditorActionListener false
    }

    /** リポジトリ検索
     * 検索成功時は RepositoriesViewModel.repositories に非同期で検索結果が入る
     * 検索失敗時はリポジトリを空にし、エラートーストを表示
     * @throws JSONException Jsonパースエラー
     * @throws Exception その他通信等エラー
     */
    private fun searchRepositories() {
        viewModelScope.launch{
            searching.value = true
            lastSearchDate = Date()
            resetRepositories()

            val gitHubClient = GithubClient()
            try{
                repositories.value = gitHubClient.searchRepositories(searchText.value ?: "")
            }catch (e: Exception){
                showSearchError(e)
                e.printStackTrace()
            }

            searching.value = false
        }
    }

    /** リポジトリ初期化
     * 検索結果リポジトリを空のlistにする
     */
    private fun resetRepositories() {
        repositories.value = listOf()
    }

    /** 検索エラー表示
     * @param e 検索時に発生したException
     */
    private suspend fun showSearchError(e: Exception) = withContext(Dispatchers.Main){
        var errMessage = context.getString(R.string.error) + ": "
        errMessage += when (e) {
            is JSONException -> context.getString(R.string.json_parse_error_occurred)
            else -> context.getString(R.string.json_parse_error_occurred)
        }
        UtilCommon.showErrorMessage(context, errMessage)
    }
}

