package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.DetailStoreResult

interface DetailStoreContract {
    interface View {
        fun getDetailStoreSuccess(data: DetailStoreResult)
        fun getDetailStoreFail(msg: String?)
    }

    interface Model {
        fun getDetailStore(pageId: Int, uid: Int)
    }
}