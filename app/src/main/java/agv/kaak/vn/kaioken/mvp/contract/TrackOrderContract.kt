package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.TrackOrder

interface TrackOrderContract {
    interface View{
        fun getListOrderingSuccess(listOrder:ArrayList<TrackOrder>)
        fun getListOrderingFail(msg:String?)

        fun getListOrderedSuccess(listOrder:ArrayList<TrackOrder>)
        fun getListOrderedFail(msg:String?)

        fun removeOrderSuccess()
        fun removeOrderFail(msg:String?)

        fun removeListOrderSuccess()
        fun removeListOrderFail(msg:String?)
    }

    interface Model{
        fun getListOrdering()
        fun getListOrdered(offset:Int, limit:Int)
        fun removeOrder(orderId:Int)
        fun removeListOrder(listOrderId:ArrayList<Int>)
    }
}