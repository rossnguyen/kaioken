package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.InfoScanToGoStoreResult

interface ScanQRToGoStoreContract {
    interface View{
        fun getStoreFromQRSuccess(data:InfoScanToGoStoreResult)
        fun getStoreFromQRFail(message:String?)
    }

    interface Model{
        fun getStoreFromQR(sig:String, lat:Double, lng:Double)
    }
}