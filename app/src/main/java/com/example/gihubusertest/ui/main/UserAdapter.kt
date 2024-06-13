package com.example.gihubusertest.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.gihubusertest.data.local.entity.UserEntity
import com.example.gihubusertest.R
import com.example.gihubusertest.databinding.ItemUserBinding
import com.example.gihubusertest.ui.detail.DetailUserActivity

class UserAdapter(
    private val onBookmarkClick: (UserEntity) -> Unit
) : ListAdapter<UserEntity, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val ivBookmark = holder.binding.ivBookmark
        if (user.isBookmarked) {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmarked_white))
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmark_white))
        }
        ivBookmark.setOnClickListener {
            onBookmarkClick(user)
        }
    }

    inner class UserViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            with(binding) {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivUser)
                tvUsername.text = user.login
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailUserActivity::class.java).apply {
                        putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }

    fun updateUser(user: UserEntity) {
        val index = currentList.indexOfFirst { it.id == user.id }
        if (index != -1) {
            val updatedList = currentList.toMutableList()
            updatedList[index] = user
            submitList(updatedList)
        }
    }
}


