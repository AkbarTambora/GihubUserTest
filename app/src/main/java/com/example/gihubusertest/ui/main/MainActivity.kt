package com.example.gihubusertest.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gihubusertest.data.model.User
import com.example.gihubusertest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        adapter = UserAdapter().apply {
            setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: User) {
                    navigateToDetailUser(data.login)
                }
            })
        }

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
        }

        binding.btnSearch.setOnClickListener { searchUser() }

        binding.etQuery.addTextChangedListener {
            if (it?.toString()?.isNotEmpty() == true) {
                searchUser()
            }
        }
    }

    private fun observeViewModel() {
        mainViewModel.listUsers.observe(this) { users ->
            users?.let {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    private fun searchUser() {
        val query = binding.etQuery.text.toString().trim()
        if (query.isNotEmpty()) {
            showLoading(true)
            mainViewModel.setSearchUsers(query)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun navigateToDetailUser(username: String) {
        Intent(this, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_USERNAME, username)
            startActivity(this)
        }
    }
}
