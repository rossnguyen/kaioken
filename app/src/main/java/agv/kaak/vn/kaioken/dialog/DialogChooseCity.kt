package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.CityAdapter
import agv.kaak.vn.kaioken.entity.City
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_city.*
import java.util.ArrayList

class DialogChooseCity(val mcontext: Context, val cityList: ArrayList<City>) : Dialog(mcontext) {
    var onCityClickListener: OnCityClickListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_city)
        toolbar.tvTitle.text = "${mcontext.resources.getString(R.string.map_choose_city)}"
        toolbar.ibtnBack.setImageResource(R.drawable.ic_close)
        initListCity()
        initEvents()
    }

    fun initListCity() {
        val adapter = CityAdapter(mcontext, cityList)
        lstCity.adapter = adapter
        adapter.onItemClickListener = object : CityAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (onCityClickListener != null)
                    onCityClickListener!!.onClick(cityList[position])
                dismiss()
            }
        }
        val layoutManager = LinearLayoutManager(mcontext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        lstCity.layoutManager = layoutManager
    }

    private fun initEvents() {
        toolbar.ibtnBack.setOnClickListener {
            dismiss()
        }
    }

    interface OnCityClickListener {
        fun onClick(city: City)
    }
}