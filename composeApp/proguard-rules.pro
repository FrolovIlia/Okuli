#############################################
## Общие правила
#############################################
-ignorewarnings
-keepattributes SourceFile,LineNumberTable,Signature,InnerClasses,Exceptions,Annotation,EnclosingMethod
-dontwarn java.lang.invoke.StringConcatFactory # [citation:1]

#############################################
## Compose (включая Material 3)
#############################################
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Material 3 может требовать дополнительные правила (судя по схожим ошибкам в Desktop) [citation:2]
-keep class androidx.compose.material3.** { *; }
-dontwarn androidx.compose.material3.**

# Общие правила для Compose
-keepclassmembers class * implements androidx.compose.ui.Modifier$Element { *; }
-keepclassmembers class * extends androidx.compose.ui.Modifier$Node { *; }
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
}

#############################################
## Voyager
#############################################
-keep class cafe.adriel.voyager.** { *; }
-dontwarn cafe.adriel.voyager.**

#############################################
## Kotlin
#############################################
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# Правило для корутин в WorkManager
-keepnames class kotlinx.coroutines.android.** { *; }

#############################################
## Realm
#############################################
-keep class io.realm.** { *; }
-dontwarn io.realm.**

# Дополнительно защищаем модели, чтобы избежать проблем с сериализацией
-keep class com.pixelrabbit.oculi.**.database.** { *; }
-keepclassmembers class com.pixelrabbit.oculi.**.database.** {
    <fields>;
}

#############################################
## WorkManager (ДОБАВЛЕНО)
#############################################
-keep class androidx.work.** { *; }
-keepclassmembers class androidx.work.** { *; }
-dontwarn androidx.work.**

# Сохраняем Worker, чтобы он не был обфусцирован
-keep class com.pixelrabbit.oculi.**.workers.** { *; }
-keepclassmembers class com.pixelrabbit.oculi.**.workers.** {
    <methods>;
}

#############################################
## Koin
#############################################
-keep class org.koin.** { *; }
-dontwarn org.koin.**

#############################################
## Ktor (если используется)
#############################################
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

#############################################
## Яндекс Реклама
#############################################
-keep class com.yandex.mobile.ads.** { *; }
-dontwarn com.yandex.mobile.ads.**

# Яндекс Ads и AppMetrica
-keep class com.yandex.metrica.** { *; }
-keep class com.yandex.varioqub.** { *; }
-dontwarn com.yandex.metrica.**
-dontwarn com.yandex.varioqub.**

#############################################
## Прочие важные зависимости
#############################################
# Для Kotlin DateTime
-keep class kotlinx.datetime.** { *; }
-dontwarn kotlinx.datetime.**

#############################################
## Защита приложения и Android
#############################################
# Сохраняем все классы приложения (временная мера для отладки)
# Позже можно будет уточнить и сократить это правило
-keep class com.pixelrabbit.oculi.** { *; }

# Сохраняем R классы от всех модулей [citation:1]
-dontwarn **.R$*
-keepclassmembers class **.R$* {
    <fields>;
}

# Сохраняем ViewModel и другие важные компоненты Android
-keep class * extends androidx.lifecycle.ViewModel
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <methods>;
}