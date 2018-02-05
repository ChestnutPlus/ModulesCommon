# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#---------------------Glide---------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#---------------------GSon---------------------------
-keep class com.google.gson.** {*;}

#---------------------Utils---------------------------
-keep class com.chestnut.common.ui.XTextView{ *; }
-keep class com.chestnut.common.utils.LogUtils{ *; }
-keep class com.chestnut.common.utils.CrashUtils{ *; }
-keep class com.chestnut.common.helper.si.XJsonHelper{ *; }

#------------------Retrolambda---------------------
-dontwarn java.lang.invoke.*

#---------------------Rx---------------------------
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#---------------Retrofit2.0，Okhttp3.x，Okio---------------
# Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
# Okhttp3.x，Okio
-dontwarn okio.**
-keep class okio.** { *;}
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
