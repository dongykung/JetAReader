package com.example.jetareader.screen.login

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetareader.R
import com.example.jetareader.components.PasswordInput
import com.example.jetareader.components.ReaderLogo
import com.example.jetareader.components.emailInput
import com.example.jetareader.navigation.ReaderScreens


@Composable
fun LoginScreen(navController: NavController,
                viewModel: LoginScreenViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    val showLoginFrom = rememberSaveable { mutableStateOf(true) }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ReaderLogo()
            if (showLoginFrom.value) UserForm(
                loading = false,
                isCreateAcount = false
            ) { email, password ->
                //TODO login fb user
                viewModel.signInWithEmailAndPassword(email,password){
                    navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                }
            }
            else {
                UserForm(loading = false, isCreateAcount = true) { email, password ->
                    //TODO create fb user
                    viewModel.createUserWithEmailAndPassword(email,password){
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val text = if (showLoginFrom.value) "Sigh up" else "Login"
            Text(text = "New User?")
            Text(text = text, modifier = Modifier
                .clickable {
                    showLoginFrom.value = !showLoginFrom.value
                }
                .padding(start = 5.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAcount: Boolean = false,
    onDone: (String, String) -> Unit = { email, password -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .wrapContentHeight()
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(targetState = isCreateAcount, label = "") {
            when (it) {
                true -> {
                    Text(
                        text = stringResource(id = R.string.create_acct),
                        modifier = Modifier.padding(4.dp)
                    )
                }
                false -> {
                    Text(text = "")
                }
            }
        }

        emailInput(emailState = email, enabled = !loading, onAction = KeyboardActions {
            passwordFocusRequest.requestFocus()
        })
        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
            }
        )

        SubmitButton(
            textId = if (isCreateAcount) "CreateAcount" else "Login",
            loading = loading,
            validInputs = valid,
            onClick = {
                onDone(email.value.trim(), password.value.trim())
                keyboardController?.hide()
            }
        )


    }
}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}




