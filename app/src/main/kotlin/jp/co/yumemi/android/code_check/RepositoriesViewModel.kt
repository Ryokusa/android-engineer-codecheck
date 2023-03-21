/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.app.Application
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import jp.co.yumemi.android.code_check.UtilCommon.Companion.lastSearchDate
import kotlinx.coroutines.*
import org.json.JSONException
import java.util.*

class RepositoriesViewModel(
    application: Application
) : AndroidViewModel(application) {

    object RepositoriesBindingAdapter {
        @BindingAdapter("onEditorAction")
        @JvmStatic
        fun setOnInputEditorAction(view: TextInputEditText, listener: OnEditorActionListener){
            view.setOnEditorActionListener(listener)
        }
    }

    private val context = application

    val repositories = MutableLiveData<List<Repository>>()
    val searchText = MutableLiveData("")

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
     * 検索失敗時はリポジトリを空にし、エラートーストを表示
     */
    fun searchRepositories() {
        viewModelScope.launch{
            lastSearchDate = Date()
            resetRepositories()

            val gitHubClient = GithubClient()
            try{
                repositories.value = gitHubClient.searchRepositories(searchText.value ?: "")
            }catch (e: Exception){
                showSearchError(e)
                e.printStackTrace()
            }
        }
    }

    private fun resetRepositories() {
        repositories.value = listOf()
    }

    /** 検索エラー表示
     * @param e 検索時に発生したException
     */
    private suspend fun showSearchError(e: Exception) = withContext(Dispatchers.Main){
        when (e) {
            is JSONException -> UtilCommon.showErrorMessage(context, "JSONパースエラー")
            else -> UtilCommon.showErrorMessage(context, "検索エラー")
        }
    }
}

