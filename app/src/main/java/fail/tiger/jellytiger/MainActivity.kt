package fail.tiger.jellytiger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fail.tiger.jellytiger.ui.theme.AppTheme
import fail.tiger.jellytiger.utils.startActivityEnhanced
import getLoginStatus

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras?.containsKey("JustLetMeIn") == false || intent.extras?.getBoolean("JustLetMeIn") == false || !getLoginStatus()) {
            startActivityEnhanced(LoginActivity::class.java)
            finish()
        }
        enableEdgeToEdge()
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("aa")
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
//
//    @Preview(showBackground = true)
//    @Composable
//    fun GreetingPreview() {
//        JellyTigerTheme {
//            Greeting("Android")
//        }
//    }
}

