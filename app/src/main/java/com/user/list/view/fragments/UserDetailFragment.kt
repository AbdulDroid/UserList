package com.user.list.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.user.list.R
import com.user.list.databinding.FragmentUserDetailBinding
import com.user.list.model.local.entities.FullUser
import com.user.list.model.local.entities.User
import com.user.list.utils.capitalize
import com.user.list.utils.formatDate
import com.user.list.view.MainActivity
import com.user.list.viewmodel.UserDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private val args by navArgs<UserDetailFragmentArgs>()

    private val viewModel: UserDetailViewModel by viewModels()
    private var _binding: FragmentUserDetailBinding? = null

    val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(R.transition.shared_transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.GONE
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        setViews(args.user)
        setObservers()
    }

    private fun setObservers() {
        with(viewModel) {
            loading.observe(viewLifecycleOwner) {
                binding.container.loader.visibility = if (it) View.VISIBLE else View.GONE
            }
            error.observe(viewLifecycleOwner) {
                Log.e("UserDetailFragment", it)
                binding.detailGroup.visibility = View.GONE
                Snackbar.make(binding.userImage, it, Snackbar.LENGTH_LONG).show()
            }
            user.observe(viewLifecycleOwner) {
                setFullDetails(it)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setViews(user: User) = with(user) {
        binding.container.loader.visibility = View.VISIBLE
        viewModel.getUser(userId)
        binding.userImage.transitionName = userImage
        Picasso.get().load(if(userImage == "") null else userImage).into(binding.userImage)
        binding.userEmail.transitionName = email
        binding.userEmail.text = email
        binding.userName.text = getString(
            R.string.username_text_template,
            title.capitalize(),
            firstName,
            lastName
        )
    }

    @SuppressLint("DefaultLocale")
    private fun setFullDetails(user: FullUser) {
        binding.phone.text = user.phone
        binding.dob.text = user.dob.formatDate()
        binding.regDate.text = user.registerDate.formatDate()
        binding.location.text = getString(
                R.string.location_text_template,
                user.location.street,
                user.location.city,
                user.location.state,
                user.location.country
            )
        binding.gender.text = user.gender.capitalize()
        binding.detailGroup.visibility = View.VISIBLE
    }
}