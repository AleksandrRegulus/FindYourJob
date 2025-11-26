package ru.practicum.android.diploma.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Salary

@Composable
fun getSalaryStringAndSymbol(salary: Salary): String {

    val currency = getCurrencySymbol(salary.currency)

    return if (salary.from != null && salary.to == null) {
        stringResource(R.string.salary_from, salary.from.toString(), currency)
    } else if (salary.from == null && salary.to != null) {
        stringResource(R.string.salary_to, salary.to.toString(), currency)
    } else if (salary.from != null && salary.to != null) {
        stringResource(R.string.salary_from_to, salary.from.toString(), salary.to.toString(), currency)
    } else {
        stringResource(R.string.salary_no_value)
    }
}

private fun getCurrencySymbol(currency: String?): String {
    return when (currency) {
        "AZN" -> "₼"
        "BYR" -> "Br"
        "EUR" -> "€"
        "GEL" -> "₾"
        "KGS" -> "с"
        "KZT" -> "₸"
        "RUR" -> "₽"
        "UAH" -> "₴"
        "USD" -> "$"
        "UZS" -> "soʻm"
        else -> currency ?: ""
    }
}
