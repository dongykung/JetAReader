package com.example.jetareader.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetareader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        text = "A.Reader",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.headlineLarge,
        color = Color.Red.copy(0.5f)
    )
}

@Composable
fun emailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(valueState = emailState, labelId = labelId, enabled = enabled, modifier = modifier,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        keyboardActions = onAction)
}


@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value, onValueChange = {
            valueState.value = it
        },
        label = { Text(text = labelId) },
        enabled = enabled,
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier= modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType=keyboardType, imeAction = imeAction),
        keyboardActions = keyboardActions
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier=Modifier,
    passwordState: MutableState<String>,
    labelId:String,
    passwordVisibility: MutableState<Boolean>,
    onAction: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction= ImeAction.Done
) {
    val visualTransformation = if(passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value, onValueChange = {
        passwordState.value=it
    },
        label = { Text(text = labelId)},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier= modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation =visualTransformation,
        trailingIcon = {
            PasswordVisibility(passwordVisibility = passwordVisibility)
        },
        keyboardActions = onAction
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>){
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value=!visible }) {
        Icon(imageVector = Icons.Default.RemoveRedEye, contentDescription = "")
    }
}

@Composable
fun TitleSection(modifier: Modifier=Modifier,
                 label:String){
    Surface(modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderAppBar(
    title: String,
    showProfile: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navController: NavController,
    onBack:Boolean=false,
    showLogOut:Boolean=true,
    onBackArrowClicked : () -> Unit={},
) {
    CenterAlignedTopAppBar(
        title = {
            Crossfade(targetState = showProfile, label = "") {
                when (it) {
                    true -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "LogoIcon",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .scale(0.9f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = title,
                                color = Color.Red.copy(alpha = 0.7f),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                        }
                    }
                    false -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = title,
                                color = Color.Red.copy(alpha = 0.7f),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                        }
                    }
                }
            }

        }, actions = {
            if (showLogOut) {
                IconButton(onClick = {
                    FirebaseAuth.getInstance().signOut().run {
                        navController.navigate(ReaderScreens.LoginScreen.name)
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.Green.copy(alpha = 0.4f)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if(onBack){
                IconButton(onClick = { onBackArrowClicked()}) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription ="Arrow Back")
                }
            }
        }
    )
}

@Composable
fun FABContent(onTab: (String) -> Unit) {
    FloatingActionButton(
        onClick = { onTab("") }, shape = RoundedCornerShape(50.dp),
        containerColor = Color(0xFF92CBDF)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a Book",
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun BookRating(score: Double = 4.5) {
    Surface(
        shape = RoundedCornerShape(56.dp),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.StarBorder, contentDescription = "Star",
                modifier = Modifier.padding(3.dp)
            )
            Text(
                text = score.toString(), style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {},
) {
    Surface(Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius
    )),
        color = Color(0xFF92CBDF)
    ) {
        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label,
                style = TextStyle(color = Color.White, fontSize = 15.sp)
            )
        }
    }
}
