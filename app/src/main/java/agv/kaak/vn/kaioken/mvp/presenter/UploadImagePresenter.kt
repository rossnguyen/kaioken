package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.UploadImageContract
import agv.kaak.vn.kaioken.mvp.model.UploadImageModel

class UploadImagePresenter(val view:UploadImageContract.View) {
    val model:UploadImageModel

    init {
        this.model= UploadImageModel(view)
    }

    fun uploadImage(pId: String, typeImage: String, subTypeImage: String, pathFile: String){
        model.uploadImage(pId,typeImage, subTypeImage, pathFile)
    }
}