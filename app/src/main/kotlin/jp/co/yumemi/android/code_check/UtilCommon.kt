package jp.co.yumemi.android.code_check

import android.app.Application
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/** 汎用機能クラス
 * アプリ全体で使用する機能を持つ
 */
class UtilCommon: Application() {

    companion object {
        /** ソフトキーボードを非表示
         * @param context コンテキスト
         * @param currentView 現在のビュー
         */
        fun hideSoftKeyBoard(context: Context, currentView: View){
            val inputMethodService = context.getSystemService(InputMethodManager::class.java)
            inputMethodService.hideSoftInputFromWindow(
                currentView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS,
            )
        }

        /** エラーメッセージ表示
         * トーストでの表示
         * @param context コンテキスト
         * @param message メッセージ内容
         */
        fun showErrorMessage(context: Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}