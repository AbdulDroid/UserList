package com.test.fairmoney.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.fairmoney.viewmodel.UserDetailViewModel
import com.test.fairmoney.viewmodel.UserListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel::class)
    abstract fun bindUserListViewModel(viewModel: UserListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailViewModel::class)
    abstract fun bindUserDetailViewModel(viewModel: UserDetailViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory


}