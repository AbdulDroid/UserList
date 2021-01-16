package com.test.fairmoney.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.test.fairmoney.App
import com.test.fairmoney.R
import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.local.entities.User
import com.test.fairmoney.utils.formatDate
import com.test.fairmoney.utils.inject
import com.test.fairmoney.viewmodel.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_user_detail.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class UserDetailFragment : Fragment() {
    private val args by navArgs<UserDetailFragmentArgs>()

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.shared_transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = inject(factory)
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.visibility = View.GONE
        back_button.setOnClickListener {
            findNavController().navigateUp()
        }
        setViews(args.user)
        setObservers()
    }

    private fun setObservers() {
        with(viewModel) {
            loading.observe(viewLifecycleOwner) {
                loader.visibility = if (it) View.VISIBLE else View.GONE
            }
            error.observe(viewLifecycleOwner) {
                Log.e("UserDetailFragment", it)
                detailGroup.visibility = View.GONE
                Snackbar.make(user_image, it, Snackbar.LENGTH_LONG).show()
            }
            user.observe(viewLifecycleOwner) {
                setFullDetails(it)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setViews(user: User) = with(user) {
        loader.visibility = View.VISIBLE
        viewModel.getUser(userId)
        user_image.transitionName = userImage
        Picasso.get().load(if(userImage == "") null else userImage).into(user_image)
        user_email.transitionName = email
        user_email.text = email
        user_name.text = ("${title.capitalize()}. $firstName $lastName")
    }

    override fun onAttach(context: Context) {
        App.appComponent(context).inject(this)
        super.onAttach(context)
    }

    @SuppressLint("DefaultLocale")
    private fun setFullDetails(user: FullUser) {
        phone.text = user.phone
        dob.text = user.dob.formatDate()
        reg_date.text = user.registerDate.formatDate()
        location.text =
            ("${user.location.street}, ${user.location.city}, ${user.location.state}, ${user.location.country}")
        gender.text = user.gender.capitalize()
        detailGroup.visibility = View.VISIBLE
    }
}