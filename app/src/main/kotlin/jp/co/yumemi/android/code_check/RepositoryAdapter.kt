package jp.co.yumemi.android.code_check

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.databinding.RepositoryItemBinding

/** リポジトリアダプター
 * リポジトリの表示処理を行う
 * @param repositoryClickListener リポジトリクリックイベント
 * @param viewLifeCycleOwner viewのライフサイクルオーナー
 */
class RepositoryAdapter(
    private val repositoryClickListener: OnItemClickListener,
    private val viewLifeCycleOwner: LifecycleOwner,
) : ListAdapter<Repository, RepositoryAdapter.RepositoryItemViewHolder>(diffUtil){

    /** リポジトリのViewHolderクラス
     * @param binding リポジトリアイテムのbinding
     */
    class RepositoryItemViewHolder(private val binding: RepositoryItemBinding): RecyclerView.ViewHolder(binding.root){
        /** binding設定
         * RepositoryItemBindingに各情報を格納する
         * @param repository 表示対象リポジトリ
         * @param repositoryClickListener リポジトリクリックリスナー
         * @param viewLifeCycleOwner viewのライフサイクルオーナー
         */
        fun bind(repository: Repository, repositoryClickListener: OnItemClickListener, viewLifeCycleOwner: LifecycleOwner) {
            binding.lifecycleOwner = viewLifeCycleOwner
            binding.repository = repository
            binding.itemClickListener = repositoryClickListener

            binding.executePendingBindings()
        }
    }

    /**　リポジトリクリックリスナー　*/
    interface OnItemClickListener{
        /** リポジトリクリックイベント
         *  @param repository クリックされたリポジトリ情報
         */
        fun repositoryClick(repository: Repository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RepositoryItemViewHolder(RepositoryItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RepositoryItemViewHolder, position: Int)
    {
        val repository = getItem(position)
        holder.bind(repository, repositoryClickListener, viewLifeCycleOwner)
    }
}

/** Repositoryアダプター用差分取得関数 */
private val diffUtil = object: DiffUtil.ItemCallback<Repository>(){
    override fun areItemsTheSame(oldRepository: Repository, newRepository: Repository): Boolean
    {
        return oldRepository.name == newRepository.name
    }

    override fun areContentsTheSame(oldRepository: Repository, newRepository: Repository): Boolean
    {
        return oldRepository == newRepository
    }
}
