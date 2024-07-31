package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.FollowStoreConstract
import agv.kaak.vn.kaioken.mvp.model.FollowStoreModel

class FollowStorePresenter(val view:FollowStoreConstract.View){
    val model=FollowStoreModel(view)

    fun followStore(uid:Int, pageId:Int){
        this.model.followStore(uid,pageId)
    }
}