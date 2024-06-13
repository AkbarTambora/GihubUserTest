package com.example.gihubusertest.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gihubusertest.data.source.Result
import com.example.gihubusertest.databinding.FragmentUserBinding
import androidx.core.widget.addTextChangedListener

class UserFragment : Fragment() {

    private var tabName: String? = null

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    companion object {
        const val ARG_TAB = "tab_name"
        const val TAB_USER = "user"
        const val TAB_FAVORITE = "favorite"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabName = arguments?.getString(ARG_TAB)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        setupViews()
    }

    private fun setupViews() {
        userAdapter = UserAdapter { user ->
            if (user.isBookmarked) {
                viewModel.deleteUser(user)
            } else {
                viewModel.saveUser(user)
            }
        }

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = userAdapter
        }

        binding.btnSearch.setOnClickListener { searchUser() }

        binding.etQuery.addTextChangedListener {
            if (it?.toString()?.isNotEmpty() == true) {
                searchUser()
            }
        }

        if (tabName == TAB_USER) {
            observeUsers()
        } else if (tabName == TAB_FAVORITE) {
            observeFavoriteUsers()
        }

        observeFavoriteStatusChanged()
    }

    private fun observeUsers() {
        viewModel.users.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    userAdapter.submitList(result.data)
                }

                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        context,
                        "Terjadi kesalahan: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observeFavoriteUsers() {
        viewModel.getBookmarkedUsers().observe(viewLifecycleOwner) { bookmarkedUsers ->
            showLoading(false)
            userAdapter.submitList(bookmarkedUsers)
        }
    }

    private fun observeFavoriteStatusChanged() {
        viewModel.favoriteStatusChanged.observe(viewLifecycleOwner) { updatedUser ->
            userAdapter.updateUser(updatedUser)
        }
    }

    private fun searchUser() {
        val query = binding.etQuery.text.toString().trim()
        if (query.isNotEmpty()) {
            val isConnected = isNetworkAvailable(requireContext())
            viewModel.setSearchUsers(query, isConnected)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}

