package ru.practicum.android.diploma.data.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterParameters
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterParamsDataStoreManager @Inject constructor(
    private val filterParamsDataStore: DataStore<FilterParameters>
) {

    val read: Flow<FilterParameters> = filterParamsDataStore.data

    suspend fun save(filterParameters: FilterParameters) {
        filterParamsDataStore.updateData { filterParameters }
    }

}
