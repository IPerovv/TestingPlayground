package com.vano.testingplayground.loginScreen

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vano.testingplayground.LoginScreen

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@get:Rule
val composeTestRule = createComposeRule()

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysAllUIElements() {
        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule.onNodeWithText("Store Me").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign Up").assertIsDisplayed()
    }

    @Test
    fun loginScreen_inputFields_acceptUserInput() {
        composeTestRule.setContent {
            LoginScreen()
        }

        val testEmail = "user@example.com"

        composeTestRule
            .onAllNodes(hasText("Email"))[0]
            .performTextInput(testEmail)

        composeTestRule
            .onNode(hasText(testEmail))
            .assertExists()
    }

    @Test
    fun loginScreen_passwordIsHiddenByDefault() {
        val testPassword = "secret123"

        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule.onNode(
            hasSetTextAction() and hasText("Password", substring = true)
        ).performTextInput(testPassword)

        composeTestRule
            .onAllNodes(hasText(testPassword), useUnmergedTree = false)
            .assertCountEquals(0)
    }



    @Test
    fun loginScreen_loginButton_triggersCallback() {
        var loginClicked = false

        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { loginClicked = true }
            )
        }

        composeTestRule
            .onNodeWithText("Log In")
            .performClick()

        assert(loginClicked)
    }
}



