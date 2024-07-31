package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.MenuInfo
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_show_menu_item.*

class ShowMenuItemFragment : BaseFragment() {
    companion object {
        val DETAIL_FOOD_SEND = "DETAIL_FOOD_SEND"
    }

    lateinit var listOrder: ArrayList<DetailOrder>
    var source: ArrayList<MenuFood>? = null
    var type: Int = 0

    private lateinit var showMenuItemAdapter: ShowMenuItemAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        activityParent = context as Activity
        mFragmentManager = childFragmentManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        getData()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_menu_item, container, false)
    }

    override fun getData() {
        if (arguments!!.getSerializable(ShowMenuFragment.LIST_ITEM) != null)
            source = arguments!!.getSerializable(ShowMenuFragment.LIST_ITEM) as ArrayList<MenuFood>
        listOrder = arguments!!.getSerializable(ChooseOrderFragment.LIST_ORDER) as ArrayList<DetailOrder>
        type = arguments!!.getInt(ChooseOrderFragment.TYPE)
    }

    override fun loadData() {
        showMenuItemAdapter = ShowMenuItemAdapter(mContext, source, listOrder, type)

        showMenuItemAdapter.onChangeNumberListener = onChangeNumberListener
        showMenuItemAdapter.onShowDialogCartListener = onShowDialogCartListener

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstItem.adapter = showMenuItemAdapter
        lstItem.layoutManager = layoutManager
    }

    override fun addEvent() {
        //nothing to do
    }

    fun doFilter(query: String) {
        val resultSearch: ArrayList<MenuFood> = arrayListOf()
        for (eachFood in source!!) {
            if (eachFood.name!!.toLowerCase().contains(query.toLowerCase()))
                resultSearch.add(eachFood)
        }
        showMenuItemAdapter.filterList(resultSearch)
    }



    fun refresh() {
        showMenuItemAdapter.notifyDataSetChanged()
    }

    var onShowDialogCartListener: ShowMenuItemAdapter.OnShowDialogCartListener? = null
    var onChangeNumberListener: ShowMenuItemAdapter.OnChangeNumberListener? = null
}
