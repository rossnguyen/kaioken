package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.dialog.DialogChooseCity
import agv.kaak.vn.kaioken.entity.City
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.ListCityContract
import agv.kaak.vn.kaioken.mvp.presenter.ListCityPresenter
import android.app.AlertDialog
import android.util.Log
import android.widget.CompoundButton
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_adjust.*

/**
 * A simple [Fragment] subclass.
 *
 */
class AdjustFragment : BaseFragment(), ListCityContract.View, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    companion object {
        var storeHaveDelivery: Boolean = false
        var storeIsOpening = false
        var listProvince: ArrayList<City> = arrayListOf()
        var provinceChoosed = ""
        var distanceChoosed = "10km"
        var distance = 10000
        var locationProvince: LatLng? = null
    }

    private val valuesDistance = intArrayOf(500, 1000, 3000, 5000, 10000, 50000)
    private lateinit var stringDistanceSource: Array<String>
    var choosedDistance: Int = 4

    private val listCityPresenter = ListCityPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adjust, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDefaultValueForAdjust()
        initAdjust()
        stringDistanceSource = resources.getStringArray(R.array.distance_search_text)

        addEvent()
    }

    override fun getData() {

    }

    override fun loadData() {

    }

    override fun addEvent() {
        btnConfirm.setOnClickListener(this)
        switchOpen.setOnCheckedChangeListener(this)
        switchDelivery.setOnCheckedChangeListener(this)
        tvDistance.setOnClickListener(this)
        tvProvince.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tvProvince -> {
                if (listProvince.isEmpty())
                    getListProvince()
                else
                    showChooseCity()
            }

            tvDistance -> showChooseDistance()
            btnConfirm -> callBackConfirmClickForMap()
        }

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView) {
            switchOpen -> storeIsOpening = isChecked
            switchDelivery -> storeHaveDelivery = isChecked
        }
    }

    override fun getListCitySuccess(listCity: ArrayList<City>) {
        //add first item is current location
        if (GlobalHelper.getLocationFromSharePreference(activityParent) != null) {
            val city = City()
            city.city = activityParent.resources.getString(R.string.all_your_location)
            city.id = "-1"
            city.latitude = "${GlobalHelper.getLocationFromSharePreference(activityParent)!!.latitude}"
            city.longitude = "${GlobalHelper.getLocationFromSharePreference(activityParent)!!.longitude}"
            listProvince.add(city)
        }

        //add list province to source
        listProvince.addAll(listCity)
        showChooseCity()
    }

    override fun getListCityFail(msg: String?) {
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.map_get_list_city_fail), true)
        Log.e("****GetListCity", msg)
    }

    private fun showChooseCity() {
        val dialogChooseCity = DialogChooseCity(mContext, listProvince)
        dialogChooseCity.onCityClickListener = object : DialogChooseCity.OnCityClickListener {
            override fun onClick(city: City) {
                provinceChoosed = city.city!!
                locationProvince = LatLng(city.latitude!!.toDouble(), city.longitude!!.toDouble())
                tvProvince.text = city.city
            }
        }
        dialogChooseCity.show()
    }

    private fun showChooseDistance() {
        AlertDialog.Builder(activityParent)
                .setSingleChoiceItems(stringDistanceSource, choosedDistance) { dialog, which ->
                    //update distance search
                    choosedDistance = which
                    distance = valuesDistance[which]
                    distanceChoosed = stringDistanceSource[which]
                    if (onAdjustValueChange != null)
                        onAdjustValueChange!!.onDistanceChange(distanceChoosed)

                    //reload adjust
                    initAdjust()

                    dialog.dismiss()
                }
                .show()
    }

    private fun getListProvince() {
        listCityPresenter.getListCity()
    }

    private fun setDefaultValueForAdjust() {
        if (provinceChoosed == "")
            provinceChoosed = activityParent.resources.getString(R.string.all_your_location)
        if (locationProvince == null)
            locationProvince = GlobalHelper.getLocationFromSharePreference(activityParent)
        if (distanceChoosed == "") {
            distanceChoosed = "10km"
            choosedDistance = 4
        }
    }

    private fun callBackConfirmClickForMap() {
        if (onConfirmClickListener != null)
            onConfirmClickListener!!.onConfirmClick()
    }

    private fun initAdjust() {
        switchOpen.isChecked = storeIsOpening
        switchDelivery.isChecked = storeHaveDelivery
        tvProvince.text = provinceChoosed
        tvDistance.text = "$distanceChoosed"
        //tvDistance.text = activityParent.resources.getString(R.string.format_distance_x, "$distanceChoosed")
    }

    fun updateDistanceSearch(positon: Int) {
        choosedDistance = positon
        distance = valuesDistance[positon]
        distanceChoosed = stringDistanceSource[positon]
        initAdjust()
    }

    var onConfirmClickListener: OnConfirmCLickListener? = null
    var onAdjustValueChange: OnAdjustValueChange? = null

    interface OnConfirmCLickListener {
        fun onConfirmClick()
    }

    interface OnAdjustValueChange {
        fun onDistanceChange(newDistance: String)
    }
}
