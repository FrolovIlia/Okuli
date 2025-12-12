#############################################
## Compose – минимальные правила
#############################################
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

-keepclassmembers class * implements androidx.compose.ui.Modifier$Element { *; }
-keepclassmembers class * extends androidx.compose.ui.Modifier$Node { *; }

#############################################
## Voyager transitions – оставить классы
#############################################
-keep class cafe.adriel.voyager.transitions.** { *; }
-dontwarn cafe.adriel.voyager.transitions.**

#############################################
## KotlinX / coroutines / serialization / Realm
#############################################
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

-keep class io.realm.** { *; }
-dontwarn io.realm.**

#############################################
## WorkManager (НОВОЕ)
#############################################
-keep class androidx.work.** { *; }
-keepclassmembers class androidx.work.** { *; }
-dontwarn androidx.work.**

-keep class com.pixelrabbit.oculi.**.workers.** { *; }
-keepclassmembers class com.pixelrabbit.oculi.**.workers.** {
    <methods>;
}

#############################################
## Сохранить ваши @Composable методы
#############################################
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
}

#############################################
## Сохранить ваши app-классы (на время)
#############################################
-keep class com.pixelrabbit.oculi.** { *; }

#############################################
## Яндекс Реклама
#############################################
-keep class com.yandex.mobile.ads.** { *; }
-dontwarn com.yandex.mobile.ads.**

-keep class com.yandex.metrica.** { *; }
-keep class com.yandex.varioqub.** { *; }
-dontwarn com.yandex.metrica.**
-dontwarn com.yandex.mobile.ads.**
-dontwarn com.yandex.varioqub.**

#############################################
## Kotlin DateTime
#############################################
-keep class kotlinx.datetime.** { *; }
-dontwarn kotlinx.datetime.**

#############################################
## Koin
#############################################
-keep class org.koin.** { *; }
-dontwarn org.koin.**