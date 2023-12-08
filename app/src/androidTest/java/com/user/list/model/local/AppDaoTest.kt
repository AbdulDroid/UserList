package com.user.list.model.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.user.list.model.local.dao.AppDao
import com.user.list.util.getUser
import com.user.list.util.getUsers
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDaoTest: DbTest() {

    private lateinit var appDao: AppDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        appDao = db.appDao()

        runBlocking {
            appDao.saveUsers(getUsers(5))
            appDao.saveFullUser(getUser("fairMoney"))
        }
    }

    @Test
    fun testGetUsers() {
        val users = appDao.getUsers()
        assertThat(users.size, equalTo(5))

        assertThat(users[0].userId, `is`("0"))
    }

    @Test
    fun testGetUser() {
        val user = appDao.getUser("fairMoney")
        assertThat(user, equalTo(getUser("fairMoney")))
    }
}