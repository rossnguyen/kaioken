package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.DetailRecruitmentResult

interface GetRecruitmentContract {
    interface View{
        fun getDetailRecruitmentSuccess(detailRecruitment:DetailRecruitmentResult)
        fun getDetailRecruitmentFail(message:String)
    }

    interface Model{
        fun getDetailRecruitment(storeId: Int)
    }
}