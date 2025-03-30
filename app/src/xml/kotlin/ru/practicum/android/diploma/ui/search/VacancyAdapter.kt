package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemFoundVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter(
    private val clickListener: VacancyClickListener?
) : RecyclerView.Adapter<VacancyViewHolder>() {

    private val vacancyList = mutableListOf<Vacancy>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFoundVacancyBinding.inflate(inflater, parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancyList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }

    fun setVacancyList(newVacancyList: List<Vacancy>) {
        val diffCallback = DiffCallback(vacancyList, newVacancyList)
        val diffVacancyList = DiffUtil.calculateDiff(diffCallback)
        vacancyList.clear()
        vacancyList.addAll(newVacancyList)
        diffVacancyList.dispatchUpdatesTo(this)
    }

    interface VacancyClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }

    class DiffCallback(private val oldList: List<Vacancy>, private val newList: List<Vacancy>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
