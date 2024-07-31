package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.InfoStoreActivity
import agv.kaak.vn.kaioken.adapter.CategoryStoreAdapter
import agv.kaak.vn.kaioken.adapter.StoreOfferAdapter
import agv.kaak.vn.kaioken.dialog.DialogChooseCity
import agv.kaak.vn.kaioken.dialog.DialogDetailPromotion
import agv.kaak.vn.kaioken.entity.City
import agv.kaak.vn.kaioken.entity.Store
import agv.kaak.vn.kaioken.entity.result.CategoryStoreResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CategoryStoreContract
import agv.kaak.vn.kaioken.mvp.contract.ListCityContract
import agv.kaak.vn.kaioken.mvp.presenter.CategoryStorePresenter
import agv.kaak.vn.kaioken.mvp.presenter.ListCityPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.widget.GridSpacingDecoration
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.SearchView
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_home_top.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeTopFragment : BaseFragment(),
        CategoryStoreContract.View,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        CompoundButton.OnCheckedChangeListener,
        ListCityContract.View {
    companion object {
        var categoryId = 0
        var categoryName = ""
        var storeHaveDelivery: Boolean = false
        var storeIsOpening = false
        var listProvince: ArrayList<City> = arrayListOf()
        var provinceChoosed = ""
        var distanceChoosed = ""
        var distance = 10000
        var locationProvince: LatLng? = null
    }

    private val valuesDistance = intArrayOf(500, 1000, 3000, 5000, 10000, 50000)
    private lateinit var stringDistanceSource: Array<String>

    private val categoryStorePresenter = CategoryStorePresenter(this)
    private val listCityPresenter = ListCityPresenter(this)

    private var listCategoryStore: ArrayList<CategoryStoreResult> = arrayListOf()
    private val listOffer: ArrayList<Store> = arrayListOf()
    private val listFollow: ArrayList<Store> = arrayListOf()
    private lateinit var adapterOffer: StoreOfferAdapter
    private lateinit var adapterFollow: StoreOfferAdapter
    private var adapterCategoryStore: CategoryStoreAdapter? = null
    var choosedDistance: Int = 4
    private var adjustIsShowing = false
    private var mapIsShowing = true

    var nothingOfferToLoadMore = false
    var isLoadingMoreOffer = false

    var nothingLikedToLoadMore = false
    var isLoadingMoreLiked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutTitle()
        layoutTitle.currentItem = 1
        initListFollow()
        initListOffer()
        setDefaultValueForAdjust()
        initAdjust()
        stringDistanceSource = resources.getStringArray(R.array.distance_search_text)

        loadingCategory.visibility = View.VISIBLE
        categoryStorePresenter.getListCategoryStore()
    }

    override fun getData() {
        //nothing to get
    }

    override fun loadData() {
        nothingOfferToLoadMore = false
        nothingLikedToLoadMore = false
    }

    override fun addEvent() {
        ibtnAdjust.setOnClickListener(this)
        ibtnMode.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)
        refreshOffer.setOnRefreshListener(this)
        refreshLiked.setOnRefreshListener(this)
        switchOpen.setOnCheckedChangeListener(this)
        switchDelivery.setOnCheckedChangeListener(this)
        tvDistance.setOnClickListener(this)
        tvProvince.setOnClickListener(this)


        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (onSearchSubmit != null && GlobalHelper.gpsIsOn(activityParent))
                    onSearchSubmit!!.onSubmit(query!!,
                            categoryId,
                            locationProvince!!,
                            distance,
                            1,
                            if (storeHaveDelivery) 1 else 0,
                            if (storeIsOpening) 1 else 0,
                            1,
                            0,
                            Constraint.LIMIT)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
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

    private fun initLayoutTitle() {
        //add item
        layoutTitle.addItem(AHBottomNavigationItem("Gợi ý", activityParent.getDrawable(R.drawable.ic_offer)))
        layoutTitle.addItem(AHBottomNavigationItem("Bản đồ", activityParent.getDrawable(R.drawable.ic_around_me)))
        layoutTitle.addItem(AHBottomNavigationItem("Yêu thích", activityParent.getDrawable(R.drawable.ic_list_follow)))

        //setup display
        layoutTitle.defaultBackgroundColor = ContextCompat.getColor(activityParent, R.color.colorMasterialGrey_2)

        //layoutTitle.accentColor=activityParent.resources.getColor(R.color.colorAccent)
        layoutTitle.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
        layoutTitle.currentItem = -1
        ViewCompat.setElevation(layoutTitle, 0.toFloat())
        ViewCompat.setElevation(layoutToolbar, resources.getDimension(R.dimen.all_masterial_elevation_switch))

        //setup listener
        layoutTitle.setOnTabSelectedListener { position, _ ->
            hideAllTab()
            when (position) {
                0 -> {
                    layoutOffer.visibility = View.VISIBLE
                    if (listOffer.isEmpty())
                        reloadListOffer()
                    MapFragment.mapIsShowing = false
                }

                1 -> {
                    layoutBesideYou.visibility = View.VISIBLE
                    adjustIsShowing = false
                    MapFragment.mapIsShowing = true
                }

                2 -> {
                    layoutLike.visibility = View.VISIBLE
                    if (listFollow.isEmpty())
                        reloadListFollow()
                    MapFragment.mapIsShowing = false
                }
            }

            return@setOnTabSelectedListener true
        }
    }

    private fun hideAllTab() {
        layoutExplanation.visibility = View.GONE
        layoutOffer.visibility = View.GONE
        layoutBesideYou.visibility = View.GONE
        layoutAdjust.visibility = View.GONE
        layoutLike.visibility = View.GONE
    }

    /**
     * init layout adjust
     */
    private fun initAdjust() {
        switchOpen.isChecked = storeIsOpening
        switchDelivery.isChecked = storeHaveDelivery
        tvProvince.text = provinceChoosed
        tvDistance.text = activityParent.resources.getString(R.string.format_distance_x, "$distanceChoosed")
    }

    override fun getCategoryRestaurantSuccess(data: ArrayList<CategoryStoreResult>) {
        loadingCategory?.visibility = View.GONE
        showListCategoryRestaurant(data)
    }

    override fun getCategoryRestaurantFail(message: String?) {
        loadingCategory?.visibility = View.GONE
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.map_get_list_category_fail), true)
        Log.e("****errorGetCategory", message)
    }

    override fun showListCategoryRestaurant(source: ArrayList<CategoryStoreResult>) {
        listCategoryStore.clear()
        listCategoryStore.addAll(source)
        adapterCategoryStore = CategoryStoreAdapter(mContext, listCategoryStore)
        gridCategoryRestaurant?.adapter = adapterCategoryStore
        adapterCategoryStore?.onItemCategoryStoreClickListener=object :CategoryStoreAdapter.OnItemCategoryStoreClickListener{
            override fun onClick(position: Int) {
                for (i in 0 until listCategoryStore.size) {
                    if (i == position) {
                        gridCategoryRestaurant.getChildAt(i).background = ContextCompat.getDrawable(activityParent, R.drawable.custom_background_5_radius_transparent_border_orange)
                    } else {
                        gridCategoryRestaurant.getChildAt(i).setBackgroundResource(0)
                    }

                }

                if (GlobalHelper.gpsIsOn(activityParent)) {
                    val category = listCategoryStore[position]
                    categoryId = category.id
                    categoryName = category.name!!

                    layoutBesideYou?.visibility = View.GONE

                    if (onSearchSubmit != null)
                        onSearchSubmit!!.onSubmit(svSearch.query.toString(),
                                categoryId,
                                locationProvince!!,
                                distance,
                                1,
                                if (storeHaveDelivery) 1 else 0,
                                if (storeIsOpening) 1 else 0,
                                1,
                                0,
                                Constraint.LIMIT)
                    svSearch.queryHint = category.name
                }
            }
        }

        val layoutManager=GridLayoutManager(mContext,5)
        gridCategoryRestaurant?.layoutManager=layoutManager
        gridCategoryRestaurant?.addItemDecoration(GridSpacingDecoration(0,5))

        /*gridCategoryRestaurant?.setOnItemClickListener { parent, view, position, id ->
            for (i in 0 until listCategoryStore.size) {
                if (i == position) {
                    gridCategoryRestaurant.getChildAt(i).background = ContextCompat.getDrawable(activityParent, R.drawable.custom_background_5_radius_transparent_border_orange)
                    ViewCompat.setElevation(gridCategoryRestaurant.getChildAt(i), activityParent.resources.getDimensionPixelOffset(R.dimen.all_masterial_elevation_card_pickup).toFloat())
                } else {
                    gridCategoryRestaurant.getChildAt(i).setBackgroundResource(0)
                    ViewCompat.setElevation(gridCategoryRestaurant.getChildAt(i), 0f)
                }

            }


            if (GlobalHelper.gpsIsOn(activityParent)) {
                val category = listCategoryStore[position]
                categoryId = category.id
                categoryName = category.name!!

                layoutBesideYou?.visibility = View.GONE

                if (onSearchSubmit != null)
                    onSearchSubmit!!.onSubmit(svSearch.query.toString(),
                            categoryId,
                            locationProvince!!,
                            distance,
                            1,
                            if (storeHaveDelivery) 1 else 0,
                            if (storeIsOpening) 1 else 0,
                            1,
                            0,
                            Constraint.LIMIT)
                svSearch.queryHint = category.name
            }
        }*/
    }

    fun getListStoreFollowSuccess(data: ArrayList<Store>) {
        loadingLike?.visibility = View.GONE
        loadingMoreLike?.visibility = View.GONE
        refreshLiked?.isRefreshing = false

        if (data.isEmpty() && !isLoadingMoreLiked) {
            layoutEmptyLike?.visibility = View.VISIBLE
            listFollow.clear()
            adapterFollow.notifyDataSetChanged()
        } else {
            layoutEmptyLike?.visibility = View.GONE
            if (!isLoadingMoreLiked)
                listFollow.clear()
            listFollow.addAll(data)
            adapterFollow.notifyDataSetChanged()
        }

        if (data.size < Constraint.LIMIT)
            nothingLikedToLoadMore = true
        isLoadingMoreLiked = false
    }

    fun getListStoreFollowFail(message: String?) {
        loadingLike?.visibility = View.GONE
        loadingMoreLike?.visibility = View.GONE
        refreshLiked?.isRefreshing = false
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.map_get_list_follow_fail), true)
        Log.e("****errorGetListFollow", message)
        isLoadingMoreLiked = false
        nothingLikedToLoadMore = false
    }

    fun getListStoreOfferSuccess(data: ArrayList<Store>) {
        loadingOffer?.visibility = View.GONE
        loadingMoreOffer?.visibility = View.GONE
        refreshOffer?.isRefreshing = false

        if (data.isEmpty() && !isLoadingMoreOffer) {
            layoutEmptyOffer?.visibility = View.VISIBLE
            listOffer.clear()
            adapterOffer.notifyDataSetChanged()
        } else {
            layoutEmptyOffer?.visibility = View.GONE
            if (!isLoadingMoreOffer)
                listOffer.clear()
            listOffer.addAll(data)
            adapterOffer.notifyDataSetChanged()
        }

        if (data.size < Constraint.LIMIT)
            nothingOfferToLoadMore = true
        isLoadingMoreOffer = false
    }

    fun getListStoreOfferFail(message: String?) {
        loadingOffer?.visibility = View.GONE
        refreshOffer?.isRefreshing = false
        loadingMoreOffer?.visibility = View.GONE
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.map_get_list_offer_fail), true)
        Log.e("****errorGetListOffer", message)
        isLoadingMoreOffer = false
        nothingOfferToLoadMore = false
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

    private fun initListOffer() {
        adapterOffer = StoreOfferAdapter(mContext, listOffer)
        lstOffer?.adapter = adapterOffer
        adapterOffer.onStoreClickListener = object : StoreOfferAdapter.OnStoreClickListener {
            override fun onClick(holder: StoreOfferAdapter.ViewHolder, storeData: Store) {
                val intent = Intent(activityParent, InfoStoreActivity::class.java)
                intent.putExtra(InfoStoreActivity.ID_STORE_SEND, storeData.id)
                intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, storeData.name)

                //Convert to byte array
                //because width of image too large
                //so skip send image
                /*val stream = ByteArrayOutputStream()
                storeData.imageBitMap!!.compress(Bitmap.CompressFormat.PNG, 100, stream);
                val byteArray = stream.toByteArray()
                intent.putExtra(InfoStoreActivity.IMAGE_STORE_SEND, byteArray)*/

                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activityParent,

                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        Pair<View, String>(holder.tvName,
                                InfoStoreActivity.NAME_MAP_TRANSITION),
                        Pair<View, String>(holder.imgCover,
                                InfoStoreActivity.COVER_MAP_TRANSITION))

                //startActivity(intent, activityOptions.toBundle())
                startActivity(intent)
            }

            override fun onDiscountClick(pageId: Int?, name: String, linkImage: String?) {
                //Waiting for a Nam
                if (pageId == null)
                    return
                val dialogPromotion = DialogDetailPromotion()
                val bundle = Bundle()
                bundle.putInt(Constraint.DATA_SEND, pageId)
                bundle.putString(DialogDetailPromotion.NAME_SEND, name)
                bundle.putString(DialogDetailPromotion.LINK_IMAGE_SEND, linkImage)
                dialogPromotion.arguments = bundle
                dialogPromotion.show(mFragmentManager, "PROMOTION")

                //GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_feature_is_developing), true)
            }
        }

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        lstOffer?.layoutManager = layoutManager

        lstOffer?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (!nothingOfferToLoadMore && (layoutManager.findLastCompletelyVisibleItemPosition() == listOffer.size - 1) && onRequestGetListStore != null) {
                    onRequestGetListStore!!.requestListOffer(listOffer.size, Constraint.LIMIT)
                    isLoadingMoreOffer = true
                    loadingMoreOffer?.visibility = View.VISIBLE
                }
            }
        })

        lstOffer?.layoutManager = layoutManager
        lstOffer?.adapter = adapterOffer
    }

    private fun initListFollow() {
        adapterFollow = StoreOfferAdapter(mContext, listFollow)
        lstLike?.adapter = adapterFollow
        adapterFollow.onStoreClickListener = object : StoreOfferAdapter.OnStoreClickListener {
            override fun onClick(holder: StoreOfferAdapter.ViewHolder, storeData: Store) {
                val intent = Intent(activityParent, InfoStoreActivity::class.java)
                intent.putExtra(InfoStoreActivity.ID_STORE_SEND, storeData.id)
                intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, storeData.name)
                //Convert to byte array
                //because width of image too large
                //so skip send image
                /*val stream = ByteArrayOutputStream()
                storeData.imageBitMap!!.compress(Bitmap.CompressFormat.PNG, 100, stream);
                val byteArray = stream.toByteArray()
                intent.putExtra(InfoStoreActivity.IMAGE_STORE_SEND, byteArray)*/

                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activityParent,

                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        Pair<View, String>(holder.tvName,
                                InfoStoreActivity.NAME_MAP_TRANSITION),
                        Pair<View, String>(holder.imgCover,
                                InfoStoreActivity.COVER_MAP_TRANSITION))

                //startActivity(intent, activityOptions.toBundle())
                startActivity(intent)
            }

            override fun onDiscountClick(pageId: Int?, name: String, linkImage: String?) {
                //Waiting for a Nam
                if (pageId == null)
                    return
                val dialogPromotion = DialogDetailPromotion()
                val bundle = Bundle()
                bundle.putInt(Constraint.DATA_SEND, pageId)
                bundle.putString(DialogDetailPromotion.NAME_SEND, name)
                bundle.putString(DialogDetailPromotion.LINK_IMAGE_SEND, linkImage)
                dialogPromotion.arguments = bundle
                dialogPromotion.show(mFragmentManager, "PROMOTION")

                //GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_feature_is_developing), true)
            }
        }

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        lstLike?.layoutManager = layoutManager

        lstLike?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (!nothingLikedToLoadMore && (layoutManager.findLastCompletelyVisibleItemPosition() == listFollow.size - 1) && onRequestGetListStore != null) {
                    onRequestGetListStore!!.requestListLiked(listFollow.size, Constraint.LIMIT)
                    isLoadingMoreLiked = true
                    loadingMoreLike?.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun reloadListOffer() {
        if (onRequestGetListStore != null) {
            loadingOffer?.visibility = View.VISIBLE
            isLoadingMoreOffer = false
            nothingOfferToLoadMore = false
            onRequestGetListStore!!.requestListOffer(0, Constraint.LIMIT)

        }
    }

    private fun reloadListFollow() {
        if (onRequestGetListStore != null) {
            loadingLike?.visibility = View.VISIBLE
            isLoadingMoreLiked = false
            nothingLikedToLoadMore = false
            onRequestGetListStore!!.requestListLiked(0, Constraint.LIMIT)
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            ibtnAdjust -> {
                hideAllTab()
                adjustIsShowing = !adjustIsShowing
                if (adjustIsShowing)
                    layoutAdjust.visibility = View.VISIBLE
                else {
                    layoutAdjust.visibility = View.GONE
                    layoutTitle.currentItem = 1
                }

            }

            btnConfirm -> {
                layoutAdjust.visibility = View.GONE
                layoutTitle.currentItem = 1
            }

            tvProvince -> {
                if (listProvince.isEmpty())
                    getListProvince()
                else
                    showChooseCity()
            }

            tvDistance -> showChooseDistance()

            ibtnMode -> toggleModeShow()
        }
    }

    /**
     * show dialog choose distance
     */
    private fun showChooseDistance() {
        AlertDialog.Builder(activityParent)
                .setSingleChoiceItems(stringDistanceSource, choosedDistance) { dialog, which ->
                    //update distance search
                    choosedDistance = which
                    distance = valuesDistance[which]
                    distanceChoosed = stringDistanceSource[which]

                    //reload adjust
                    initAdjust()

                    dialog.dismiss()
                }
                .show()
    }

    fun updateDistanceSearch(positon: Int) {
        choosedDistance = positon
        distance = valuesDistance[positon]
        distanceChoosed = stringDistanceSource[positon]
        initAdjust()
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

    fun hideListCategory() {
        layoutBesideYou?.visibility = View.GONE
    }

    /**
     * show dialog choose province
     */
    private fun getListProvince() {
        listCityPresenter.getListCity()
    }

    private fun toggleModeShow(){
        mapIsShowing=!mapIsShowing
        if(mapIsShowing){
            ibtnMode.setImageResource(R.drawable.ic_adjust)
            ibtnMode.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorBlackPrimary))
        }else{
            ibtnMode.setImageResource(R.drawable.ic_see_mode_map)
            ibtnMode.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorBlackPrimary))
        }
    }


    override fun onRefresh() {
        when (layoutTitle.currentItem) {
            0 -> {
                reloadListOffer()
                refreshOffer?.isRefreshing = false
            }
            2 -> {
                reloadListFollow()
                refreshLiked?.isRefreshing = false
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView) {
            switchOpen -> storeIsOpening = isChecked
            switchDelivery -> storeHaveDelivery = isChecked
        }
    }

    var onSearchSubmit: OnSearchSubmit? = null

    interface OnSearchSubmit {
        fun onSubmit(query: String,
                     categoryId: Int,
                     location: LatLng,
                     distance: Int,
                     haveKaak: Int,
                     haveDelivery: Int,
                     isOpening: Int,
                     isRating: Int,
                     offset: Int,
                     limit: Int)
    }

    var onRequestGetListStore: OnRequestGetListStore? = null

    interface OnRequestGetListStore {
        fun requestListOffer(offset: Int, limit: Int)
        fun requestListLiked(offset: Int, limit: Int)
    }

    fun getYCoordinateOfLayoutTitle(): Int {
        var location = IntArray(2)
        layoutTitle.getLocationInWindow(location)
        return location[1]
    }

    fun getYCoordinateOfButtonAdjut(): Int {
        var location = IntArray(2)
        ibtnAdjust.getLocationInWindow(location)
        return location[1]
    }

    fun getXCoordinateOfButtonAdjut(): Int {
        var location = IntArray(2)
        ibtnAdjust.getLocationInWindow(location)
        return location[0]
    }

    fun getButtonAdjust(): ImageButton {
        return ibtnAdjust
    }
}
