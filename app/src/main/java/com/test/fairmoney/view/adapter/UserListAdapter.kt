package com.test.fairmoney.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.fairmoney.R
import com.test.fairmoney.model.local.entities.User
import kotlinx.android.synthetic.main.user_list_item.view.*

class UserListAdapter(private val users: List<User>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(users[position])

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("DefaultLocale")
        fun bind(user: User) = with(itemView) {
            Picasso.get().load(user.userImage).into(user_image)
            user_image.transitionName = user.userImage
            user_email.text = user.email
            user_email.transitionName = user.email
            user_name.text = ("${user.title.capitalize()}. ${user.firstName} ${user.lastName}")
            setOnClickListener {
                listener.onItemClick(user, user_image, user_email)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: User, image: ImageView, email: TextView)
    }
}