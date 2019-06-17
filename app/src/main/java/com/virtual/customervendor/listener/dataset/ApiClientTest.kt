//package com.virtual.customervendor.listener.dataset
//
//import com.virtual.customervendor.utills.AppConstants
//import dagger.Module
//import dagger.Provides
//
//import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.converter.gson.GsonConverterFactory
//
//@Module
// class ApiClientTest {
//    constructor()
//
//
//    private var retrofit: Retrofit? = null
//
//
//    //By Using Rx java
//
////    val client: Retrofit
////        get() {
////            if (retrofit == null) {
////                retrofit = Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
////                        .addConverterFactory(GsonConverterFactory.create())
////                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
////                        .build()
////            }
////            return this!!.retrofit!!
////        }
//
//    @Provides
//    fun get(): Retrofit {
//        if (retrofit == null) {
//            retrofit = Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build()
//        }
//        return this!!.retrofit!!
//    }
//
//}
