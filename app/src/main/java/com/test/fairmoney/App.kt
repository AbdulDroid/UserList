package com.test.fairmoney

import android.app.Application
import android.content.Context
import com.test.fairmoney.di.AppComponent
import com.test.fairmoney.di.DaggerAppComponent

class App: Application() {

    private val appComponent: AppComponent by lazy{
        DaggerAppComponent.builder().application(this)!!.create()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    companion object {
        @JvmStatic
        fun appComponent(context: Context) =
            (context.applicationContext as App).appComponent
    }
}