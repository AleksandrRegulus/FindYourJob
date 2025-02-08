package ru.practicum.android.diploma.ui.about

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.databinding.FragmentAboutBinding
import ru.practicum.android.diploma.ui.fragment.BindingFragment

@AndroidEntryPoint
class AboutFragment : BindingFragment<FragmentAboutBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater, container, false)
    }
}
