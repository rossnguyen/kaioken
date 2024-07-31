package agv.kaak.vn.kaioken.mvp.contract

interface RateForStoreContract {
    interface View {
        fun onRateSuccess()
        fun onRateFail(message: String?)
    }

    interface Model {
        fun rateForStore(pageId: Int, rate: Int, feedback: String, orderId:Int)
    }
}