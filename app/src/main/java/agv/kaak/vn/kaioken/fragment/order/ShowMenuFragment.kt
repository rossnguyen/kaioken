package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.adapter.ViewPagerAdapter
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.MenuInfo
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.DialogInterface
import kotlinx.android.synthetic.main.fragment_show_menu.*
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ShowMenuFragment : BaseFragment() {
    companion object {
        const val LIST_ITEM = "LIST_ITEM"
    }

    lateinit var listOrder: ArrayList<DetailOrder>
    lateinit var source: ArrayList<MenuInfo>
    var type: Int? = 0

    var indexPager = 0

    private lateinit var viewpagerAdapter: ViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_menu, container, false)
    }

    override fun getData() {
        source = arguments!!.getSerializable(ChooseOrderFragment.SOURCE) as ArrayList<MenuInfo>
        listOrder = arguments!!.getSerializable(ChooseOrderFragment.LIST_ORDER) as ArrayList<DetailOrder>
        type = arguments!!.getInt(ChooseOrderFragment.TYPE)
    }

    override fun loadData() {
        initViewPager(source, listOrder, type!!)
    }

    override fun addEvent() {
        //nothing to do
    }

    private fun initViewPager(source: ArrayList<MenuInfo>, listOrder: ArrayList<DetailOrder>, type: Int) {
        viewpagerAdapter = ViewPagerAdapter(mFragmentManager)

        for (eachMenu: MenuInfo in source) {
            val fragmentShowMenuItem = ShowMenuItemFragment()
            val bundle = Bundle()
            bundle.putSerializable(LIST_ITEM, eachMenu.items as ArrayList)
            bundle.putSerializable(ChooseOrderFragment.LIST_ORDER, listOrder)
            bundle.putSerializable(ChooseOrderFragment.TYPE, type)
            fragmentShowMenuItem.arguments = bundle


            fragmentShowMenuItem.onChangeNumberListener = onChangeNumberListener
            fragmentShowMenuItem.onShowDialogCartListener = object : ShowMenuItemAdapter.OnShowDialogCartListener {
                override fun onShow() {
                    indexPager = layoutPager.currentItem
                    val fragmentCart = CartFragment()
                    val bundle = Bundle()
                    bundle.putSerializable(Constraint.DATA_SEND, listOrder)
                    fragmentCart.arguments = bundle
                    fragmentCart.isCancelable = false
                    fragmentCart.isExpand = listOrder.size >= 4
                    fragmentCart.onDismissListener = DialogInterface.OnDismissListener {
                        reloadListMenu()
                        onChangeNumberListener.onChangeNumberOrder(-1, -1, -1)
                    }

                    fragmentCart.show(mFragmentManager, fragmentCart.tag)
                }
            }

            viewpagerAdapter.addFragment(fragmentShowMenuItem, eachMenu.categoryName!!)
        }

        layoutPager.adapter = viewpagerAdapter
        layoutTitle?.setupWithViewPager(layoutPager)

        layoutTitle?.post(runAbleConfigTitle)
    }

    private val runAbleConfigTitle = Runnable {
        if (layoutTitle?.width!!.plus(10) < activityParent.resources.displayMetrics.widthPixels) {
            layoutTitle?.tabMode = TabLayout.MODE_FIXED

            val mParams = layoutTitle?.layoutParams
            mParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutTitle?.layoutParams = mParams
        } else {
            layoutTitle?.tabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun reloadListMenu() {
        (viewpagerAdapter.getItem(indexPager) as ShowMenuItemFragment).refresh()

        //when current pager is start -> refresh end
        //use try/catch two level because maybe pager have only one page
        try {
            (viewpagerAdapter.getItem(indexPager - 1) as ShowMenuItemFragment).refresh()
        } catch (exception: Exception) {
            try {
                (viewpagerAdapter.getItem(viewpagerAdapter.count - 1) as ShowMenuItemFragment).refresh()
            } catch (abc: Exception) {
            }
        }

        //when current pager is end -> refresh start
        //use try/catch two level because maybe pager have only one page
        try {
            (viewpagerAdapter.getItem(indexPager + 1) as ShowMenuItemFragment).refresh()
        } catch (exception: Exception) {
            try {
                (viewpagerAdapter.getItem(0) as ShowMenuItemFragment).refresh()
            } catch (abc: Exception) {
            }
        }
    }

    lateinit var onChangeNumberListener: ShowMenuItemAdapter.OnChangeNumberListener
}
