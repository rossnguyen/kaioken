package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.StorePromotionAdapter
import agv.kaak.vn.kaioken.entity.StorePromotion
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.StorePromotionContract
import agv.kaak.vn.kaioken.mvp.presenter.StorePromotionPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_detail_promotion.*
import android.support.v7.widget.LinearLayoutManager


class DialogDetailPromotion : BaseDialogFragment(), StorePromotionContract.View {

    companion object {
        val NAME_SEND = "NAME_SEND"
        val LINK_IMAGE_SEND = "LINK_IMAGE_SEND"
    }

    val promotionPresenter = StorePromotionPresenter(this)

    lateinit var promotionInfo: StorePromotion
    var pageId: Int = -1
    var nameStore: String = ""
    var linkImageStore: String = ""
    var promotions: ArrayList<StorePromotion> = arrayListOf()
    lateinit var adapterPromotion: StorePromotionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_detail_promotion, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val widthScreen = Resources.getSystem().displayMetrics.widthPixels

            dialog.window.setLayout(widthScreen * 80 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun getData() {
        //promotionInfo = arguments?.getSerializable(Constraint.DATA_SEND) as StorePromotion
        pageId = arguments!!.getInt(Constraint.DATA_SEND, -1)
        nameStore = arguments!!.getString(NAME_SEND, "")
        linkImageStore = arguments!!.getString(LINK_IMAGE_SEND)

    }

    override fun loadData() {
        //ImageHelper.loadImage(mContext, imgAvatar, linkImageStore, PlaceHolderType.RESTAURANT)
        tvName?.text = nameStore

        initListPromotion()
        layoutLoading?.visibility = View.VISIBLE
        promotionPresenter.getListPromotion(pageId)
    }

    override fun addEvent() {
        btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun listenSocket() {
        //nothing to do
    }

    override fun offSocket() {
        //nothing to do
    }

    private fun initListPromotion() {
        adapterPromotion = StorePromotionAdapter(mContext, promotions)

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstPromotion.adapter = adapterPromotion
        lstPromotion.layoutManager = layoutManager
    }

    override fun getDetailStorePromotionSuccess(promotion: StorePromotion) {
        //nothing to do

    }

    override fun getDetailStorePromotionFail(msg: String?) {
        //nothing to do
    }

    override fun getListStorePromotionSuccess(promotions: ArrayList<StorePromotion>) {
        this.promotions.clear()
        layoutLoading?.visibility = View.GONE
        this.promotions.addAll(promotions)
        adapterPromotion.notifyDataSetChanged()
    }

    override fun getListStorePromotionFail(msg: String?) {
        layoutLoading?.visibility = View.GONE
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, msg, true)
    }
}