package com.quovantis.photosearch.views.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quovantis.photosearch.databinding.ItemPhotoListBinding
import com.quovantis.photosearch.databinding.ItemRecentSearchBinding
import com.quovantis.photosearch.db.entities.RecentSearchData
import com.quovantis.photosearch.model.PhotoItem

class RecentSearchAdapter : RecyclerView.Adapter<RecentSearchAdapter.RecentSearchVH>() {

    inner class RecentSearchVH(val binding: ItemRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<RecentSearchData>() {

        override fun areItemsTheSame(oldItem: RecentSearchData, newItem: RecentSearchData): Boolean {
            return oldItem.createdAt == newItem.createdAt && oldItem.value == newItem.value
        }

        override fun areContentsTheSame(oldItem: RecentSearchData, newItem: RecentSearchData): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchVH {
        val binding =
            ItemRecentSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentSearchVH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecentSearchVH, position: Int) {
        val item = differ.currentList[position]

        holder.binding.apply {
            textviewSearch.text = item.value

            root.setOnClickListener {
                onItemClickListener?.let { listener ->
                    listener(item)
                }
            }
        }
    }

    private var onItemClickListener: ((RecentSearchData) -> Unit)? = null
    fun setOnItemClickListener(listener: (RecentSearchData) -> Unit) {
        onItemClickListener = listener
    }
}