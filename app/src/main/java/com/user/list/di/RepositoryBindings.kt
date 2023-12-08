package com.user.list.di

import com.user.list.model.repositories.UserDetailRepository
import com.user.list.model.repositories.UserDetailRepositoryImpl
import com.user.list.model.repositories.UserListRepository
import com.user.list.model.repositories.UserListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindings {
    @[Binds Singleton]
    fun bindUserListRepository(impl: UserListRepositoryImpl): UserListRepository

    @[Binds Singleton]
    fun bindsUserDetailRepository(impl: UserDetailRepositoryImpl): UserDetailRepository
}