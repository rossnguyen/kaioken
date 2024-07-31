package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.ForgotPasswordContract
import agv.kaak.vn.kaioken.mvp.model.ForgotPasswordModel


/**
 * Created by MyPC on 16/03/2018.
 */
class ForgotPasswordPresenter(private val mView: ForgotPasswordContract.View):ForgotPasswordContract.Model {

    private val model = ForgotPasswordModel(mView)

    override fun forgotPassword(phone: String, code: String, token: String) {
        model.forgotPassword(phone, code, token)
    }
}