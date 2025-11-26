package ru.practicum.android.diploma.ui.vacancy

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemPhoneCommentBinding
import ru.practicum.android.diploma.domain.models.Phone

class PhoneAndCommentViewHolder(
    private val binding: ItemPhoneCommentBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(phone: Phone?, phoneClickListener: PhoneAndCommentAdapter.PhoneClickListener?) {
        binding.tvPhone.text = phone?.phone

        if (phone?.comment.isNullOrEmpty()) {
            binding.tvCommentCaption.visibility = View.GONE
            binding.tvComment.visibility = View.GONE
        } else {
            binding.tvComment.text = phone?.comment
        }

        binding.tvPhone.setOnClickListener {
            phoneClickListener?.onPhoneClick(phone)
        }
    }
}
