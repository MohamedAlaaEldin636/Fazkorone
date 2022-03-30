/*
 * Copyright © 2021 Mohamed Alaa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

@file:Suppress("unused", "SameParameterValue", "PropertyName")

package com.mohamedalaa.moviestvshows.dependencies

/**
 * - GroupId, ArtifactId, Version
 */
object Deps {

    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"

    const val google_login = "com.google.android.gms:play-services-auth:${Versions.google_login}"
    const val facebook_login = "com.facebook.android:facebook-login:${Versions.facebook_login}"

    const val google_map = "com.google.android.gms:play-services-maps:${Versions.google_map}"
    const val google_location = "com.google.android.gms:play-services-location:${Versions.google_location}"
    const val google_places = "com.google.android.libraries.places:places:${Versions.google_places}"
    /*

  implementation("")
     */
    //const val facebook_login = "com.facebook.android:facebook-login:${Versions.facebook_login}"

    //const val google_login = ":${Versions.google_login}"
    //com.google.android.gms:play-services-location:19.0.1
    //com.google.android.libraries.places:places:2.5.0
    // +++++++++ Map

    // networking in samee retrofit + gson

    //glide

    const val multidex = "androidx.multidex:multidex:${Versions.multidex}"

    const val pdf_viewer = "es.voghdev.pdfviewpager:library:${Versions.pdf_viewer}"

    const val firebase_messaging = "com.google.firebase:firebase-messaging-ktx:23.0.0"
    const val firebase_analytics = "com.google.firebase:firebase-analytics-ktx:20.1.0"

    // firebase for fcm + tracking realtime databse isa.

    // Kotlin Coroutines

    const val exo_player = "com.google.android.exoplayer:exoplayer:${Versions.exo_player}"

    //      implementation 'com.github.smarteist:autoimageslider:1.4.0'
    const val slider_view = "com.github.smarteist:autoimageslider:${Versions.slider_view}"

    const val localization = "com.zeugmasolutions.localehelper:locale-helper-android:${Versions.localization}"

    val kotlin = Kotlin

    val own_libs = OwnLibs()

    val androidx = Androidx()

    val squareup = Squareup()

    val dagger = Dagger

    val glide = Glide

    val mohamed_alaa = MohamedAlaa()

    const val landscapist_glide = "com.github.skydoves:landscapist-glide:1.3.6"

    const val ellipsized_text_view = "com.thecodeyard.android:ellipsizedtextview:1.0.0"

    const val jdk_desugar = "com.android.tools:desugar_jdk_libs:${Versions.jdk_desugar}"

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val material = "com.google.android.material:material:${Versions.material}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val toasty = "com.github.GrenderG:Toasty:${Versions.toasty}"

    const val colored_console = "com.github.MohamedAlaaEldin636:Colored-Console:${Versions.colored_console}"

    const val protobuf_javalite = "com.google.protobuf:protobuf-javalite:3.10.0"

    // ---- Testing ---- //

    const val junit = "junit:junit:${Versions.junit}"

    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"

    // ---- Groups (might include testing as well isa) ---- //

    class MohamedAlaa : BaseGroup() {
        override val name: String
            get() = "com.github.MohamedAlaaEldin636"

        val mautils_gson = MAUtilsGson()

        inner class MAUtilsGson internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@MohamedAlaa}.MAUtils-Gson"

            override val version: String
                get() = Versions.mohamed_alaa_mautils_gson

            val core = lib("core")

            val processor = lib("processor")

