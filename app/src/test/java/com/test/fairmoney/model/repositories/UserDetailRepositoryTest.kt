package com.test.fairmoney.model.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.fairmoney.model.NetworkState
import com.test.fairmoney.model.local.AppDatabase
import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.remote.ApiService
import com.test.fairmoney.utils.getUser
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import retrofit2.Response

@RunWith(JUnit4::class)
class UserDetailRepositoryTest {
    private lateinit var userDetailRepository: UserDetailRepository

    private val dao = mock(AppDao::class.java)
    private val apiService = mock(ApiService::class.java)
    private val network = mock(NetworkState::class.java)
    private val userId = "0F8JIqi4zwvb77FGz6Wt"

    @Rule
    @JvmField
    val instantRuleExecutor = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = mock(AppDatabase::class.java)
        `when`(db.appDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        userDetailRepository = UserDetailRepository(apiService, dao, network)
    }

    @Test
    fun getLocalUser() {
        runBlocking {
            `when`(network.hasInternet()).thenReturn(false)
            userDetailRepository.getUser("")
            verify(dao, atLeastOnce()).getUser("")
            verify(apiService, never()).getUser("")
            verifyZeroInteractions(apiService)
            verifyZeroInteractions(dao)
        }
    }

    @Test
    fun `remote call completes successfully`() {
        runBlocking {
            `when`(apiService.getUser(userId)).thenReturn(Response.success(getUser(userId)))
            `when`(network.hasInternet()).thenReturn(true)
            val resp = userDetailRepository.getUser( userId)
            assertNull(resp.errorMessage)
            assertNotNull(resp.data)
            assertThat(resp.data!!.userId, `is`(userId))
            verify(dao, atLeastOnce()).getUser(userId)
            verify(apiService, atLeastOnce()).getUser(userId)
            verify(dao, atLeastOnce()).saveFullUser(FullUser(userId))
            verifyZeroInteractions(dao)
            verifyZeroInteractions(apiService)
        }
    }

    @Test
    fun `remote call fails`() {
        runBlocking {
            `when`(apiService.getUser(userId)).thenReturn(Response.error(404, ResponseBody.create(
                MediaType.get("text/plain"), "No User found with such id")))
            `when`(network.hasInternet()).thenReturn(true)
            val resp = userDetailRepository.getUser(userId)
            assertNull(resp.data)
            assertNotNull(resp.errorMessage)
            assertThat(resp.errorMessage, `is`("No User found with such id"))
            verify(dao, atLeastOnce()).getUser(userId)
            verify(apiService, atLeastOnce()).getUser(userId)
            verify(dao, never()).saveFullUser(FullUser(userId))
            verifyZeroInteractions(dao)
            verifyZeroInteractions(apiService)
        }
    }
}