package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.KiOfPageResult
import agv.kaak.vn.kaioken.entity.result.KiOfUserResult

interface KiContract {
    interface View{
        fun getListKiOfUserSuccess(listKiOfUser:KiOfUserResult)
        fun getListKiOfUserFail(msg:String?)

        fun getKiOfPageSuccess(kiOfPage:KiOfPageResult)
        fun getKiOfPageFail(msg:String?)
    }

    interface Model{
        fun getListKiOfUser(offset:Int, limit:Int)
        fun getKiOfPage(pageId:Int)
    }
}