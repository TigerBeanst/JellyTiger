package fail.tiger.jellytiger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import fail.tiger.jellytiger.ui.theme.AppTheme
import fail.tiger.jellytiger.ui.view.LoginAppView
import fail.tiger.jellytiger.viewModel.LoginViewModel

class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                LoginAppView(loginViewModel = viewModel)
            }
        }
    }
}