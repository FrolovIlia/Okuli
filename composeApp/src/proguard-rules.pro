#############################################
## Compose – prevent broken Modifier keys
#############################################
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

-keepclassmembers class * implements androidx.compose.ui.Modifier$Element { *; }
-keepclassmembers class * extends androidx.compose.ui.Modifier$Node { *; }

#############################################
## Voyager transitions – avoid R8 merging keys
#############################################
-keep class cafe.adriel.voyager.transitions.** { *; }
-dontwarn cafe.adriel.voyager.transitions.**

#############################################
## Kotlin reflection / coroutines
#############################################
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

#############################################
## Realm Kotlin
#############################################
-keep class io.realm.** { *; }
-dontwarn io.realm.**

#############################################
## Serialization
#############################################
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

#############################################
## Prevent removing @Composable methods
#############################################
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
}

#############################################
## Prevent removal of your app's classes
#############################################
-keep class com.pixelrabbit.oculi.** { *; }
