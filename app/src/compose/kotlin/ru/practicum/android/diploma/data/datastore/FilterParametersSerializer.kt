package ru.practicum.android.diploma.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import ru.practicum.android.diploma.datastore.FilterParametersProto
import ru.practicum.android.diploma.domain.models.FilterParameters
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class FilterParametersSerializer @Inject constructor() : Serializer<FilterParameters> {
    override val defaultValue: FilterParameters = FilterParameters.initial

    override suspend fun readFrom(input: InputStream): FilterParameters {
        try {
            val protoFilterParams = FilterParametersProto.parseFrom(input)
            return FilterParameters(
                idIndustry = protoFilterParams.idIndustry,
                nameIndustry = protoFilterParams.nameIndustry,
                idCountry = protoFilterParams.idCountry,
                nameCountry = protoFilterParams.nameCountry,
                idRegion = protoFilterParams.idRegion,
                nameRegion = protoFilterParams.nameRegion,
                salary = protoFilterParams.salary,
                doNotShowWithoutSalary = protoFilterParams.doNotShowWithoutSalary
            )
        } catch (exception: Exception) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: FilterParameters, output: OutputStream) {
        FilterParametersProto.newBuilder()
            .setIdIndustry(t.idIndustry)
            .setNameIndustry(t.nameIndustry)
            .setIdCountry(t.idCountry)
            .setNameCountry(t.nameCountry)
            .setIdRegion(t.idRegion)
            .setNameRegion(t.nameRegion)
            .setSalary(t.salary)
            .setDoNotShowWithoutSalary(t.doNotShowWithoutSalary)
            .build()
            .writeTo(output)
    }
}

