package com.example.gihubusertest.ui.main

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

class UserFragment : Fragment() {

    private var tabName: String? = null

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabName = arguments?.getString(ARG_TAB)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }

        val userAdapter = UserAdapter { user ->
            if (user.isBookmarked){
                viewModel.deleteUser(user)
            } else {
                viewModel.saveUser(user)
            }
        }

        if (tabName == TAB_USER) {
            viewModel.getListUser("").observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            val userData = result.data
                            userAdapter.submitList(userData)
                        }

                        is Result.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                context,
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else if (tabName == TAB_FAVORITE) {
            viewModel.getBookmarkedUsers().observe(viewLifecycleOwner) { bookmarkedUsers ->
                binding?.progressBar?.visibility = View.GONE
                userAdapter.submitList(bookmarkedUsers)
            }
        }

        binding?.rvUser?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_TAB = "tab_name"
        const val TAB_USER = "user"
        const val TAB_FAVORITE = "favorite"
    }
}