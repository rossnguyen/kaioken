package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.ScanQRToGoStoreContract
import agv.kaak.vn.kaioken.mvp.model.ScanQRToGoStoreModel

class ScanQRToGoStorePresenter(val view:ScanQRToGoStoreContract.View) {
    lateinit var model:ScanQRToGoStoreModel

    init {
        this.model= ScanQRToGoStoreModel(view)
    }

    fun getStoreFromQR(sig: String, lat: Double, lng: Double){
        this.model.getStoreFromQR(sig, lat, lng)
    }
}