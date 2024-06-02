package com.example.gihubusertest.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.gihubusertest.data.model.DetailUserResponse
import com.example.gihubusertest.databinding.ActivityDetailUserBinding
import com.example.gihubusertest.ui.main.SectionPagerAdapter

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailUserViewModel>()

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME).orEmpty()
        val bundle = Bundle().apply {
            putString(EXTRA_USERNAME, username)
        }

        viewModel.users.observe(this) { user ->
            setDetailUsers(user)
        }

        if (username.isNotEmpty()) {
            viewModel.setUsersDetail(username)
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tab.setupWithViewPager(viewPager)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setDetailUsers(user: DetailUserResponse) {
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.login
            tvFollowing.text = "${user.following} Following"
            tvFollowers.text = "${user.followers} Followers"
            Glide.with(this@DetailUserActivity)
                .load(user.avatar_url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(imageProfile)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
