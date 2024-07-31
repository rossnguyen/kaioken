package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ImageHelper
import kotlinx.android.synthetic.main.fragment_ki_info.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class KiInfoFragment : BaseFragment() {

    var qrCode: String? = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ki_info, container, false)
    }

    override fun getData() {
        //nothing to get
    }

    override fun loadData() {
        //nothing to load
        loadQRCode(qrCode)
    }

    override fun addEvent() {
        layoutListKiSaved.setOnClickListener {
            if (onFeaturesClickListenter != null)
                onFeaturesClickListenter!!.onListKiClick()
        }

        layoutListCoupon.setOnClickListener {
            if (onFeaturesClickListenter != null)
                onFeaturesClickListenter!!.onListCouponClick()
        }
    }

    fun loadQRCode(linkQRCode: String?) {
        qrCode = linkQRCode
        ImageHelper.loadImage(mContext, imgQR, qrCode, PlaceHolderType.IMAGE)
    }

    var onFeaturesClickListenter: OnFeaturesClickListenter? = null

    interface OnFeaturesClickListenter {
        fun onListKiClick()
        fun onListCouponClick()
    }
}
