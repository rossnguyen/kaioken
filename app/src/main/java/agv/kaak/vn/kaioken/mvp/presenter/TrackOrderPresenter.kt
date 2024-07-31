package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.TrackOrderContract
import agv.kaak.vn.kaioken.mvp.model.TrackOrderModel

class TrackOrderPresenter(val view:TrackOrderContract.View) {
    val model=TrackOrderModel(view)

    fun getListOrdering(){
        model.getListOrdering()
    }

    fun getListOrdered(offset:Int, limit:Int){
        model.getListOrdered(offset, limit)
    }

    fun removeOrdered(orderId:Int){
        model.removeOrder(orderId)
    }

    fun removeListOrdered(listOrderedId:ArrayList<Int>){
        model.removeListOrder(listOrderedId)
    }
}