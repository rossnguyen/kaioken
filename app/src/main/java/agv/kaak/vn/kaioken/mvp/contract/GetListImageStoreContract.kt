package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.ImageDetailRestautantResult

interface GetListImageStoreContract {
    interface View{
        fun getListImageStoreSuccess(listImage:ArrayList<ImageDetailRestautantResult>)
        fun getListImageStoreFail(message:String)
    }

    interface Model{
        fun getListImageStore(storeId:Int)
    }
}