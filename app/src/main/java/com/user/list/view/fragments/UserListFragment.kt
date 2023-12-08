package com.user.list.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.user.list.databinding.FragmentUserListBinding
import com.user.list.model.local.entities.User
import com.user.list.view.MainActivity
import com.user.list.view.adapter.UserListAdapter
import com.user.list.viewmodel.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class UserListFragment : Fragment(), UserListAdapter.OnItemClickListener {

    private val viewModel: UserListViewModel by viewModels()
    private var _binding: FragmentUserListBinding? = null

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.VISIBLE
        with(binding.userList) {
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
            setHasFixedSize(true)
        }
        binding.container.loader.visibility = View.VISIBLE
        viewModel.getUsers()
        setObservers()
    }

    private fun setObservers() {
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.container.loader.visibility = if (it) View.VISIBLE else
                View.GONE
        }

        viewModel.users.observe(viewLifecycleOwner) {
            binding.userList.adapter = UserListAdapter(it, this).apply {
                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Log.e("UserListFragment", it)
            Snackbar.make(binding.userList, it, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onItemClick(user: User, image: ImageView, email: TextView) {
        val direction =
            UserListFragmentDirections.toUserDetailFragment(
                user
            )
        val extras = FragmentNavigatorExtras(image to user.userImage, email to user.email)
        findNavController().navigate(direction, extras)
    }
}