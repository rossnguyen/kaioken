package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.NotificationResult
import retrofit2.http.Field

interface NotificationContract {
    interface View{
        fun getListNoticeNotiSuccess(listNoti:ArrayList<NotificationResult>)
        fun getListNoticeFail(msg:String?)
    }

    interface Model{
        fun getListNoticeNoti(userId: Int,departmentId: Int,pageId: Int,offset: Int,limit: Int)
    }
}