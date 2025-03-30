package ru.practicum.android.diploma.ui.vacancy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemPhoneCommentBinding
import ru.practicum.android.diploma.domain.models.Phone

class PhoneAndCommentAdapter(
    private val clickListener: PhoneClickListener?
) : RecyclerView.Adapter<PhoneAndCommentViewHolder>() {

    private val phonesList = mutableListOf<Phone>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneAndCommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhoneCommentBinding.inflate(inflater, parent, false)
        return PhoneAndCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhoneAndCommentViewHolder, position: Int) {
        holder.bind(phonesList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return phonesList.size
    }

    fun setPhoneList(newPhoneList: List<Phone>) {
        val filteredList = newPhoneList.distinctBy { it.phone }
        phonesList.addAll(filteredList)
        notifyDataSetChanged()
    }

    interface PhoneClickListener {
        fun onPhoneClick(phone: Phone?)
    }
}
