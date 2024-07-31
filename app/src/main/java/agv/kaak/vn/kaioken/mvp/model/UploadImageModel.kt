package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.UploadImageService
import agv.kaak.vn.kaioken.entity.response.UploadImageResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.UploadImageContract
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.res.Resources
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadImageModel(val view: UploadImageContract.View) : UploadImageContract.Model {
    private val TAG = UploadImageModel::class.java.simpleName
    override fun uploadImage(pId: String, typeImage: String, subTypeImage: String, pathFile: String) {
        val objectId = RequestBody.create(MediaType.parse("multipart/form-data"), pId)
        val type = RequestBody.create(MediaType.parse("multipart/form-data"), typeImage)
        val subType = RequestBody.create(MediaType.parse("multipart/form-data"), subTypeImage)
        var image: MultipartBody.Part? = null
        if (pathFile.isNotEmpty()) {
            val file = File(pathFile)
            val imageStream = ImageHelper.getStreamByteFromImage(file)
            //val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), imageStream)
            image = MultipartBody.Part.createFormData("files", file.name, requestFile)
        }

        if (Constraint.mRetrofitImage == null) {
            view.uploadImageFail("")
            return
        }

        Constraint.mRetrofitImage!!.create(UploadImageService::class.java)
                .uploadImage(objectId, type, subType, image!!, GlobalHelper.getHeadersForImage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<UploadImageResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(uploadImageResponse: UploadImageResponse) {
                        if (uploadImageResponse.status == Constraint.STATUS_RIGHT)
                            view.uploadImageSuccess(uploadImageResponse.uploadImage!!)
                        else {
                            view.uploadImageFail(
                                    if (uploadImageResponse.error?.code == 0)
                                        uploadImageResponse.error.desc!!
                                    else
                                        try {
                                            Constraint.errorMap!![uploadImageResponse.error?.code]!!
                                        } catch (ex: Exception) {
                                            ""
                                        }
                            )
                            GlobalHelper.logE(TAG, uploadImageResponse.error?.desc)
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.uploadImageFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }
}