package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.Store

interface StoreOnMapContract {
    interface View {
        fun onGetListStoreOnMapSuccess(data: ArrayList<Store>)
        fun onGetListStoreOnMapFail(message: String?)

        fun getListStoreFollowSuccess(data: ArrayList<Store>)
        fun getListStoreFollowFail(message: String?)

        fun getListStoreOfferSucces(data: ArrayList<Store>)
        fun getListStoreOfferFail(message: String?)

        fun sendLocationAtFirstSuccess()
        fun sendLocationAtFirstFail(msg:String?)
    }


    interface Model {
        fun getListStoreOnMap(lat: Double,
                                   lng: Double,
                                   keyword: String,
                                   category_id: Int,
                                   distance: Int,
                                   is_using_app: Int,
                                   is_shipping: Int,
                                   is_online: Int,
                                   rating: Int,
                                   offset: Int,
                                   limit: Int)

        fun getListStoreFollow(uid: Int, lat: Double, lng: Double, offset:Int, limit:Int)
        fun getListStoreOffer(lat: Double,
                              lng: Double,
                              distance: Int,
                              offset: Int,
                              limit: Int)

        fun sendLocationAtFirst(lat:Double, lng:Double)

    }
}