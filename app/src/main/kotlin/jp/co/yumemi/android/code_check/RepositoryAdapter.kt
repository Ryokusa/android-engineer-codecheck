package jp.co.yumemi.android.code_check

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RepositoryAdapter(
    private val repositoryClickListener: OnItemClickListener,
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

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnItemClickListener{
        fun repositoryClick(repository: Repository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val repository = getItem(position)
        val itemView = holder.itemView

        val repositoryNameView = itemView.findViewById<TextView>(R.id.repository_name_view)
        repositoryNameView.text = repository.name

        itemView.setOnClickListener{
            repositoryClickListener.repositoryClick(repository)
        }
    }
}
