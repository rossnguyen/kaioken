package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.api.DetailStoreService
import agv.kaak.vn.kaioken.mvp.contract.DetailStoreContract
import agv.kaak.vn.kaioken.mvp.model.DetailStoreModel
import android.view.View
import com.wang.avi.AVLoadingIndicatorView

class DetailStorePresenter(val view: DetailStoreContract.View) {
    lateinit var detailStoreMode: DetailStoreModel

    init {
        this.detailStoreMode = DetailStoreModel(view)
    }

    fun getDetailStore(pageId: Int, uid: Int) {
        this.detailStoreMode.getDetailStore(pageId, uid)
    }
}