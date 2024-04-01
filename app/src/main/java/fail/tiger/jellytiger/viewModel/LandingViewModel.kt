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

class LandingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState


    fun onServerAddressChanged(nowServerAddress: String) {
        _uiState.update { it.copy(serverAddress = nowServerAddress) }
    }

    fun onUserNameChanged(nowUserName: String) {
        _uiState.update { it.copy(userName = nowUserName) }
    }

    fun onPasswordChanged(nowPassword: String) {
        _uiState.update { it.copy(password = nowPassword) }
    }

    fun onConnectClicked(serverAddress: String) {
        _uiState.update { it.copy(connectLoading = true) }
        viewModelScope.launch {
            getPublicSystemInfo(serverAddress, then = { publicSystemInfo ->
                _uiState.update {
                    it.copy(serverCheckState = true, publicSystemInfo = publicSystemInfo)
                }
            })
            _uiState.update { it.copy(connectLoading = false) }
        }
    }

    fun onSignInClicked(serverAddress: String,userName: String,passWord: String) {
        _uiState.update { it.copy(signInLoading = true) }
        viewModelScope.launch {
            loginByUserName(serverAddress, userName, passWord) {
                startActivityEnhanced(MainActivity::class.java)
            }
            _uiState.update { it.copy(signInLoading = false) }
        }
    }
}

data class LandingUiState(
    var serverAddress: String = "",
    var userName: String = "",
    var password: String = "",
    var serverCheckState: Boolean = false,
    var publicSystemInfo: PublicSystemInfo? = null,
    var connectLoading: Boolean = false,
    var signInLoading: Boolean = false
)
