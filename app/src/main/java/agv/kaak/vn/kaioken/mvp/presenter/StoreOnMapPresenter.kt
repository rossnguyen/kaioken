package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.StoreOnMapContract
import agv.kaak.vn.kaioken.mvp.model.StoreOnMapModel

class StoreOnMapPresenter(val view: StoreOnMapContract.View) {
    val model: StoreOnMapModel = StoreOnMapModel(view)

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
                          limit: Int) {
        model.getListStoreOnMap(lat, lng, keyword, category_id, distance, is_using_app, is_shipping, is_online, rating, offset, limit)
    }

    fun getListStoreFollow(uid: Int, lat: Double, lng: Double, offset: Int, limit: Int) {
        model.getListStoreFollow(uid, lat, lng, offset, limit)
    }

    fun getListStoreOffer(lat: Double,
                          lng: Double,
                          distance: Int,
                          offset: Int,
                          limit: Int) {
        model.getListStoreOffer(lat, lng, distance, offset, limit)
    }

    fun sendLocationAtFirst(lat:Double, lng:Double){
        model.sendLocationAtFirst(lat,lng)
    }
}