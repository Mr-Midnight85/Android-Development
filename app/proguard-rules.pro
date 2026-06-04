# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# signingConfig, minifyEnabled, and shrinkResources "false" flag inside
# build.gradle.kts files.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Hilt ProGuard rules
-keep,allowobfuscation interface com.google.inject.Injector

# Room Database ProGuard rules
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**
-dontwarn androidx.annotation.**

# Retrofit ProGuard rules
-keepattributes Signature, InnerClasses
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Coroutines ProGuard rules
-keep class kotlinx.coroutines.** { *; }

# Keep Compose runtime classes
-keep class androidx.compose.** { *; }
