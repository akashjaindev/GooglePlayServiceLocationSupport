# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\ide\android\AndroidSDK/tools/proguard/proguard-android.txt
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


#-optimizationpasses 5
#-renamesourcefileattribute SourceFile
#-optimizations !code/simplification/advanced,code/simplification/*
#
#
#-keepclassmembers enum * { public static **[] values(); public static ** valueOf(java.lang.String); }
#
#-keep class com.github.akashandroid90.googlesupport.location.** { *; }
#
#-keep class com.google.android.gms.**
#-keep class com.google.android.gms.location.**
#-keep class com.google.api.services.*.model.*
#-keep class com.google.api.client.**
#
#-keep public class * extends android.view.View {
#void set*(***);
#   *** get*();
#   }
