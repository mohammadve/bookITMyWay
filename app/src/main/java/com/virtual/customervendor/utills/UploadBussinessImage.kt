package com.virtual.customer_vendor.utill

import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.model.response.UserProfilePicResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.AppKeys
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.ProgressDialogLoader
import com.virtual.customervendor.vendor.ui.activity.VendorTaxiActivity
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadBussinessImage {
    companion object {
        var TAG: String = VendorTaxiActivity::class.java.simpleName
        var bussinessImage: IBussinessImage? = null
        var bussinessImage1: IBussinessImage1? = null
        var eventImage: IEvevtServiceImage? = null
        public fun uploadUserPicOnServer(file: File, bussinessId: String, bussinessImage: IBussinessImage) {
            this.bussinessImage = bussinessImage
            if (file != null) {
//                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//                // MultipartBody.Part is used to send also the actual file name
//                val img = MultipartBody.Part.createFormData(AppKeys.VENDOR_BUSSINESSIMAGE, file.name, requestFile)
//                val anInterface = ApiClient.getClient().create(ApiInterface::class.java)
//                anInterface.updateBussinessImage("Bearer " + SharedPreferenceManager.getAuthToken(), bussinessId, img).subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(object : SingleObserver<UserProfilePicResponse> {
//                            override fun onSubscribe(d: Disposable) {
//                                bussinessImage.onSubscribe(d)
//                            }
//
//                            override fun onSuccess(userImageUploadResponse: UserProfilePicResponse) {
//                                ProgressDialogLoader.progressDialogDismiss()
//                                bussinessImage.onSuccess(userImageUploadResponse)
//                                AppLog.e(TAG, userImageUploadResponse.toString())
//                            }
//
//                            override fun onError(e: Throwable) {
//                                ProgressDialogLoader.progressDialogDismiss()
//                                bussinessImage.onError(e)
//                                AppLog.e(TAG, e.toString())
//                            }
//                        })
            }
        }

        public fun uploadPicsOnServer(addfile: ArrayList<File>, bussinessId: String, bussinessImage: IBussinessImage1) {
            this.bussinessImage1 = bussinessImage
            var imageList = java.util.ArrayList<MultipartBody.Part>()
//            var deleteimageList = java.util.ArrayList<MultipartBody.Part>()
            if (addfile != null) {

                for (imageFile in addfile) {
                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
                    imageList.add(MultipartBody.Part.createFormData(AppKeys.VENDOR_BUSSINESSIMAGES, imageFile.name, requestFile))
                }
            }

            val anInterface = ApiClient.client.create(ApiInterface::class.java)
            anInterface.updateBussinessImages("Bearer " + SharedPreferenceManager.getAuthToken(), bussinessId, imageList).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<BusinessImagesResponse> {
                        override fun onSubscribe(d: Disposable) {
                            bussinessImage.onSubscribe(d)
                        }

                        override fun onSuccess(userImageUploadResponse: BusinessImagesResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            bussinessImage.onSuccess(userImageUploadResponse)
                            AppLog.e(TAG, userImageUploadResponse.toString())
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            bussinessImage.onError(e)
                            if (e != null)
                                AppLog.e(TAG, e.toString())
                        }
                    })
        }

        public fun uploadEventImageOnServer(files: ArrayList<File>, bussinessId: String, eventImage: IEvevtServiceImage) {
            this.eventImage = eventImage
            if (files.size > 0) {
                var imageList = java.util.ArrayList<MultipartBody.Part>()
                for (imageFile in files) {
                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
                    imageList.add(MultipartBody.Part.createFormData(AppKeys.VENDOR_EVENTIMAGE, imageFile.name, requestFile))
                }


                val anInterface = ApiClient.client.create(ApiInterface::class.java)
                anInterface.updateEventImage("Bearer " + SharedPreferenceManager.getAuthToken(), bussinessId, imageList).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : SingleObserver<BusinessImagesResponse> {
                            override fun onSubscribe(d: Disposable) {
                                eventImage.onSubscribeEvent(d)
                            }

                            override fun onSuccess(userImageUploadResponse: BusinessImagesResponse) {
                                ProgressDialogLoader.progressDialogDismiss()
                                eventImage.onSuccessEvent(userImageUploadResponse)
                                AppLog.e(TAG, userImageUploadResponse.toString())
                            }

                            override fun onError(e: Throwable) {
                                ProgressDialogLoader.progressDialogDismiss()

                                eventImage.onErrorEvent(e)
                                if (e != null) {
                                    AppLog.e(TAG, e.toString())
                                }
                            }
                        })
            }
        }
    }

    interface IBussinessImage {
        fun onSuccess(userImageUploadResponse: UserProfilePicResponse)
        fun onError(e: Throwable)
        fun onSubscribe(d: Disposable)
    }

    interface IBussinessImage1 {
        fun onSuccess(userImageUploadResponse: BusinessImagesResponse)
        fun onError(e: Throwable)
        fun onSubscribe(d: Disposable)
    }

    interface IEvevtServiceImage {
        fun onSuccessEvent(userImageUploadResponse: BusinessImagesResponse)
        fun onErrorEvent(e: Throwable)
        fun onSubscribeEvent(d: Disposable)
    }
}