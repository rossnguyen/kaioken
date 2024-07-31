package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.UploadImageResult

interface UploadImageContract {
    interface View{
        fun uploadImageSuccess(data: UploadImageResult)
        fun uploadImageFail(msg: String)
    }

    interface Model{
        fun uploadImage(pId: String, typeImage: String, subTypeImage: String, pathFile: String)
    }
}