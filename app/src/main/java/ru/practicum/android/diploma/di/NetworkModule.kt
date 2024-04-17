package ru.practicum.android.diploma.di

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.HHApiService
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import javax.inject.Singleton

@Module
class NetworkModule() {

    @Singleton
    @Provides
    fun provideHHApiService(): HHApiService {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.run {
                    proceed(
                        request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer ${BuildConfig.HH_ACCESS_TOKEN}")
                            .addHeader("HH-User-Agent", "Find Your Job/1.0 (goaltenders@yandex.ru)")
                            .build()
                    )
                }
            }
            .build()

        return Retrofit
            .Builder()
            .baseUrl("https://api.hh.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(HHApiService::class.java)
    }

    @Singleton
    @Provides
    fun privideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideNetworkClient(hhApiService: HHApiService, connectivityManager: ConnectivityManager): NetworkClient {
        return RetrofitNetworkClient(hhApiService, connectivityManager)
    }
}
