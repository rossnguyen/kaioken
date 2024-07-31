package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.KiOfPageResult
import agv.kaak.vn.kaioken.entity.result.KiOfUserResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.KiContract
import agv.kaak.vn.kaioken.mvp.presenter.KiPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.util.Log
import kotlinx.android.synthetic.main.fragment_ki.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class KiFragment : BaseFragment(), KiContract.View {

    lateinit var kiInfoFragment: KiInfoFragment
    lateinit var listKiFragment: ListKiFragment
    lateinit var listCouponFragment: ListCouponFragment

    private val TAB_INFO = "TAB_INFO"
    private val TAB_LIST_KI = "TAB_LIST_KI"
    private val TAB_LIST_COUPON = "TAB_LIST_COUPON"

    var fragmentIsShowing = TAB_INFO

    val kiPresenter = KiPresenter(this)
    var qrCode: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ki, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kiPresenter.getListKiOfUser(0, 0)
        mFragmentManager.beginTransaction().replace(R.id.layoutContent, kiInfoFragment).commit()
    }

    override fun getData() {
        //nothing to get
    }

    override fun loadData() {

    }

    override fun addEvent() {
        kiInfoFragment.onFeaturesClickListenter = object : KiInfoFragment.OnFeaturesClickListenter {
            override fun onListKiClick() {
                showFragment(TAB_LIST_KI)
            }

            override fun onListCouponClick() {
                showFragment(TAB_LIST_COUPON)
            }
        }

        listKiFragment.onBackButtonClick = View.OnClickListener {
            showFragment(TAB_INFO)
        }

        listCouponFragment.onBackButtonCLickListenter = View.OnClickListener {
            showFragment(TAB_INFO)
        }
    }

    init {
        kiInfoFragment = KiInfoFragment()
        listKiFragment = ListKiFragment()
        listCouponFragment = ListCouponFragment()
    }

    private fun showFragment(TAB: String) {
        when (TAB) {
            TAB_INFO -> {
                mFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.layoutContent, kiInfoFragment).commit()
                fragmentIsShowing = TAB_INFO
                kiInfoFragment.loadQRCode(qrCode)
            }
            TAB_LIST_KI -> {
                mFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.layoutContent, listKiFragment).commit()
                fragmentIsShowing = TAB_LIST_KI
            }
            TAB_LIST_COUPON -> {
                mFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.layoutContent, listCouponFragment).commit()
                fragmentIsShowing = TAB_LIST_COUPON
            }
        }
    }

    override fun getListKiOfUserSuccess(listKiOfUser: KiOfUserResult) {
        layoutLoading?.visibility = View.GONE
        bindDataToToolbar(listKiOfUser)

        //put data to KiInfo fragment
        qrCode = listKiOfUser.qrCode
        kiInfoFragment.loadQRCode(listKiOfUser.qrCode)

        //put data to ListKi fragment
        val data = Bundle()
        data.putSerializable(Constraint.DATA_SEND, listKiOfUser.listKi)
        listKiFragment.arguments = data
    }

    override fun getListKiOfUserFail(msg: String?) {
        layoutLoading?.visibility = View.GONE
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.ki_get_list_ki_fail), true)
        Log.e("****GetListKi", msg)
    }

    override fun getKiOfPageSuccess(kiOfPage: KiOfPageResult) {
        //nothing to do
    }

    override fun getKiOfPageFail(msg: String?) {
        //nothing to do
    }

    private fun bindDataToToolbar(listKiOfUser: KiOfUserResult) {
        var totalKi = 0.toDouble()
        if (listKiOfUser.totalKi != null)
            totalKi += listKiOfUser.totalKi!!

        tvName?.text = listKiOfUser.name
        ImageHelper.loadImage(mContext, imgAvatar, listKiOfUser.linkAvatar, PlaceHolderType.RESTAURANT)
        tvTotalKi?.text = "${totalKi.toInt()} Ki"
    }
}
