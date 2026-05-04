# Add project specific ProGuard rules here.

# Supabase / Ktor
-keep class io.github.jan.supabase.** { *; }
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Web3j
-keep class org.web3j.** { *; }
-dontwarn org.web3j.**
-dontwarn org.bouncycastle.**

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }
-keep,includedescriptorclasses class com.carboncredit.app.data.models.**$$serializer { *; }
-keepclassmembers class com.carboncredit.app.data.models.** {
    *** Companion;
}
-keepclasseswithmembers class com.carboncredit.app.data.models.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class com.carboncredit.app.core.network.ApiService { *; }

# iText PDF
-keep class com.itextpdf.** { *; }
-dontwarn com.itextpdf.**

# ZXing
-keep class com.google.zxing.** { *; }
