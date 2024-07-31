package agv.kaak.vn.kaioken.fragment.info_store


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.InfoStoreActivity
import agv.kaak.vn.kaioken.adapter.CategoryMenuStoreAdapter
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.MenuInfo
import agv.kaak.vn.kaioken.entity.result.DetailStoreResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.bignerdranch.expandablerecyclerview.Model.ParentObject
import kotlinx.android.synthetic.main.fragment_store_menu.*

/**
 * A simple [Fragment] subclass.
 *
 */
class StoreMenuFragment : BaseFragment() {

    private var listMenu: ArrayList<MenuInfo> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_menu, container, false)
    }

    override fun getData() {
        listMenu.clear()
        listMenu.addAll((arguments!!.getSerializable(Constraint.DATA_SEND) as DetailStoreResult).menuInfo!!)
    }

    override fun loadData() {
        initListMenu(lstMenu, listMenu)
    }

    override fun addEvent() {
        //nothing to do
    }

    private fun initListMenu(lst: RecyclerView, source: ArrayList<MenuInfo>) {
        val adapter = CategoryMenuStoreAdapter(mContext, source)
        adapter.setOnFoodClickListener(object : CategoryMenuStoreAdapter.OnFoodClickListener {
            override fun onFoodClick(food: MenuFood) {
                GlobalHelper.showDialogDetailFood(mContext, food)
            }
        })
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        lst.adapter = adapter
        lst.layoutManager = manager
    }
}
