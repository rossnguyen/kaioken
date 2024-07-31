package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.InfoCoupon
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.InfoCouponContract
import agv.kaak.vn.kaioken.mvp.presenter.InfoCouponPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.res.Resources
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_detail_coupon.*

class DialogDetailCoupon : BaseDialogFragment(), InfoCouponContract.View {

    var couponId: Int = -1
    val infoCouponPresenter = InfoCouponPresenter(this)
    var nameStore = ""
    var linkImage = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_detail_coupon, container, false)
    }

    override fun getData() {
        couponId = arguments?.getInt(Constraint.DATA_SEND)!!
        nameStore = arguments!!.getString(DialogDetailPromotion.NAME_SEND)
        linkImage = arguments!!.getString(DialogDetailPromotion.LINK_IMAGE_SEND)
    }

    override fun loadData() {
        layoutLoading?.visibility = View.VISIBLE
        btnSeeDetailStore?.visibility = View.GONE
        infoCouponPresenter.getInfoCoupon(couponId)
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val widthScreen = Resources.getSystem().displayMetrics.widthPixels

            dialog.window.setLayout(widthScreen * 80 / 100, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun addEvent() {
        btnSeeDetailStore.setOnClickListener {
            if (onClickSeeDetail != null) {
                onClickSeeDetail!!.onClick()
                dismiss()
            }
        }

        ibtnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun listenSocket() {
        //nothing to do
    }

    override fun offSocket() {
        //nothing to do
    }

    override fun getInfoCouponSuccess(infoCoupon: InfoCoupon) {
        layoutLoading?.visibility = View.GONE
        btnSeeDetailStore?.visibility = View.VISIBLE

        ImageHelper.loadImage(mContext, imgAvatar, linkImage, PlaceHolderType.RESTAURANT)

        tvName?.text = nameStore

        tvTime?.text = Html.fromHtml("${mContext.resources.getString(R.string.detail_store_see_detail_promotion_time_from)} " +
                "<b>${ConvertHelper.reverseDate(infoCoupon.dateStart!!.split(" ")[0], "-")}</b> " +
                "${mContext.resources.getString(R.string.detail_store_see_detail_promotion_time_to)} " +
                "<b>${ConvertHelper.reverseDate(infoCoupon.dateEnd!!.split(" ")[0], "-")}</b")

        tvCouponValue?.text = Html.fromHtml("${mContext.resources.getString(R.string.detail_store_see_detail_promotion_discount_value)} " +
                "<b>${if (infoCoupon.discountType == DiscountType.PERCENT) "${infoCoupon.discountValue}%" else "${infoCoupon.discountValue}K"}</b>")

        tvDescription?.text = infoCoupon.description
    }

    override fun getInfoCouponFail(msg: String?) {
        layoutLoading?.visibility = View.GONE
        btnSeeDetailStore?.visibility = View.VISIBLE

        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, msg, true)
    }

    interface OnClickSeeDetail {
        fun onClick()
    }

    var onClickSeeDetail: OnClickSeeDetail? = null
}