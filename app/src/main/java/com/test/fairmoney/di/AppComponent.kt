package com.test.fairmoney.di

import com.test.fairmoney.App
import com.test.fairmoney.view.MainActivity
import com.test.fairmoney.view.fragments.UserDetailFragment
import com.test.fairmoney.view.fragments.UserListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder?
        fun create(): AppComponent
    }

    fun inject(app: App)
    fun inject(mainActivity: MainActivity)
    fun inject(userListFragment: UserListFragment)
    fun inject(userDetailFragment: UserDetailFragment)
}