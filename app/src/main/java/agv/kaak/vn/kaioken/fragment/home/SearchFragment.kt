package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.CategoryStoreAdapter
import agv.kaak.vn.kaioken.entity.result.CategoryStoreResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CategoryStoreContract
import agv.kaak.vn.kaioken.mvp.presenter.CategoryStorePresenter
import agv.kaak.vn.kaioken.widget.GridSpacingDecoration
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.SearchView
import kotlinx.android.synthetic.main.fragment_search.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class SearchFragment : BaseFragment(), CategoryStoreContract.View {

    companion object {
        var categoryId = 2
        var categoryName = "Coffee"
    }

    private var listCategoryStore: ArrayList<CategoryStoreResult> = arrayListOf()
    private var adapterCategoryStore: CategoryStoreAdapter? = null

    val categoryStorePresenter = CategoryStorePresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategory()
        categoryStorePresenter.getListCategoryStore()
        hideKeyboard(svSearch)
    }

    private fun initCategory() {
        adapterCategoryStore = CategoryStoreAdapter(mContext, listCategoryStore)
        adapterCategoryStore?.onItemCategoryStoreClickListener=object :CategoryStoreAdapter.OnItemCategoryStoreClickListener{
            override fun onClick(position: Int) {
                for (i in 0 until listCategoryStore.size) {
                    if (i == position) {
                        gridCategory.getChildAt(i).background = ContextCompat.getDrawable(activityParent, R.drawable.custom_background_5_radius_transparent_border_orange)
                    } else {
                        gridCategory.getChildAt(i).setBackgroundResource(0)
                    }
                }


                if (GlobalHelper.gpsIsOn(activityParent)) {
                    val category = listCategoryStore[position]
                    categoryId = category.id
                    categoryName = category.name!!

                    if (onSearchFragmentCallBack != null)
                        onSearchFragmentCallBack!!.onCategoryChoosed(svSearch.query.trim().toString(), category.id, category.name)
                }
            }
        }

        gridCategory?.adapter = adapterCategoryStore

        val layoutManager = GridLayoutManager(mContext, 5)
        gridCategory?.layoutManager = layoutManager
        gridCategory?.addItemDecoration(GridSpacingDecoration(0, 5))

        /*gridCategory?.setOnItemClickListener { parent, view, position, id ->
            for (i in 0 until listCategoryStore.size) {
                if (i == position) {
                    gridCategory.getChildAt(i).background = ContextCompat.getDrawable(activityParent, R.drawable.custom_background_5_radius_transparent_border_orange)
                    ViewCompat.setElevation(gridCategory.getChildAt(i), activityParent.resources.getDimensionPixelOffset(R.dimen.all_masterial_elevation_card_pickup).toFloat())
                } else {
                    gridCategory.getChildAt(i).setBackgroundResource(0)
                    ViewCompat.setElevation(gridCategory.getChildAt(i), 0f)
                }
            }


            if (GlobalHelper.gpsIsOn(activityParent)) {
                val category = listCategoryStore[position]
                categoryId = category.id
                categoryName = category.name!!

                if (onSearchFragmentCallBack != null)
                    onSearchFragmentCallBack!!.onCategoryChoosed(svSearch.query.trim().toString(), category.id, category.name)
            }

        }*/
    }

    override fun getData() {
        //nothing to get
    }

    override fun loadData() {

    }

    override fun addEvent() {
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (onSearchFragmentCallBack != null)
                    onSearchFragmentCallBack!!.onSummit(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        layoutSpace.setOnClickListener {
            if (onSearchFragmentCallBack != null)
                onSearchFragmentCallBack!!.onSpaceClick()
        }

        ibtnBack.setOnClickListener {
            if (onSearchFragmentCallBack != null)
                onSearchFragmentCallBack!!.onBackClick()
        }
    }

    override fun getCategoryRestaurantSuccess(data: java.util.ArrayList<CategoryStoreResult>) {
        loadingCategory?.visibility = View.GONE
        listCategoryStore.clear()
        listCategoryStore.addAll(data)
        adapterCategoryStore?.notifyDataSetChanged()
    }

    override fun getCategoryRestaurantFail(message: String?) {
        loadingCategory?.visibility = View.GONE
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.map_get_list_category_fail), true)
        Log.e("****errorGetCategory", message)
    }

    override fun showListCategoryRestaurant(source: java.util.ArrayList<CategoryStoreResult>) {

    }

    var onSearchFragmentCallBack: OnSearchFragmentCallBack? = null

    interface OnSearchFragmentCallBack {
        fun onSummit(query: String?)
        fun onCategoryChoosed(query: String?, idCategory: Int, nameCategory: String?)
        fun onBackClick()
        fun onSpaceClick()
    }

    fun focusToSearch() {
        svSearch.requestFocusFromTouch()
        showKeyboard()
    }

    fun notifyDataSetChanged() {
        adapterCategoryStore?.notifyDataSetChanged()
    }

    fun setQuery(query: String) {
        svSearch.setQuery(query, false)
    }
}
