package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.presentation.vacancy.VacancyViewModel
import ru.practicum.android.diploma.presentation.vacancy.state.VacancyFragmentScreenState
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.util.getSalaryStringAndSymbol

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {

    private val viewModel by viewModel<VacancyViewModel>()
    private var vacancyId = ""
    private var vacancyDetails: VacancyDetails? = null
    private var adapter: PhoneAndCommentAdapter? = null
    private val phoneList: ArrayList<Phone> = arrayListOf()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getVacancyFragmentScreenState().observe(viewLifecycleOwner) {
            render(it)
        }

        vacancyId = requireArguments().getString(ARGS_VACANCY).toString()
        viewModel.getVacancyDetails(vacancyId)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnShare.setOnClickListener {
            viewModel.shareVacancy(vacancyDetails?.alternateUrl)
        }

        binding.tvEmail.setOnClickListener {
            viewModel.openEmail(vacancyDetails?.contactEmail)
        }

        adapter = PhoneAndCommentAdapter(phoneList, object : PhoneAndCommentAdapter.PhoneClickListener {
            override fun onPhoneClick(phone: Phone?) {
                viewModel.openPhone(phone?.phone)
            }
        })

        binding.phoneAndCommentRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.phoneAndCommentRecyclerView.adapter = adapter
    }

    private fun render(state: VacancyFragmentScreenState) {
        when (state) {
            is VacancyFragmentScreenState.Content -> showContent(state.vacancy)
            is VacancyFragmentScreenState.ServerError -> showError()
            is VacancyFragmentScreenState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.flProgressBar.visibility = View.VISIBLE
        binding.llErrorServer.visibility = View.GONE
        binding.svVacancy.visibility = View.GONE
    }

    private fun showError() {
        binding.llErrorServer.visibility = View.VISIBLE
        binding.flProgressBar.visibility = View.GONE
        binding.svVacancy.visibility = View.GONE
    }

    private fun showContent(vacancy: VacancyDetails) {
        vacancyDetails = vacancy
        binding.svVacancy.visibility = View.VISIBLE
        binding.llErrorServer.visibility = View.GONE
        binding.flProgressBar.visibility = View.GONE
        installVacancyDetails(vacancy)
    }

    private fun installVacancyDetails(vacancy: VacancyDetails) {
        binding.tvVacancyName.text = vacancy.name
        binding.tvSalary.text = this.context?.let { getSalaryStringAndSymbol(vacancy.salary, it) }
        binding.tvEmployerName.text = vacancy.employerName
        binding.tvExperienceCaption.text = vacancy.experience
        binding.tvEmploymentSchedule.text =
            getString(R.string.employment_schedule, vacancy.employment, vacancy.schedule)
        binding.tvDescription.setText(Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_COMPACT))

        if (vacancy.address.isNotEmpty()) {
            binding.tvAreaName.text = vacancy.address
        } else {
            binding.tvAreaName.text = vacancy.areaName
        }

        if (vacancy.keySkills.isNullOrEmpty()) {
            binding.keySkillsGroup.visibility = View.GONE
        } else {
            binding.tvSkills.text = viewModel.getStringKeySkills(vacancy.keySkills)
        }

        installVacancyContacts(vacancy)

        Glide.with(binding.ivEmployerLogo)
            .load(vacancy.employerLogoUrl)
            .placeholder(R.drawable.ic_placeholder_vacancy_item)
            .centerCrop()
            .transform(RoundedCorners(binding.ivEmployerLogo.resources.getDimensionPixelSize(R.dimen._12dp)))
            .into(binding.ivEmployerLogo)
    }

    private fun installVacancyContacts(vacancy: VacancyDetails) {
        if (vacancy.contactEmail.isNullOrEmpty()
            && vacancy.contactName.isNullOrEmpty()
            && vacancy.contactPhones.isNullOrEmpty()
        ) {
            binding.ContactsGroup.visibility = View.GONE
        } else {
            if (vacancy.contactName.isNullOrEmpty()) {
                binding.ContactNameGroup.visibility = View.GONE
            } else {
                binding.tvContact.text = vacancy.contactName
            }

            if (vacancy.contactEmail.isNullOrEmpty()) {
                binding.ContactEmailGroup.visibility = View.GONE
            } else {
                binding.tvEmail.text = vacancy.contactEmail
            }
            if (vacancy.contactPhones.isNullOrEmpty()) {
                binding.phoneAndCommentRecyclerView.visibility = View.GONE
            } else {
                adapter?.let {
                    phoneList.clear()
                    phoneList.addAll(vacancy.contactPhones)
                    it.notifyDataSetChanged()
                }
            }
        }
    }

    companion object {
        private const val ARGS_VACANCY = "ARGS_VACANCY_ID"

        fun createArgs(vacancyId: String): Bundle = bundleOf(ARGS_VACANCY to vacancyId)
    }
}
