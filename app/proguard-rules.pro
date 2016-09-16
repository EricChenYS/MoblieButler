# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\CWQ\AppData\Local\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn cn.sharesdk.**
-keep class cn.sharesdk.** { *; }
-dontwarn com.mob.**
-keep class com.mob.** { *; }
-dontwarn com.hp.**
-keep class com.hp.** { *; }
-dontwarn com.lidroid.**
-keep class com.lidroid.** { *; }
-dontwarn com.android.**
-keep class com.android.** { *; }
-dontwarn demo.**
-keep class demo.** { *; }
-dontwarn net.sourceforge.**
-keep class net.sourceforge.** { *; }