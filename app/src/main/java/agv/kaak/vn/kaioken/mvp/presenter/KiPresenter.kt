package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.KiContract
import agv.kaak.vn.kaioken.mvp.model.KiModel

class KiPresenter(val view: KiContract.View) {
    val model: KiModel = KiModel(view)

    fun getListKiOfUser(offset: Int, limit: Int) {
        model.getListKiOfUser(offset, limit)
    }

    fun getKiOfPage(pageId: Int) {
        model.getKiOfPage(pageId)
    }
}