            val extensions = lib("extensions")
        }
    }

    object Glide : SameVersionBaseGroup() {
        override val name: String
            get() = "com.github.bumptech.glide"

        override val version: String
            get() = Versions.glide

        val glide = lib("glide")
        val compiler = lib("compiler")
    }

    object Dagger : SameVersionBaseGroup() {
        override val name: String
            get() = "com.google.dagger"

        override val version: String
            get() = Versions.dagger_hilt

        val hilt_android = lib("hilt-android")
        val hilt_android_compiler = lib("hilt-android-compiler")
    }

    class Squareup : BaseGroup() {
        override val name: String
            get() = "com.squareup"

        val retrofit2 = Retrofit2()

        val okhttp3 = OkHttp3()

        inner class Retrofit2 : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Squareup.name}.retrofit2"

            override val version: String
                get() = Versions.retrofit2

            val retrofit = lib("retrofit")
            val converter_gson = lib("converter-gson")
        }

        inner class OkHttp3 internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Squareup.name}.okhttp3"

            override val version: String
                get() = Versions.okhttp3

            val logging_interceptor = lib("logging-interceptor")
        }

    }

    class Androidx : BaseGroup() {
        override val name: String
            get() = "androidx"

        val test = Test()

        val annotation = Annotation()

        val core = Core()

        val app_compat = AppCompat()

        val constraint_layout = ConstraintLayout()

        val fragment = Fragment()

        val activity = Activity()

        val lifecycle = Lifecycle()

        val room = Room()

        val navigation = Navigation()

        val paging = Paging()

        val hilt = Hilt()

        val recycler_view = RecyclerView()

        val drawer_layout = DrawerLayout()

        val view_pager_2 = ViewPager2()

        val datastore = DataStore()

        val compose = Compose()

        val work = Work()

        inner class Work internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.work"

            override val version: String
                get() = Versions.androidx_work

            val work_runtime_ktx = lib("work-runtime-ktx")

            val work_multiprocess = lib("work-multiprocess")
        }

        open inner class Compose internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.compose"

            override val version: String
                get() = Versions.androidx_compose

            val ui_package = UI()

            val material_package = Material()

            inner class UI internal constructor() : SameVersionBaseGroup() {
                override val name: String
                    get() = "${this@Compose.name}.ui"

                override val version: String
                    get() = this@Compose.version

                val ui = lib("ui")

                val ui_tooling_preview = lib("ui-tooling-preview")

                val ui_test_junit4 = lib("ui-test-junit4")

                val ui_tooling = lib("ui-tooling")
            }

            inner class Material internal constructor() : SameVersionBaseGroup() {
                override val name: String
                    get() = "${this@Compose.name}.material"

                override val version: String
                    get() = this@Compose.version

                val material = lib("material")
            }
        }

        inner class DataStore internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.datastore"

            override val version: String
                get() = Versions.androidx_data_store

            val preferences = lib("datastore-preferences")

            val typed = lib("datastore")

            val typed_core = lib("datastore-core")
        }

        inner class ViewPager2 internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.viewpager2"

            override val version: String
                get() = Versions.androidx_view_pager_2

            val viewpager2 = lib("viewpager2")
        }

        inner class DrawerLayout internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.drawerlayout"

            override val version: String
                get() = Versions.androidx_drawer_layout

            val drawerlayout = lib("drawerlayout")
        }

        inner class RecyclerView internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.recyclerview"

            override val version: String
                get() = Versions.androidx_recycler_view

            val recyclerview = lib("recyclerview")

            // This version is temporary as it should use same version isa.
            // Check https://developer.android.com/jetpack/androidx/releases/recyclerview#declaring_dependencies
            val selection = lib("recyclerview-selection", "1.1.0")
        }

        inner class Hilt internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.hilt"

            override val version: String
                get() = Versions.androidx_hilt

            val work = lib("hilt-work")
            val compiler = lib("hilt-compiler")
            val navigation_fragment = lib("hilt-navigation-fragment")
            val navigation_compose = lib("hilt-navigation-compose", "1.0.0-alpha03")
        }

        inner class Paging internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.paging"

            override val version: String
                get() = Versions.androidx_paging

            val runtime = lib("paging-runtime")

            val paging_compose = lib("paging-compose", "1.0.0-alpha12")
        }

        inner class Navigation internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.navigation"

            override val version: String
                get() = Versions.androidx_navigation

            val fragment_ktx = lib("navigation-fragment-ktx")
            val ui_ktx = lib("navigation-ui-ktx")

            val navigation_compose = lib("navigation-compose", "2.4.0-alpha09")

            /*
            // Feature module Support
            implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
             */
        }

        inner class Room internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.room"

            override val version: String
                get() = Versions.androidx_room

            val runtime = lib("room-runtime")
            val compiler = lib("room-compiler")
            val ktx = lib("room-ktx")
        }

        inner class Lifecycle internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.lifecycle"

            override val version: String
                get() = Versions.androidx_lifecycle

            val lifecycle_runtime_ktx = lib("lifecycle-runtime-ktx")
            val view_model_ktx = lib("lifecycle-viewmodel-ktx")
            val live_data_ktx = lib("lifecycle-livedata-ktx")
            val saved_state = lib("lifecycle-viewmodel-savedstate")
            val common_java8 = lib("lifecycle-common-java8")
            val app_process = lib("lifecycle-process")
            val lifecycle_view_model_compose = lib("lifecycle-viewmodel-compose", "2.4.0-beta01")

            // optional - helpers for implementing LifecycleOwner in a Service
            //implementation("androidx.lifecycle:lifecycle-service:$lifecycle_version")
        }

        inner class Activity internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.activity"

            override val version: String
                get() = Versions.androidx_activity

            val activity_ktx = lib("activity-ktx")

            val activity_compose = lib("activity-compose"/*, "1.4.0-alpha02"*/)
        }

        inner class Fragment internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.fragment"

            override val version: String
                get() = Versions.androidx_fragment

            val fragment_ktx = lib("fragment-ktx")
        }

        inner class Core internal constructor() : BaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.core"

            val core_ktx = lib("core-ktx", Versions.androidx_core)
        }

        inner class AppCompat internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.appcompat"

            override val version: String
                get() = Versions.androidx_app_compat

            val app_compat = lib("appcompat")
        }

        inner class ConstraintLayout internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.constraintlayout"

            override val version: String
                get() = Versions.androidx_constraint_layout

            val constraint_layout = lib("constraintlayout")

            val constraint_layout_compose = lib("constraintlayout-compose", "1.0.0-beta02")
        }

        inner class Test internal constructor() : BaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.test"

            val core_ktx = lib("core-ktx", Versions.Test.core)
            val runner = lib("runner", Versions.Test.runner)
            val rules = lib("rules", Versions.Test.rules)

            val espresso = Espresso()

            val ext = Ext()

            inner class Espresso : BaseGroup() {
                override val name: String
                    get() = "${this@Test.name}.espresso"

                val core = lib("espresso-core", Versions.Test.espresso)
            }

            inner class Ext : BaseGroup() {
                override val name: String
                    get() = "${this@Test.name}.ext"

                val junit_ktx = lib("junit-ktx", Versions.Test.Ext.junit)

                val truth = lib("truth", Versions.Test.Ext.truth)
            }

        }

        inner class Annotation internal constructor() : BaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.annotation"

            val annotation = lib("annotation", Versions.androidx_annotation)

        }

    }

    class OwnLibs : EmptyBaseGroup() {
        val core = ownLib("core")
        val common_value_objects = ownLib("common_value_objects")
        val local = ownLib("local")
        val remote = ownLib("remote")
        val repository = ownLib("repository")
        val shared_ui = ownLib("shared_ui")
        val shared = ownLib("shared")
        val feature = ownLib("feature")
    }

    object Kotlin : SameVersionBaseGroup() {
        override val name: String
            get() = "org.jetbrains.kotlin"

        override val version: String
            get() = Versions.kotlin

        val stdlib_jdk8 = lib("kotlin-stdlib-jdk8")

        val test = lib("kotlin-test")
    }

    abstract class BaseGroup {
        protected abstract val name: String

        protected fun lib(artifact: String, version: String): String = "$name:$artifact:$version"

        protected fun ownLib(moduleName: String): String = ":$moduleName"

        override fun toString(): String = name
    }

    abstract class EmptyBaseGroup : BaseGroup() {
        override val name: String
            get() = ""
    }

    abstract class SameVersionBaseGroup : BaseGroup() {
        abstract val version: String

        protected fun lib(artifact: String): String = "$name:$artifact:$version"
    }

}
