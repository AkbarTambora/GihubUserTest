package com.example.gihubusertest.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gihubusertest.data.local.entity.UserEntity
import com.example.gihubusertest.ui.detail.DetailUserActivity
import com.example.gihubusertest.ui.theme.SettingPreferences
import com.example.gihubusertest.ui.theme.SwitchThemeActivity
import com.example.gihubusertest.ui.theme.ThemeViewModel
import com.example.gihubusertest.ui.theme.ThemeViewModelFactory
import com.example.gihubusertest.ui.theme.dataStore
import com.example.githubusertest.R
import com.example.githubusertest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var themeViewModel: ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(applicationContext)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        adapter = UserAdapter(
            onItemClick = { userEntity ->
                navigateToDetailUser(userEntity.login)
            },
            onBookmarkClick = { userEntity ->
                toggleBookmark(userEntity)
            }
        )

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
            if (users != null) {
                adapter.submitList(users)  // Gunakan submitList untuk ListAdapter
                showLoading(false)
            } else {
                adapter.submitList(emptyList())
            }
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(ThemeViewModel::class.java)

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

    private fun toggleBookmark(user: UserEntity) {
        val newBookmarkState = !user.isBookmarked
        mainViewModel.setBookmarkedUsers(user, newBookmarkState)
        adapter.notifyItemChanged(adapter.currentList.indexOf(user))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                val intent = Intent(this, SwitchThemeActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, FavoriteFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
