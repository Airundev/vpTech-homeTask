package com.vp.movies

import android.app.Activity
import android.app.Application
import com.vp.movies.di.DaggerAppComponent
import com.vp.persistence.di.RoomModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MoviesApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .roomModule(RoomModule(this))
                .build()
                .inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? = dispatchingActivityInjector
}
