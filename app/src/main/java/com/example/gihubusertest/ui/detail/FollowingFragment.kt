package com.example.gihubusertest.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gihubusertest.data.local.entity.UserEntity
import com.example.gihubusertest.ui.main.UserAdapter
import com.example.gihubusertest.R
import com.example.gihubusertest.databinding.FragmentFollowBinding

class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).orEmpty()

        _binding = FragmentFollowBinding.bind(view)

        adapter = UserAdapter(
            onBookmarkClick = { userEntity ->
                // Tambahkan logika untuk toggle bookmark di sini jika diperlukan
            }
        )

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }

        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        viewModel.setListFollowing(username)
        viewModel.listFollowing.observe(viewLifecycleOwner) { users ->
            if (users != null) {
                val userEntities = users.map { user ->
                    UserEntity(user.id, user.login, user.avatar_url, false)
                }
                adapter.submitList(userEntities)
                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
