package ru.practicum.android.diploma.ui

import android.app.Application
import android.content.Context
import ru.practicum.android.diploma.di.AppComponent
import ru.practicum.android.diploma.di.DaggerAppComponent
import ru.practicum.android.diploma.di.DataModule

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .dataModule(DataModule(context = this))
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

