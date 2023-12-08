package com.user.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.user.list.model.local.entities.FullUser
import com.user.list.model.models.Result
import com.user.list.model.repositories.UserDetailRepository
import com.user.list.utils.getUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
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
class UserDetailViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var loadingObserver: Observer<Boolean>

    @Mock
    lateinit var errorObserver: Observer<String>

    @Mock
    lateinit var dataObserver: Observer<FullUser>

    @Mock
    lateinit var repository: UserDetailRepository

    private lateinit var viewModel: UserDetailViewModel
    private val threadContext = newSingleThreadContext("UI thread")

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(threadContext)
        viewModel = UserDetailViewModel(repository)
    }

    @Test
    fun `viewModel gets data successfully via repository`() {
        runBlocking {
            `when`(repository.getUser("")).thenReturn(Result(getUser("")))

            viewModel.loading.observeForever(loadingObserver)
            viewModel.error.observeForever(errorObserver)
            viewModel.user.observeForever(dataObserver)

            viewModel.getUser("")

            Thread.sleep(100)

            verify(loadingObserver).onChanged(true)
            verify(dataObserver).onChanged(FullUser(""))
            verifyZeroInteractions(errorObserver)
            verify(loadingObserver).onChanged(false)
        }
    }

    @Test
    fun `viewModel gets error from repository`() {
        runBlocking {
            `when`(repository.getUser("")).thenReturn(
                Result(
                    data = null,
                    errorMessage = "An Error Occurred"
                )
            )
            viewModel.loading.observeForever(loadingObserver)
            viewModel.error.observeForever(errorObserver)
            viewModel.user.observeForever(dataObserver)

            viewModel.getUser("")

            Thread.sleep(100)
            verify(loadingObserver).onChanged(true)
            verify(errorObserver).onChanged("An Error Occurred")
            verifyZeroInteractions(dataObserver)
            verify(loadingObserver).onChanged(false)
        }
    }
}