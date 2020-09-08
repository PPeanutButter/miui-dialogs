package com.peanut.sdk.miuidialog

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

    }

    @Test
    fun connect_device(){
        Runtime.getRuntime().exec("adb connect 192.168.0.103:5555")
    }

    @Test
    fun disconnect_device(){
        Runtime.getRuntime().exec("adb disconnect")
    }
}