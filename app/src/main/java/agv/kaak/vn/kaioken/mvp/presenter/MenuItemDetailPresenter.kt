package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.MenuItemDetailContract
import agv.kaak.vn.kaioken.mvp.model.MenuItemDetailModel

class MenuItemDetailPresenter(val view:MenuItemDetailContract.View) {
    lateinit var menuItemDetailModel:MenuItemDetailModel

    init {
        this.menuItemDetailModel= MenuItemDetailModel(view)
    }

    fun getMenuItemDetail(menuId: Int){
        this.menuItemDetailModel.getMenuItemDetail(menuId)
    }
}