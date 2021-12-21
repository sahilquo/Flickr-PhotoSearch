package com.quovantis.photosearch.views.detail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.quovantis.photosearch.R
import com.quovantis.photosearch.databinding.ActivityPhotoDetailBinding
import com.quovantis.photosearch.model.PhotoItem
import com.quovantis.photosearch.utils.ValueUtils.getAuthorName
import com.quovantis.photosearch.utils.ValueUtils.getDescriptionText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailActivity : AppCompatActivity() {

    companion object {
        const val ARG_PHOTO_DATA = "ARG_PHOTO_DATA"

        fun start(context: Context, photoItem: PhotoItem) {
            val intent = Intent(context, PhotoDetailActivity::class.java)
            intent.apply {
                putExtras(Bundle().apply {
                    putSerializable(ARG_PHOTO_DATA, photoItem)
                })
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI(binding)

        intent.extras?.let { bundle ->
            val photoData = bundle.getSerializable(ARG_PHOTO_DATA) as? PhotoItem
            photoData?.let {
                showPhotoData(binding, it)
            }
        }
    }

    private fun initializeUI(binding: ActivityPhotoDetailBinding) {
        with(binding) {
            setSupportActionBar(toolbar)

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun showPhotoData(binding: ActivityPhotoDetailBinding, photoData: PhotoItem) {
        with(binding) {
            Glide.with(imageviewPhoto)
                .asBitmap()
                .load(photoData.media.mediaLink)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        textviewDimensions.text =
                            String.format(
                                getString(R.string.dimensions_s_s),
                                resource.width,
                                resource.height
                            )
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })

            Glide.with(imageviewPhoto).load(photoData.media.mediaLink).into(imageviewPhoto)
            textviewTitle.isVisible = photoData.title.isNotEmpty()
            textviewTitle.text = String.format(getString(R.string.title_s), photoData.title)
            supportActionBar?.title = photoData.title

            val updatedAuthorName = photoData.author.getAuthorName()
            textviewAuthor.isVisible = !updatedAuthorName.isNullOrEmpty()
            textviewAuthor.text = String.format(getString(R.string.author_s), updatedAuthorName)

            val updatedDescription = photoData.description.getDescriptionText()
            textviewDescription.isVisible = updatedDescription.isNotEmpty()
            textviewDescription.text =
                String.format(getString(R.string.description_s), updatedDescription)
        }
    }
}