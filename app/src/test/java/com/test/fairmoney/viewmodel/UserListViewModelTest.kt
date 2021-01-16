package com.test.fairmoney.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.test.fairmoney.model.local.entities.User
import com.test.fairmoney.model.models.Result
import com.test.fairmoney.model.repositories.UserListRepository
import com.test.fairmoney.utils.getUsers
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class UserListViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var loadingObserver: Observer<Boolean>

    @Mock
    lateinit var errorObserver: Observer<String>

    @Mock
    lateinit var dataObserver: Observer<List<User>>

    @Mock
    lateinit var repository: UserListRepository

    private lateinit var viewModel: UserListViewModel
    @ObsoleteCoroutinesApi
    private val threadContext = newSingleThreadContext("UI thread")

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(threadContext)
        viewModel = UserListViewModel(repository)
    }


    @Test
    fun `viewModel successfully fetches user list from repository`() {
        runBlocking {
            `when`(repository.getUsers()).thenReturn(Result(getUsers(5)))

            viewModel.loading.observeForever(loadingObserver)
            viewModel.error.observeForever(errorObserver)
            viewModel.users.observeForever(dataObserver)

            viewModel.getUsers()

            Thread.sleep(100)

            verify(loadingObserver).onChanged(true)
            verify(dataObserver).onChanged(getUsers(5))
            verifyZeroInteractions(errorObserver)
            verify(loadingObserver).onChanged(false)
        }
    }

    @Test
    fun `viewModel encounters an error while fetching user list from repository`() {
        runBlocking {
            `when`(repository.getUsers()).thenReturn(Result(null, "An Error Occurred"))

            viewModel.loading.observeForever(loadingObserver)
            viewModel.error.observeForever(errorObserver)
            viewModel.users.observeForever(dataObserver)

            viewModel.getUsers()

            Thread.sleep(100)

            verify(loadingObserver).onChanged(true)
            verify(errorObserver).onChanged("An Error Occurred")
            verifyZeroInteractions(dataObserver)
            verify(loadingObserver).onChanged(false)
        }
    }
}