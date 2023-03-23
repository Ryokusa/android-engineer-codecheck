/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import jp.co.yumemi.android.code_check.UtilCommon.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.RepositoryFragmentBinding

object RepositoryBindingAdapter{
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, imageUrl: String){
        view.load(imageUrl){
            placeholder(CircularProgressDrawable(view.context))
        }
    }
}

class RepositoryFragment : Fragment(R.layout.repository_fragment) {

    private val args: RepositoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RepositoryFragmentBinding.inflate(inflater, container, false)
        binding.repository = args.repository
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())
    }
}
