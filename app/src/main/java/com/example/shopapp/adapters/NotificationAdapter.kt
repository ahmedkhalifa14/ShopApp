package com.example.shopapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.databinding.NotificationsItemBinding
import com.example.shopapp.models.Notifications

class NotificationAdapter(private val NotificationItem: List<Notifications>)

    : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(val binding:NotificationsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Notifications>() {
        override fun areItemsTheSame(oldItem: Notifications, newItem: Notifications): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notifications, newItem: Notifications): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            NotificationsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = differ.currentList[position]
        holder.itemView.apply {

            holder.binding.notificationsTitle.text = notification.title
            holder.binding.notificationMassageTv.text = notification.message
            setOnClickListener {
                onItemClickListener?.let { it(notification) }
            }

        }
    }

    override fun getItemCount(): Int = differ.currentList.size


    private var onItemClickListener: ((Notifications) -> Unit)? = null
    fun setOnItemClickListener(listener: (Notifications) -> Unit) {
        onItemClickListener = listener
    }
}