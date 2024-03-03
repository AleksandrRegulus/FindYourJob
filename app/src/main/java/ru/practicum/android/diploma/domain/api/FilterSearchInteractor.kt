package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Industry

interface FilterSearchInteractor {
    fun getIndustries(): Flow<List<Industry>>
}
