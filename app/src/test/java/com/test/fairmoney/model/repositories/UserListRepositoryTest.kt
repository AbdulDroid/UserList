package com.test.fairmoney.model.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.fairmoney.model.NetworkState
import com.test.fairmoney.model.local.AppDatabase
import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.models.UserResponse
import com.test.fairmoney.model.remote.ApiService
import com.test.fairmoney.utils.getUsers
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import retrofit2.Response

@RunWith(JUnit4::class)
class UserListRepositoryTest {

    private lateinit var userListRepository: UserListRepository
    private val dao = mock(AppDao::class.java)
    private val apiService = mock(ApiService::class.java)
    private val network = mock(NetworkState::class.java)

    @Rule
    @JvmField
    val instantRuleExecutor = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = mock(AppDatabase::class.java)
        `when`(db.appDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        userListRepository = UserListRepository(apiService, dao, network)
    }

    @Test
    fun getLocalUsers() {
        runBlocking {
            `when`(network.hasInternet()).thenReturn(false)
            userListRepository.getUsers()
            verify(dao, atLeastOnce()).getUsers()
            verify(apiService, never()).getUsers()
            verifyZeroInteractions(dao)
            verifyZeroInteractions(apiService)
        }
    }

    @Test
    fun `remote call returns successfully with no data`() {
        runBlocking {
            `when`(apiService.getUsers()).thenReturn(Response.success(UserResponse()))
            `when`(network.hasInternet()).thenReturn(true)
            userListRepository.getUsers()
            verify(apiService, atLeastOnce()).getUsers()
            verify(dao, atMost(1)).getUsers()
            verify(dao, never()).saveUsers(emptyList())
            verifyZeroInteractions(dao)
        }
    }

    @Test
    fun `remote call returns successfully with data`() {
        runBlocking {
            `when`(apiService.getUsers()).thenReturn(
                Response.success(
                    UserResponse(
                        data = getUsers(5),
                        total = 10,
                        limit = 5
                    )
                )
            )
            `when`(network.hasInternet()).thenReturn(true)
            val resp = userListRepository.getUsers()
            assertThat(resp.data!!.size, `is`(5))
            assertNull(resp.errorMessage)
            verify(apiService, atLeastOnce()).getUsers()
            verify(dao, atLeastOnce()).getUsers()
            verify(dao, atLeastOnce()).saveUsers(getUsers(5))
            verifyZeroInteractions(dao)
        }
    }

    @Test
    fun `remote call fails`() {
        runBlocking {
            `when`(apiService.getUsers()).thenReturn(
                Response.error(
                    404, ResponseBody.create(
                        MediaType.get("text/plain"), "Invalid App ID"
                    )
                )
            )
            `when`(network.hasInternet()).thenReturn(true)
            val resp = userListRepository.getUsers()
            assertNull(resp.data)
            assertThat(resp.errorMessage, `is`("Invalid App ID"))
            verify(dao, atLeastOnce()).getUsers()
            verify(apiService, atLeastOnce()).getUsers()
            verify(dao, never()).saveUsers(emptyList())
            verifyZeroInteractions(dao)
            verifyZeroInteractions(apiService)
        }
    }
}