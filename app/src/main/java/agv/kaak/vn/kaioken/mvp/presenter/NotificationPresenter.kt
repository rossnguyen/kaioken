package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.NotificationContract
import agv.kaak.vn.kaioken.mvp.model.NotificationModel

class NotificationPresenter(val view:NotificationContract.View) {
    val model=NotificationModel(view)

    fun getListNoticeNoti(userId: Int,departmentId: Int,pageId: Int,offset: Int,limit: Int){
        model.getListNoticeNoti(userId,departmentId, pageId, offset, limit)
    }
}