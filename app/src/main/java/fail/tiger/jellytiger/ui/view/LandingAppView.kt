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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import fail.tiger.jellytiger.R
import fail.tiger.jellytiger.viewModel.LandingUiState
import fail.tiger.jellytiger.viewModel.LandingViewModel
import org.jellyfin.sdk.Jellyfin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingAppView(
    landingViewModel: LandingViewModel,
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
        LandingAppContent(
            landingViewModel = landingViewModel
        )
    }
}

@Composable
fun LandingAppContent(
    landingViewModel: LandingViewModel,
    modifier: Modifier = Modifier,
) {
    val landingUiState by landingViewModel.uiState.collectAsState()
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
            if (!landingUiState.serverCheckState) {
                ServerAddressView(landingViewModel, landingUiState, modifier.align(Alignment.End))
            } else {
                UserInfoView(landingViewModel, landingUiState, modifier.align(Alignment.End))
            }

        }
        if (!landingUiState.serverCheckState) {
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
                    R.string.activity_landing_tips,
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
    landingViewModel: LandingViewModel,
    landingUiState: LandingUiState,
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
        value = landingUiState.serverAddress,
        onValueChange = { landingViewModel.onServerAddressChanged(it) },
        placeholder = { Text(stringResource(id = R.string.activity_landing_server_address_placeholder)) },
        label = { Text(stringResource(id = R.string.activity_landing_server_address)) },
        modifier = Modifier.fillMaxWidth()
    )
    // Button: Connect Server
    Button(
        onClick = { landingViewModel.onConnectClicked(landingUiState.serverAddress) },
        modifier = modifier.padding(top = 16.dp),
        enabled = !landingUiState.loading
    ) {
        if (landingUiState.loading) {
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
        Text(stringResource(id = R.string.activity_landing_server_connect))
    }
}

@Composable
fun UserInfoView(
    landingViewModel: LandingViewModel,
    landingUiState: LandingUiState,
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
                    text = landingUiState.publicSystemInfo?.serverName
                        ?: stringResource(R.string.error_data),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = landingUiState.publicSystemInfo?.version
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
                        append(stringResource(R.string.activity_landing_login_operating_system))
                    }
                    append(
                        " " + (landingUiState.publicSystemInfo?.operatingSystem
                            ?: stringResource(R.string.error_data))
                    )
                }
            )
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                        append(stringResource(R.string.activity_landing_login_product_name))
                    }
                    append(
                        " " + (landingUiState.publicSystemInfo?.productName
                            ?: stringResource(R.string.error_data))
                    )
                }
            )
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                        append(stringResource(R.string.activity_landing_login_local_address))
                    }
                    append(
                        " " + (landingUiState.publicSystemInfo?.localAddress
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
        value = landingUiState.userName,
        onValueChange = { landingViewModel.onUserNameChanged(it) },
        placeholder = { Text(stringResource(id = R.string.activity_landing_login_username)) },
        label = { Text(stringResource(id = R.string.activity_landing_login_username)) },
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
        value = landingUiState.password,
        onValueChange = { landingViewModel.onPasswordChanged(it) },
        placeholder = { Text(stringResource(id = R.string.activity_landing_login_password)) },
        label = { Text(stringResource(id = R.string.activity_landing_login_password)) },
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        visualTransformation = PasswordVisualTransformation()
    )
    // Button: Sign In
    Button(
        onClick = { landingViewModel.onConnectClicked(landingUiState.serverAddress) },
        modifier = modifier.padding(top = 16.dp),
        enabled = !landingUiState.loading
    ) {
        if (landingUiState.loading) {
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
        Text(stringResource(id = R.string.activity_landing_login_sign_in))
    }
    // Button: Quick Connect
    OutlinedButton(
        onClick = { landingViewModel.onConnectClicked(landingUiState.serverAddress) },
        modifier = modifier,
        enabled = !landingUiState.loading
    ) {
        if (landingUiState.loading) {
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
        Text(stringResource(id = R.string.activity_landing_login_quick_connect))
    }
}

@Preview(showBackground = true)
@Composable
fun LandingAppPreview() {
    LandingAppView(
        landingViewModel = LandingViewModel()
    )
}