// iosMain/kotlin/Main_ios.kt
import androidx.compose.ui.window.ComposeUIViewController
import com.pixelrabbit.oculi.OculiApp
import com.pixelrabbit.oculi.ads.AppOpenAdManager
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    val appOpenAdManager = AppOpenAdManager()

    return ComposeUIViewController {
        OculiApp(appOpenAdManager = appOpenAdManager)
    }
}