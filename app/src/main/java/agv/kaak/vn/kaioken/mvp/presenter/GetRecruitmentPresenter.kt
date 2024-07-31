package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.GetRecruitmentContract
import agv.kaak.vn.kaioken.mvp.model.GetRecruitmentModel

class GetRecruitmentPresenter(val view:GetRecruitmentContract.View){
    lateinit var model:GetRecruitmentModel

    init {
        model= GetRecruitmentModel(view)
    }

    fun getDetailRecruitment(storeId: Int){
        model.getDetailRecruitment(storeId)
    }
}