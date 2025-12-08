package com.vano.testingplayground

import junit.framework.TestCase.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {

    @Test
    fun string_isReversedCorrectly() {
        val original = "hello"
        val reversed = original.reversed()
        assertEquals("olleh", reversed)
    }

    @Test
    fun list_containsExpectedElement() {
        val list = listOf("apple", "banana", "orange")
        assertTrue(list.contains("banana"))
    }

    @Test
    fun map_returnsCorrectValue() {
        val map = mapOf("key1" to 10, "key2" to 20)
        assertEquals(20, map["key2"])
    }

    @Test
    fun filter_filtersCorrectly() {
        val nums = listOf(1, 2, 3, 4, 5)
        val filtered = nums.filter { it % 2 == 0 }
        assertEquals(listOf(2, 4), filtered)
    }
}
