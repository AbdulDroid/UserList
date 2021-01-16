package com.test.fairmoney.utils

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExtensionTest {

    @Test
    fun `test that formatted date is returned for valid input`() {
        val formatted = "2020-03-07T00:42:32.221Z".formatDate()
        assertThat(formatted, equalTo("Mar 07 2020"))
    }

    @Test
    fun `test that date parsing uses the null block to return empty string for wrong input`() {
        val formatted = "2020-03-07 00:42:32.221Z".formatDate()
        assertThat(formatted, equalTo(""))
    }

    @Test
    fun `test empty string is returned for incorrect format input`() {
        val formatted = "".formatDate()
        assertThat(formatted, equalTo(""))
    }
}