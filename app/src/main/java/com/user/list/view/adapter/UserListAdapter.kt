package com.user.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.user.list.R
import com.user.list.databinding.UserListItemBinding
import com.user.list.model.local.entities.User
import com.user.list.utils.capitalize
import java.util.Locale

class UserListAdapter(private val users: List<User>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(users[position])

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(val binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(user: User) = with(itemView) {
            Picasso.get().load(user.userImage).into(binding.userImage)
            binding.userImage.transitionName = user.userImage
            binding.userEmail.text = user.email
            binding.userEmail.transitionName = user.email
            binding.userName.text = itemView.context.getString(
                R.string.username_text_template,
                user.title.capitalize(),
                user.firstName,
                user.lastName
            )
            setOnClickListener {
                listener.onItemClick(user, binding.userImage, binding.userEmail)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: User, image: ImageView, email: TextView)
    }
}