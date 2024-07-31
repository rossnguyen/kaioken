package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.CategoryStoreContract
import agv.kaak.vn.kaioken.mvp.model.CategoryStoreModel

class CategoryStorePresenter(val view:CategoryStoreContract.View) {
    val model:CategoryStoreModel= CategoryStoreModel(view)

    fun getListCategoryStore(){
        model.getListCategoryStore()
    }
}