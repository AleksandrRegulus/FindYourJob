package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Salary

fun getSalaryStringAndSymbol(salary: Salary, context: Context): String {
    return if (salary.from != null && salary.to == null) {
        context.getString(R.string.salary_from, salary.from.toString(), getCurrencySymbol(salary.currency))
    } else if (salary.from == null && salary.to != null) {
        context.getString(R.string.salary_to, salary.to.toString(), getCurrencySymbol(salary.currency))
    } else if (salary.from != null && salary.to != null) {
        context.getString(
            R.string.salary_from_to,
            salary.from.toString(),
            salary.to.toString(),
            getCurrencySymbol(salary.currency)
        )
    } else {
        context.getString(R.string.salary_no_value)
    }
}

private fun getCurrencySymbol(currency: String?): String? {
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
        else -> currency
    }
}
