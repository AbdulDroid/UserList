package com.test.fairmoney.model.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class ApiServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer
    private val userId = "0F8JIqi4zwvb77FGz6Wt"

    @Before
    fun setupService() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun makeGetUsersRequest() {
        runBlocking {
            enqueueResponse("users.json")
            val result = apiService.getUsers().body()
            val request = mockWebServer.takeRequest()
            assertNotNull(result)
            assertThat(request.path, `is`("/user?limit=100"))
        }
    }

    @Test
    fun getUsersResponse() {
        runBlocking {
            enqueueResponse("users.json")
            val result = apiService.getUsers().body()
            val users = result!!.data
            assertNotNull(result)
            assertThat(result.limit, `is`(7))
            assertThat(users.size, `is`(7))
        }
    }

    @Test
    fun makeGetUserRequest() {
        runBlocking {
            enqueueResponse("user.json")
            val result = apiService.getUser(userId).body()
            val request = mockWebServer.takeRequest()
            assertNotNull(result)
            assertThat(request.path, `is`("/user/${userId}"))
        }
    }

    @Test
    fun getUserResponse() {
        runBlocking {
            enqueueResponse("user.json")
            val result = apiService.getUser(userId).body()
            assertThat(result!!.userId, `is`(userId))
            assertThat(result.title, `is`("mr"))
        }
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
            .getResourceAsStream("remote-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(mockResponse.setBody(
            source.readString(Charsets.UTF_8))
        )
    }
}