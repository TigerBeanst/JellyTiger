package fail.tiger.jellytiger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import fail.tiger.jellytiger.ui.theme.AppTheme
import fail.tiger.jellytiger.ui.view.LandingAppView
import fail.tiger.jellytiger.viewModel.LandingViewModel

class LandingActivity : ComponentActivity() {
    private val viewModel: LandingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                LandingAppView(landingViewModel = viewModel)
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Composable
//private fun AppIcon(modifier: Modifier = Modifier) {
//    Image(
//        painter = painterResource(id = R.drawable.ic_launcher_foreground),
//        contentDescription = null,
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    JellyTigerTheme {
//        ConstraintLayout(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//        ) {
//            val (appIconImage, appTitleText) = createRefs()
//            AppIcon(modifier = Modifier
//                .size(128.dp)
//                .constrainAs(appIconImage) {
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                })
//            Greeting("Andrssoid",
//                modifier = Modifier
//                    .constrainAs(appTitleText) {
//                        top.linkTo(appIconImage.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    })
//
//        }
//    }
//}