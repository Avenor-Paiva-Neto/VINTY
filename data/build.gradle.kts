plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)          // OBRIGATÓRIO
    alias(libs.plugins.hilt.android) // OBRIGATÓRIO
}

android {
    namespace = "io.android.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(project(":domain"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room (Banco de Dados Local)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // O compilador do Room via KSP

    implementation(platform(libs.firebase.bom))

    // Firebase Auth (não precisa de versão aqui pois o BoM gerencia)
    implementation(libs.firebase.auth)
    // Firestore (Banco de Dados NoSQL)
    implementation(libs.firebase.firestore)

    // Storage (Armazenamento de Fotos/Arquivos)
    implementation(libs.firebase.storage)

    // Analytics (Opcional - Monitoramento de uso)
    implementation(libs.firebase.analytics)

    // Javax Inject (Para o @Inject e @Singleton)
    implementation(libs.javax.inject)

    // Hilt (Necessário se você estiver usando Hilt para injetar o FirebaseAuth)
    implementation(libs.hilt.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}