package agv.kaak.vn.kaioken.fragment.info_store


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.ChooseShareActivity
import agv.kaak.vn.kaioken.activity.DirectionActivity
import agv.kaak.vn.kaioken.dialog.DialogDetailPromotion
import agv.kaak.vn.kaioken.entity.result.DetailStoreResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.location.Location
import kotlinx.android.synthetic.main.fragment_store_detail.*
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 *
 */
class StoreDetailFragment : BaseFragment(), View.OnClickListener {

    private lateinit var storeInfo: DetailStoreResult
    var nameStore = ""
    var linkImage = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_detail, container, false)
    }


    override fun getData() {
        storeInfo = arguments!!.getSerializable(Constraint.DATA_SEND) as DetailStoreResult
        nameStore = arguments!!.getString(DialogDetailPromotion.NAME_SEND)
        linkImage = arguments!!.getString(DialogDetailPromotion.LINK_IMAGE_SEND)
    }

    override fun loadData() {
        tvRate?.setContentText(activityParent.resources.getString(R.string.detail_store_rating, storeInfo.rating!!.toFloat()))

        if (Constraint.myLocation != null) {
            val distance = FloatArray(1)
            Location.distanceBetween(
                    Constraint.myLocation!!.latitude,
                    Constraint.myLocation!!.longitude,
                    storeInfo.location!!.latitude,
                    storeInfo.location!!.longitude, distance)

            if (distance[0] > 0) {
                if (distance[0] > 1000)
                    tvAddress?.setSubText("${activityParent.resources.getString(R.string.all_distance)} ${String.format("%.1f", distance[0] / 1000)} ${activityParent.resources.getString(R.string.all_km)}")
                else
                    tvAddress?.setSubText("${activityParent.resources.getString(R.string.all_distance)} ${distance[0].toInt()} ${activityParent.resources.getString(R.string.all_m)}")
            } else
                tvAddress?.setSubText("${activityParent.resources.getString(R.string.all_distance)} ${activityParent.resources.getString(R.string.all_unknown)}")
        }

        tvAddress?.setContentText(storeInfo.address!!)

        setTimeOpen(storeInfo.workingHour)

        tvDescriptionStore.setSubText(storeInfo.description!!)

        tvContact?.setSubText("${storeInfo.phone}")

        if (storeInfo.countRecuitment == 0)
            tvRecruitment?.setSubText("${activityParent.resources.getString(R.string.detail_store_recruitment_none)}")
        else
            tvRecruitment?.setSubText("${activityParent.resources.getString(R.string.detail_store_recruitment_count)}: ${storeInfo.countRecuitment}")

        //show promotion if have
        if (storeInfo.promotion != null) {
            tvPromotion?.visibility = View.VISIBLE
            tvPromotion?.setContentText(storeInfo.promotion?.reason!!)
        }

    }

    override fun addEvent() {
        tvAddress.setOnClickListener(this)
        tvRecruitment.setOnClickListener(this)
        tvShare.setOnClickListener(this)
        tvPromotion.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tvAddress -> showDialogDirection(storeInfo.location?.latitude, storeInfo.location?.longitude)
            tvRecruitment -> if (storeInfo.countRecuitment!! > 0)
                showDialogRecruitment()
            tvShare -> showDialogChooseShare()
            tvPromotion -> showDialogDetailPromotion(storeInfo.id, nameStore, linkImage)
        }
    }

    private fun showDialogDirection(lat: Double?, lng: Double?) {
        if (GlobalHelper.gpsIsOn(activityParent) && GlobalHelper.networkIsConnected(activityParent)) {
            if (lat != null && lng != null && lat != 0.toDouble() && lng != 0.toDouble()) {
                val intentDirection = Intent(activityParent, DirectionActivity::class.java)
                intentDirection.putExtra(Constraint.DATA_SEND, "$lat,$lng")
                intentDirection.putExtra(DirectionActivity.NAME_STORE_SEND, storeInfo.name)
                intentDirection.putExtra(DirectionActivity.ADDRESS_STORE_SEND, storeInfo.address)
                intentDirection.putExtra(DirectionActivity.IMAGE_STORE_SEND, storeInfo.cover)
                startActivity(intentDirection)
            }
        }
    }

    private fun showDialogRecruitment() {
        RecruitmentFragment().show(mFragmentManager, "Recruitment")
    }

    private fun showDialogChooseShare() {
        //startActivity(Intent(activityParent, ChooseShareActivity::class.java))
        val chooseShareModelBottomSheet = ChooseShareFragment()
        chooseShareModelBottomSheet.show(mFragmentManager, chooseShareModelBottomSheet.tag)
    }

    private fun showDialogDetailPromotion(pageId: Int, name: String, linkImage: String) {
        val bundle = Bundle()
        bundle.putInt(Constraint.DATA_SEND, pageId)
        bundle.putString(DialogDetailPromotion.NAME_SEND, name)
        bundle.putString(DialogDetailPromotion.LINK_IMAGE_SEND, linkImage)

        val dialogDetailPromotion = DialogDetailPromotion()
        dialogDetailPromotion.arguments = bundle

        dialogDetailPromotion.show(mFragmentManager, "PROMOTION")
    }

    private fun setTimeOpen(timeOpen: String?) {
        if (timeOpen == null) {
            tvTimeOpen.setSubText(activityParent.resources.getString(R.string.detail_store_info_not_set_up))
        } else {
            try {
                val timeStart = timeOpen.split("-")[0].trim()
                val hourStart = (timeStart.split(":")[0]).toInt()
                val minutesStart = (timeStart.split(":")[1]).toInt()

                val timeEnd = timeOpen.split("-")[1].trim()
                val hourEnd = (timeEnd.split(":")[0]).toInt()
                val minutesEnd = (timeEnd.split(":")[1]).toInt()

                val format2Digit = DecimalFormat("00")
                tvTimeOpen.setSubText("${format2Digit.format(hourStart)}:${format2Digit.format(minutesStart)} - ${format2Digit.format(hourEnd)}:${format2Digit.format(minutesEnd)}")
            } catch (ex: Exception) {
                tvTimeOpen.setSubText(activityParent.resources.getString(R.string.detail_store_info_not_set_up))
            }
        }


    }
}

