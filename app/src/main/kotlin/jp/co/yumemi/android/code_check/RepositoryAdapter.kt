package jp.co.yumemi.android.code_check

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.databinding.RepositoryItemBinding

class RepositoryAdapter(
    private val repositoryClickListener: OnItemClickListener,
    private val viewLifeCycleOwner: LifecycleOwner,
) : ListAdapter<Repository, RepositoryAdapter.ViewHolder>(diffUtil){

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<Repository>(){
            override fun areItemsTheSame(oldRepository: Repository, newRepository: Repository): Boolean
            {
                return oldRepository.name == newRepository.name
            }

            override fun areContentsTheSame(oldRepository: Repository, newRepository: Repository): Boolean
            {
                return oldRepository == newRepository
            }
        }
    }

    class ViewHolder(private val binding: RepositoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Repository, repositoryClickListener: OnItemClickListener, viewLifeCycleOwner: LifecycleOwner) {
            binding.lifecycleOwner = viewLifeCycleOwner
            binding.repository = item
            binding.itemClickListener = repositoryClickListener

            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener{
        fun repositoryClick(repository: Repository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(RepositoryItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val repository = getItem(position)
        holder.bind(repository, repositoryClickListener, viewLifeCycleOwner)
    }
}
