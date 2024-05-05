package com.example.gihubusertest.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gihubusertest.R
import com.example.gihubusertest.data.model.User
import com.example.gihubusertest.databinding.ActivityMainBinding
import com.example.gihubusertest.ui.detail.DetailUserActivity
import com.example.gihubusertest.ui.theme.SettingPreferences
import com.example.gihubusertest.ui.theme.SwitchThemeActivity
import com.example.gihubusertest.ui.theme.ThemeViewModel
import com.example.gihubusertest.ui.theme.ViewModelFactory
import com.example.gihubusertest.ui.theme.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var themeViewModel: ThemeViewModel

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

        val pref = SettingPreferences.getInstance(application.dataStore)
        themeViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(ThemeViewModel::class.java)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                //navigate to SwitchThemeActivity
                val intent = Intent(this, SwitchThemeActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu2 -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, FavoriteFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
