package fail.tiger.jellytiger.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fail.tiger.jellytiger.MainActivity
import fail.tiger.jellytiger.utils.jellyfin.getPublicSystemInfo
import fail.tiger.jellytiger.utils.jellyfin.loginByUserName
import fail.tiger.jellytiger.utils.startActivityEnhanced
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jellyfin.sdk.model.api.PublicSystemInfo

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState


    fun onServerAddressChanged(nowServerAddress: String) {
        _uiState.update { it.copy(serverAddress = nowServerAddress) }
    }

    fun onUserNameChanged(nowUserName: String) {
        _uiState.update { it.copy(userName = nowUserName) }
    }

    fun onPasswordChanged(nowPassword: String) {
        _uiState.update { it.copy(password = nowPassword) }
    }

    fun onConnectClicked() {
        _uiState.update { it.copy(connectLoading = true) }
        viewModelScope.launch {
            getPublicSystemInfo(uiState.value.serverAddress, then = { publicSystemInfo ->
                _uiState.update {
                    it.copy(serverCheckState = true, publicSystemInfo = publicSystemInfo)
                }
            })
            _uiState.update { it.copy(connectLoading = false) }
        }
    }

    fun onSignInClicked() {
        _uiState.update { it.copy(signInLoading = true) }
        viewModelScope.launch {
            loginByUserName(
                uiState.value.serverAddress,
                uiState.value.userName,
                uiState.value.password
            ) {
                startActivityEnhanced(MainActivity::class.java, Pair("JustLetMeIn", true))
            }
            _uiState.update { it.copy(signInLoading = false) }
        }
    }
}

data class LoginUiState(
    var serverAddress: String = "",
    var userName: String = "",
    var password: String = "",
    var serverCheckState: Boolean = false,
    var publicSystemInfo: PublicSystemInfo? = null,
    var connectLoading: Boolean = false,
    var signInLoading: Boolean = false
)
