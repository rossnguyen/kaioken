package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.GetListImageStoreContract
import agv.kaak.vn.kaioken.mvp.model.GetListImageStoreModel

class GetListImageStorePresenter(val view:GetListImageStoreContract.View){
    lateinit var model:GetListImageStoreModel

    init {
        this.model=GetListImageStoreModel(view)
    }

    fun getListImageStore(storeId:Int){
        this.model.getListImageStore(storeId)
    }
}