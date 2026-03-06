// iosMain/kotlin/Main_ios.kt
import androidx.compose.ui.window.ComposeUIViewController
import com.pixelrabbit.backy.backyApp
import com.pixelrabbit.backy.ads.AppOpenAdManager
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    val appOpenAdManager = AppOpenAdManager()

    return ComposeUIViewController {
        backyApp(appOpenAdManager = appOpenAdManager)
    }
}