package fail.tiger.jellytiger.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Person4
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import fail.tiger.jellytiger.R
import fail.tiger.jellytiger.viewModel.LoginUiState
import fail.tiger.jellytiger.viewModel.LoginViewModel
import org.jellyfin.sdk.Jellyfin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAppView(
    loginViewModel: LoginViewModel,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

            )
        },
    ) { _ ->
        LoginAppContent(
            loginViewModel = loginViewModel
        )
    }
}

@Composable
fun LoginAppContent(
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier,
) {
    val loginUiState by loginViewModel.uiState.collectAsState()
    ConstraintLayout(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        val (column, tips) = createRefs()
        Column(
            modifier = modifier.constrainAs(column) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!loginUiState.serverCheckState) {
                ServerAddressView(loginViewModel, loginUiState, modifier.align(Alignment.End))
            } else {
                UserInfoView(loginViewModel, loginUiState, modifier.align(Alignment.End))
            }

        }
        if (!loginUiState.serverCheckState) {
            Text(
                modifier = modifier
                    .alpha(0.5F)
                    .padding(start = 16.dp, end = 16.dp)
                    .constrainAs(tips) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(
                    R.string.activity_login_tips,
                    Jellyfin.apiVersion.toString(),
                    Jellyfin.minimumVersion.toString(),
                ),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ServerAddressView(
    loginViewModel: LoginViewModel,
    loginUiState: LoginUiState,
    modifier: Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = null,
        modifier = Modifier.size(128.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    // TextField: Server Address
    OutlinedTextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Dns,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {loginViewModel.onConnectClicked(loginUiState.serverAddress)}
        ),
        value = loginUiState.serverAddress,
        onValueChange = { loginViewModel.onServerAddressChanged(it) },
        placeholder = { Text(stringResource(id = R.string.activity_login_server_address_placeholder)) },
        label = { Text(stringResource(id = R.string.activity_login_server_address)) },
        modifier = Modifier.fillMaxWidth()
    )
    // Button: Connect Server
    Button(
        onClick = { loginViewModel.onConnectClicked(loginUiState.serverAddress) },
        modifier = modifier.padding(top = 16.dp),
        enabled = !loginUiState.connectLoading
    ) {
        if (loginUiState.connectLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.Link,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp)
            )
        }
        Text(stringResource(id = R.string.activity_login_server_connect))
    }
}

@Composable
fun UserInfoView(
    loginViewModel: LoginViewModel,
    loginUiState: LoginUiState,
    modifier: Modifier

) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = loginUiState.publicSystemInfo?.serverName
                        ?: stringResource(R.string.error_data),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = loginUiState.publicSystemInfo?.version
                        ?: stringResource(R.string.error_data),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .alpha(0.5F)
                        .padding(start = 4.dp)
                )
            }
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                        append(stringResource(R.string.activity_login_login_operating_system))
                    }
                    append(
                        " " + (loginUiState.publicSystemInfo?.operatingSystem
                            ?: stringResource(R.string.error_data))
                    )
                }
            )
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                        append(stringResource(R.string.activity_login_login_product_name))
                    }
                    append(
                        " " + (loginUiState.publicSystemInfo?.productName
                            ?: stringResource(R.string.error_data))
                    )
                }
            )
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                        append(stringResource(R.string.activity_login_login_local_address))
                    }
                    append(
                        " " + (loginUiState.publicSystemInfo?.localAddress
                            ?: stringResource(R.string.error_data))
                    )
                }
            )
        }
    }
    // TextField: Username
    OutlinedTextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Person4,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        value = loginUiState.userName,
        onValueChange = { loginViewModel.onUserNameChanged(it) },
        placeholder = { Text(stringResource(id = R.string.activity_login_input_username)) },
        label = { Text(stringResource(id = R.string.activity_login_input_username)) },
        modifier = Modifier.fillMaxWidth()
    )
    // TextField: Password
    OutlinedTextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Password,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {loginViewModel.onSignInClicked(loginUiState.serverAddress, loginUiState.userName, loginUiState.password)}
        ),
        value = loginUiState.password,
        onValueChange = { loginViewModel.onPasswordChanged(it) },
        placeholder = { Text(stringResource(id = R.string.activity_login_input_password)) },
        label = { Text(stringResource(id = R.string.activity_login_input_password)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        visualTransformation = PasswordVisualTransformation()
    )
    // Button: Sign In
    Button(
        onClick = { loginViewModel.onSignInClicked(loginUiState.serverAddress, loginUiState.userName, loginUiState.password) },
        modifier = modifier.padding(top = 16.dp),
        enabled = !loginUiState.signInLoading
    ) {
        if (loginUiState.signInLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Login,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp)
            )
        }
        Text(stringResource(id = R.string.activity_login_sign_in))
    }
    // Button: Quick Connect
    OutlinedButton(
        onClick = { loginViewModel.onConnectClicked(loginUiState.serverAddress) },
        modifier = modifier,
        enabled = !loginUiState.signInLoading
    ) {
        if (loginUiState.connectLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.Sensors,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp)
            )
        }
        Text(stringResource(id = R.string.activity_login_quick_connect))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginAppPreview() {
    LoginAppView(
        loginViewModel = LoginViewModel()
    )
}