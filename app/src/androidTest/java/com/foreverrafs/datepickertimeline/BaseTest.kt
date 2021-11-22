package com.foreverrafs.datepickertimeline

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

internal open class BaseTest {
    @get:Rule
    val rule = createComposeRule()

    fun setContent(content: @Composable () -> Unit) {
        rule.setContent(content)
    }
}
