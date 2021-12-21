package com.quovantis.photosearch.views.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quovantis.photosearch.databinding.ItemPhotoListBinding
import com.quovantis.photosearch.model.PhotoItem

class PhotoListAdapter : RecyclerView.Adapter<PhotoListAdapter.PhotoListVH>() {

    inner class PhotoListVH(val binding: ItemPhotoListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<PhotoItem>() {

        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.media.mediaLink == newItem.media.mediaLink
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListVH {
        val binding =
            ItemPhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoListVH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PhotoListVH, position: Int) {
        val item = differ.currentList[position]

        holder.binding.apply {
            Glide.with(imageviewPhoto).load(item.media.mediaLink).into(imageviewPhoto)
            textviewTitle.text = item.title

            root.setOnClickListener {
                onItemClickListener?.let { listener ->
                    listener(item)
                }
            }
        }
    }

    private var onItemClickListener: ((PhotoItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (PhotoItem) -> Unit) {
        onItemClickListener = listener
    }
}