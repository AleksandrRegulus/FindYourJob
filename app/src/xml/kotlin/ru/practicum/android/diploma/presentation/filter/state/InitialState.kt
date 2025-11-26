package ru.practicum.android.diploma.presentation.filter.state

data class InitialState(
    val isIndustryChosen: Boolean = false,
    val isAreaChosen: Boolean = false,
    val isSalaryChosen: Boolean = false,
    val doNotShowWithoutSalary: Boolean = false
)
