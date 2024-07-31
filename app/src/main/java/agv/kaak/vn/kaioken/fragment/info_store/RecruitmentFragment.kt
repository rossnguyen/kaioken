package agv.kaak.vn.kaioken.fragment.info_store


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.DetailRecruitmentResult
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.GetRecruitmentContract
import agv.kaak.vn.kaioken.mvp.presenter.GetRecruitmentPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.support.v4.view.ViewCompat
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_recruitment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RecruitmentFragment : BaseDialogFragment(), GetRecruitmentContract.View {

    private val getDetailRecruitmentPresentException=GetRecruitmentPresenter(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recruitment, container, false)
    }


    override fun getData() {
        //nothing to do
    }

    override fun loadData() {
        //ViewCompat.setElevation(activityParent.findViewById(R.id.imgCover), resources.getDimension(R.dimen.toolbar_elevation))
        getDetailRecruitmentPresentException.getDetailRecruitment(Constraint.ID_STORE_ORDERING)
    }

    override fun addEvent() {
        ibtnClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun listenSocket() {
        //nothing to do
    }

    override fun offSocket() {
        //nothing to do
    }

    private fun bindData(detailRecruitment: DetailRecruitmentResult){
        tvPosition?.setSubText(detailRecruitment.title)
        tvQuantity?.setSubText("${detailRecruitment.quantity}")
        tvDescription?.setSubText(detailRecruitment.description)
        ImageHelper.loadImage(mContext,imgCover,detailRecruitment.linkImage, PlaceHolderType.IMAGE)
    }

    override fun getDetailRecruitmentSuccess(detailRecruitment: DetailRecruitmentResult) {
        bindData(detailRecruitment)
    }

    override fun getDetailRecruitmentFail(message: String) {
        GlobalHelper.showMessage(mContext,activityParent.resources.getString(R.string.all_get_data_fail_message),true)
        Log.e("****errorRecruitment",message)
    }
}
