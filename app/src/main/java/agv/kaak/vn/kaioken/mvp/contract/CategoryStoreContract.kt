package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.CategoryStoreResult
import java.util.ArrayList

interface CategoryStoreContract {
    interface View{
        fun getCategoryRestaurantSuccess(data: ArrayList<CategoryStoreResult>)
        fun getCategoryRestaurantFail(message: String?)

        fun showListCategoryRestaurant(source: ArrayList<CategoryStoreResult>)
    }

    interface Model{
        fun getListCategoryStore()
    }
}