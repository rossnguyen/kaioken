package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.ImageDetailFood
import agv.kaak.vn.kaioken.entity.ImageDetailItemFood
import agv.kaak.vn.kaioken.entity.MenuFood

interface MenuItemDetailContract {
    interface View{
        fun getMenuItemDetailSuccess(data: MenuFood)
        fun getMenuItemDetailFail(message: String)
    }

    interface Model{
        fun getMenuItemDetail(menuId: Int)
    }
}