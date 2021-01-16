package com.test.fairmoney.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.test.fairmoney.App
import com.test.fairmoney.R
import com.test.fairmoney.model.local.entities.User
import com.test.fairmoney.utils.inject
import com.test.fairmoney.view.adapter.UserListAdapter
import com.test.fairmoney.viewmodel.UserListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_user_list.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class UserListFragment : Fragment(), UserListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: UserListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = inject(factory)
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.visibility = View.VISIBLE
        with(userList) {
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
            setHasFixedSize(true)
        }
        loader.visibility = View.VISIBLE
        viewModel.getUsers()
        setObservers()
    }

    private fun setObservers() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            loader.visibility = if (it) View.VISIBLE else
                View.GONE
        })

        viewModel.users.observe(viewLifecycleOwner, Observer {
            userList.adapter = UserListAdapter(it, this).apply {
                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            Log.e("UserListFragment", it)
            Snackbar.make(userList, it, Snackbar.LENGTH_LONG).show()
        })
    }

    override fun onAttach(context: Context) {
        App.appComponent(context).inject(this)
        super.onAttach(context)
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