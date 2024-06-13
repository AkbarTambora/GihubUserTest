package com.example.gihubusertest.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gihubusertest.R
import com.example.gihubusertest.databinding.FragmentUserBinding
import com.example.gihubusertest.data.source.Result
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.gihubusertest.ui.detail.DetailUserActivity

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
        observeViewModel()
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
    }

    private fun observeViewModel() {
        if (tabName == TAB_USER) {
            viewModel.getListUser("").observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val userData = result.data
                            userAdapter.submitList(userData)
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                context,
                                "Terjadi kesalahan: ${result.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else if (tabName == TAB_FAVORITE) {
            viewModel.getBookmarkedUsers().observe(viewLifecycleOwner) { bookmarkedUsers ->
                binding.progressBar.visibility = View.GONE
                userAdapter.submitList(bookmarkedUsers)
            }
        }
    }

    private fun searchUser() {
        val query = binding.etQuery.text.toString().trim()
        if (query.isNotEmpty()) {
            showLoading(true)
            viewModel.getListUser(query).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val userData = result.data
                        userAdapter.submitList(userData)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan: ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun navigateToDetailUser(username: String) {
        val intent = Intent(requireContext(), DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_USERNAME, username)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
