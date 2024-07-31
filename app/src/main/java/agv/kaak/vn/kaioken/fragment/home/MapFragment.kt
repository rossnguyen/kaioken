package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.InfoStoreActivity
import agv.kaak.vn.kaioken.adapter.StoreOfferAdapter
import agv.kaak.vn.kaioken.adapter.StoreOnMapAdapter
import agv.kaak.vn.kaioken.dialog.DialogDetailPromotion
import agv.kaak.vn.kaioken.dialog.DialogGettingLocation
import agv.kaak.vn.kaioken.entity.SerializableLatLng
import agv.kaak.vn.kaioken.entity.Store
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.AsyncGeoCoding
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.StoreOnMapContract
import agv.kaak.vn.kaioken.mvp.presenter.StoreOnMapPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Handler
import android.support.v4.app.*
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.animation.DecelerateInterpolator
import android.widget.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.takusemba.spotlight.OnSpotlightStateChangedListener
import com.takusemba.spotlight.OnTargetStateChangedListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.target.SimpleTarget
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.fragment_map.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 *
 */
class MapFragment : BaseFragment(),
        OnMapReadyCallback,
        StoreOnMapContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {
    companion object {
        var mapIsShowing = true
        var locationSearch: LatLng? = null
    }

    private val COUNT_STORE_GET_FIRST = 30

    private var adjustFragment: AdjustFragment? = null
    private var searchFragment: SearchFragment? = null
    private var mGoogleMap: GoogleMap? = null
    private var mylocation: LatLng? = null
    private var querySearch = ""

    //private var markerMyLocation: Marker? = null
    private val listMarkerStore = ArrayList<Marker>()
    private val listStore: ArrayList<Store> = arrayListOf()
    private var mCircle: Circle? = null
    private lateinit var dialogGettingLocation: DialogGettingLocation

    var adapterStoreHorizontal: StoreOnMapAdapter? = null
    var adapterStoreVertical: StoreOfferAdapter? = null
    var nothingToLoadMore = false
    var isLoadingMore = false
    var mapIsReady = false

    val presenter: StoreOnMapPresenter = StoreOnMapPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setElevation(layoutNote, resources.getDimension(R.dimen.all_masterial_elevation_card_rest))
        //ViewCompat.setElevation(layoutHomeTop, resources.getDimension(R.dimen.all_masterial_elevation_card_pickup))
        dialogGettingLocation = DialogGettingLocation(activityParent)
        //lock gesture right to end
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        getMyLocationFromSaveInstanceState(savedInstanceState)
        initComponent()

        if (isFirstUse())
            Handler().postDelayed({
                showSpotLight()
            }, 1000)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mylocation != null) {
            val location = SerializableLatLng(mylocation!!.latitude, mylocation!!.longitude)
            outState.putSerializable("LOCATION", location)
        }
    }

    private fun initComponent() {
        initListStore()
        initListStoreVertical()
        initSearch()
        initMap()
        initAdjust()
        hideFragmentSearch()
        tvDistance.text = AdjustFragment.distanceChoosed
    }

    override fun getData() {

    }

    override fun loadData() {
        Log.d("****OnLoad", "Co do onload")
        if (!Constraint.backFromInfoStore) {
            nothingToLoadMore = false
            isLoadingMore = false
            mylocation = Constraint.myLocation
        }
    }

    /*fun reloadAfterGetPermissionLocation(){
        loadData()
    }*/

    override fun addEvent() {
        ibtnMode.setOnClickListener(this)
        ibtnAdjust.setOnClickListener(this)
        btnExpandScope?.setOnClickListener(this)
        btnExpandScopeVertical?.setOnClickListener(this)
        layoutSearch.setOnClickListener { showFragmentSearch(tvSearch.text.toString()) }
        refreshStoreVertical.setOnRefreshListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            ibtnMode -> toggleModeShow()
            ibtnAdjust -> showAdjust()
            btnExpandScope -> expandScopeSearch()
            btnExpandScopeVertical -> expandScopeSearch()
        }
    }

    override fun onRefresh() {
        isLoadingMore = false
        nothingToLoadMore = false
        doSearch(querySearch,
                SearchFragment.categoryId,
                locationSearch!!,
                AdjustFragment.distance,
                1,
                if (AdjustFragment.storeHaveDelivery) 1 else 0,
                if (AdjustFragment.storeIsOpening) 1 else 0,
                1,
                0,
                COUNT_STORE_GET_FIRST)
    }

    override fun onMapReady(p0: GoogleMap?) {
        Log.d("${Constraint.TAG_LOG}Map", "map ready")

        mapIsReady = true
        mylocation = Constraint.myLocation
        mGoogleMap = p0
        mGoogleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN

        //move logo map
        moveLogoGoogle()
        moveIconMyLocation(0, mContext.resources.getDimensionPixelOffset(R.dimen.all_margin_short), 0, 0)


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(activityParent,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap?.isMyLocationEnabled = true
            }
        } else {
            //Not in api-23, no need to prompt
            mGoogleMap?.isMyLocationEnabled = true
        }

        //Handle when click on map
        //drawe circle around location choose
        mGoogleMap?.setOnMapClickListener {
            if (lstStoreHorizontal?.visibility == View.VISIBLE) {
                lstStoreHorizontal?.visibility = View.GONE
            } else
                lstStoreHorizontal?.visibility = View.VISIBLE
        }
        mGoogleMap?.setOnMarkerClickListener { marker ->
            //move listview to position
            val position = marker.tag as Int
            if (position != -1)
                lstStoreHorizontal?.smoothScrollToPosition(position)
            false
        }

        mGoogleMap?.setOnMyLocationButtonClickListener {
            if (mylocation != null)
                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mylocation!!.latitude, mylocation!!.longitude),
                        14.toFloat()),
                        1000,
                        null)
            return@setOnMyLocationButtonClickListener true
        }

        mGoogleMap?.setOnInfoWindowClickListener {
            lstStoreHorizontal?.visibility = View.VISIBLE
        }

        if (mylocation == null) {
            showDialogGettingLocation()
        } else {
            Log.d("****LoadStore", "From map ready")
            updateDataWhenLocationChange()
        }
    }

    private fun moveLogoGoogle() {
        val logoMap = view?.findViewWithTag<View>("GoogleWatermark")
        val glLayoutParams = logoMap?.layoutParams as RelativeLayout.LayoutParams
        glLayoutParams.apply {
            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
            addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
            addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE)
            addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
            addRule(RelativeLayout.ALIGN_PARENT_END, 0)
            setMargins(ConvertHelper.dpToPx(mContext, 10), ConvertHelper.dpToPx(mContext, 10), 0, 0)
        }

        logoMap.layoutParams = glLayoutParams
    }

    /**
     * move icon my location to left bottom
     */
    private fun moveIconMyLocation(marginLeft: Int, marginTop: Int, marginRight: Int, marginBottom: Int) {
        val mapView = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).view
        if (mapView?.findViewById<View>(Integer.parseInt("1")) != null) {
            // Get the button view
            val locationButton = (mapView.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
            // and next place it, on bottom right (as Google Maps app)
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            // position on right bottomint marinTop=caculateMarginTop();
            layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)

            sharedElementEnterTransition
            locationButton.layoutParams = layoutParams
        }
    }

    fun updateDataWhenLocationChange() {
        mylocation = Constraint.myLocation

        var timeWaitingMapReady = 0
        if (!mapIsReady || mylocation == null)
            timeWaitingMapReady = 5000

        Handler().postDelayed({
            if (!Constraint.backFromInfoStore || listStore.isEmpty())
                getListStoreBaseOnTime()

            Handler().postDelayed({ Constraint.backFromInfoStore = false }, 1000)

            if (mCircle == null) {
                drawMarkerWithCircle(LatLng(mylocation!!.latitude, mylocation!!.longitude), AdjustFragment.distance.toDouble())
            } else {
                updateMarkerWithCircle(LatLng(mylocation!!.latitude, mylocation!!.longitude), AdjustFragment.distance.toDouble())
            }
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mylocation!!.latitude, mylocation!!.longitude), 15.toFloat()))
            zoomCamera(LatLng(mylocation!!.latitude, mylocation!!.longitude), AdjustFragment.distance)
            hideDialogGettingLocation()
        }, timeWaitingMapReady.toLong())
    }

    private fun showDialogGettingLocation() {
        if (GlobalHelper.gpsIsOn(activityParent)) {
            dialogGettingLocation.setCancelable(false)
            //dialogGettingLocation.show()
        }
    }

    private fun hideDialogGettingLocation() {
        if (dialogGettingLocation.isShowing)
            Handler().postDelayed({
                dialogGettingLocation.dismiss()
            }, 1000)
    }

    private fun toggleModeShow() {
        mapIsShowing = !mapIsShowing
        if (mapIsShowing) {
            ibtnMode.setImageResource(R.drawable.ic_list_task)
            ibtnMode.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryButton))
            showMap()
        } else {
            ibtnMode.setImageResource(R.drawable.ic_see_mode_map)
            ibtnMode.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryButton))
            showFragmentOffer()
        }
    }

    /**
     * init Google map
     */
    private fun initMap() {
        if (mGoogleMap == null)
            (mFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
    }

    private fun getListStoreBaseOnTime() {
        //apply AI to auto choose category
        SearchFragment.categoryId = 2
        SearchFragment.categoryName = "Coffee"
        tvCategory?.text = SearchFragment.categoryName
        loadingSearch?.visibility = View.VISIBLE
        layoutNote?.visibility = View.GONE
        layoutNoteVertical?.visibility = View.GONE
        lstStoreHorizontal?.visibility = View.GONE
        cleanStore()

        locationSearch = LatLng(mylocation!!.latitude, mylocation!!.longitude)

        Log.e("*****SearchStore", "Location: ${mylocation!!.latitude}, ${mylocation!!.longitude}\n" +
                "keyword: \n" +
                "CategoryId: ${SearchFragment.categoryId}\n" +
                "Distance: ${AdjustFragment.distance}\n" +
                "Using app: 1\n" +
                "Is shipping: 0\n" +
                "Is online: 0\n" +
                "Rating: 1\n" +
                "Offset: 0\n" +
                "Limit: 30\n")

        presenter.getListStoreOnMap(mylocation!!.latitude,
                mylocation!!.longitude,
                "",
                SearchFragment.categoryId,
                AdjustFragment.distance,
                1, 0, 0, 1, 0, COUNT_STORE_GET_FIRST)

        //send location if first search
        if (!Constraint.locationHadSent)
            presenter.sendLocationAtFirst(mylocation!!.latitude, mylocation!!.longitude)
    }

    /**
     * init ListRestaurant
     */
    private fun initListStore() {
        adapterStoreHorizontal = StoreOnMapAdapter(mContext, listStore)
        adapterStoreHorizontal!!.onItemClickedListener = object : StoreOnMapAdapter.OnItemClickedListener {
            override fun onClick(holder: StoreOnMapAdapter.ViewHolder, store: Store) {
                val intent = Intent(activityParent, InfoStoreActivity::class.java)
                intent.putExtra(InfoStoreActivity.ID_STORE_SEND, store.id)
                intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, store.name)

                startActivity(intent)
            }
        }

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayout.HORIZONTAL
        lstStoreHorizontal.adapter = adapterStoreHorizontal

        lstStoreHorizontal.addOnItemChangedListener { viewHolder, adapterPosition ->
            if (adapterPosition == -1)
                return@addOnItemChangedListener
            if (listMarkerStore.size == 0)
                return@addOnItemChangedListener
            if (adapterPosition >= listMarkerStore.size)
                return@addOnItemChangedListener

            listMarkerStore[adapterPosition].showInfoWindow()
            if (!nothingToLoadMore && adapterPosition == listStore.size - 1 && !isLoadingMore) {

                Log.e("*****SearchStore",
                        "Location: ${locationSearch!!.latitude}, ${locationSearch!!.longitude}\n" +
                                "keyword: $querySearch\n" +
                                "CategoryId: ${SearchFragment.categoryId}\n" +
                                "Distance: ${AdjustFragment.distance}\n" +
                                "Using app: 1\n" +
                                "Is shipping: ${if (AdjustFragment.storeHaveDelivery) 1 else 0}\n" +
                                "Is online: ${if (AdjustFragment.storeIsOpening) 1 else 0}\n" +
                                "Rating: 1\n" +
                                "Offset: ${listStore.size}\n" +
                                "Limit: ${Constraint.LIMIT}\n")

                presenter.getListStoreOnMap(locationSearch!!.latitude,
                        locationSearch!!.longitude, querySearch,
                        SearchFragment.categoryId,
                        AdjustFragment.distance,
                        1,
                        if (AdjustFragment.storeHaveDelivery) 1 else 0,
                        if (AdjustFragment.storeIsOpening) 1 else 0,
                        1,
                        listStore.size,
                        listStore.size + Constraint.LIMIT)
                isLoadingMore = true
                loadingMore?.visibility = View.VISIBLE
            }
        }

        val scaleTransformer = ScaleTransformer.Builder()
                .setMaxScale(1f)
                .setMinScale(0.9f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build()
        lstStoreHorizontal.setItemTransformer(scaleTransformer)

    }

    private fun initListStoreVertical() {
        adapterStoreVertical = StoreOfferAdapter(mContext, listStore)
        adapterStoreVertical!!.onStoreClickListener = object : StoreOfferAdapter.OnStoreClickListener {
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
            }
        }

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        lstStoreVertical?.layoutManager = layoutManager

        lstStoreVertical?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (!nothingToLoadMore && (layoutManager.findLastCompletelyVisibleItemPosition() == listStore.size - 1)) {

                    Log.e("*****SearchStore",
                            "Location: ${locationSearch!!.latitude}, ${locationSearch!!.longitude}\n" +
                                    "keyword: $querySearch\n" +
                                    "CategoryId: ${SearchFragment.categoryId}\n" +
                                    "Distance: ${AdjustFragment.distance}\n" +
                                    "Using app: 1\n" +
                                    "Is shipping: ${if (AdjustFragment.storeHaveDelivery) 1 else 0}\n" +
                                    "Is online: ${if (AdjustFragment.storeIsOpening) 1 else 0}\n" +
                                    "Rating: 1\n" +
                                    "Offset: ${listStore.size}\n" +
                                    "Limit: ${Constraint.LIMIT}\n")
                    presenter.getListStoreOnMap(locationSearch!!.latitude,
                            locationSearch!!.longitude, querySearch,
                            SearchFragment.categoryId,
                            AdjustFragment.distance,
                            1,
                            if (AdjustFragment.storeHaveDelivery) 1 else 0,
                            if (AdjustFragment.storeIsOpening) 1 else 0,
                            1,
                            listStore.size,
                            listStore.size + Constraint.LIMIT)
                    isLoadingMore = true
                    loadingMoreStoreVertical?.visibility = View.VISIBLE
                }
            }
        })

        lstStoreVertical?.layoutManager = layoutManager
        lstStoreVertical?.adapter = adapterStoreVertical
    }

    //zoomcamera fix display
    private fun zoomCamera(center: LatLng, radius: Int) {
        val scale = (radius / 500).toDouble()
        val zoomValue = (16 - Math.log(scale) / Math.log(2.0)).toInt()
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomValue.toFloat()))
    }

    /**
     * Hàm vẽ vòng tròn xung quanh mylocalocation
     */
    private fun updateMarkerWithCircle(position: LatLng, radiusInMeters: Double) {
        if (mCircle != null) {
            mCircle?.center = position
            mCircle?.radius = radiusInMeters
            /*markerMyLocation!!.run {
                setPosition(position)
                tag = -1
                title = HomeTopFragment.provinceChoosed
                hideInfoWindow()
            }*/
            zoomCamera(position, radiusInMeters.toInt())
        }
    }

    private fun drawMarkerWithCircle(position: LatLng?, radiusInMeters: Double) {

        val strokeColor = Color.TRANSPARENT //red outline
        val shadeColor = ContextCompat.getColor(mContext, R.color.colorMistApp_2) //opaque red fill


        val circleOptions = CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8f)
        mCircle = mGoogleMap?.addCircle(circleOptions)
    }

    private fun addMarkerIntoMap(data: ArrayList<Store>) {
        //clear all marker old
        listMarkerStore.forEach {
            it.remove()
        }
        listMarkerStore.clear()
        //add new marker
        data.forEachIndexed { index, store ->
            val markerOptions = MarkerOptions()
            markerOptions.position(store.location!!)
            markerOptions.title(store.name)
            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_coffee))
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
            val bitmapDraw: BitmapDrawable = activityParent.resources.getDrawable(R.drawable.marker_kaio_x) as BitmapDrawable
//            val bitmapDraw: BitmapDrawable = activityParent.resources.getDrawable(R.mipmap.ic_marker_green) as BitmapDrawable
            val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, ConvertHelper.dpToPx(mContext, 18), ConvertHelper.dpToPx(mContext, 18), false)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            val marker = mGoogleMap!!.addMarker(markerOptions)
            marker.tag = index
            listMarkerStore.add(marker)
        }
    }

    private fun doSearch(query: String, categoryId: Int, location: LatLng, distance: Int, haveKaak: Int, storeHaveDelivery: Int, isOpening: Int, isRating: Int, offset: Int, limit: Int) {
        nothingToLoadMore = false
        isLoadingMore = false

        loadingSearch?.visibility = View.VISIBLE
        //markerMyLocation?.isVisible = false
        lstStoreHorizontal.visibility = View.GONE
        layoutNote.visibility = View.GONE
        layoutNoteVertical.visibility = View.GONE
        cleanStore()
        locationSearch = location
        //Search on anylocation
        if (query.contains("-")) {
            //get location from address
            val address = query.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }
            val asyncGeoCoding = AsyncGeoCoding()
            asyncGeoCoding.geoCodingAddress = object : AsyncGeoCoding.GeoCodingAddress {

                override fun onSuccess(result: LatLng?) {
                    locationSearch = result
                    val strQuery = query.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

                    Log.e("*****SearchStore",
                            "Location: ${result!!.latitude}, ${result.longitude}\n" +
                                    "keyword: $strQuery\n" +
                                    "CategoryId: $categoryId\n" +
                                    "Distance: $distance\n" +
                                    "Using app: $haveKaak\n" +
                                    "Is shipping: $storeHaveDelivery\n" +
                                    "Is online: $isOpening\n" +
                                    "Rating: $isRating\n" +
                                    "Offset: $offset" +
                                    "Limit: $limit\n")
                    presenter.getListStoreOnMap(result.latitude, result.longitude, strQuery, categoryId, distance, haveKaak, storeHaveDelivery, isOpening, isRating, offset, limit)
                    updateMarkerWithCircle(result, distance.toDouble())
                }

                override fun onFail() {
                    GlobalHelper.showMessage(mContext, "Khong tim duoc dia chi", true)
                }
            }

            val link = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(" ".toRegex(), "+") + "&key=" + activityParent.resources.getString(R.string.google_maps_key)
            AdjustFragment.provinceChoosed = "$address."
            asyncGeoCoding.execute(link)
        } else {
            //on mylocation
            if (AdjustFragment.provinceChoosed.indexOf(".") > -1 || AdjustFragment.provinceChoosed == activityParent.resources.getString(R.string.all_your_location)) {
                AdjustFragment.provinceChoosed = activityParent.resources.getString(R.string.all_your_location)
                AdjustFragment.locationProvince = mylocation
                locationSearch = mylocation

                Log.e("*****SearchStore",
                        "Location: ${AdjustFragment.locationProvince?.latitude}, ${AdjustFragment.locationProvince?.longitude}\n" +
                                "keyword: $query\n" +
                                "CategoryId: $categoryId\n" +
                                "Distance: $distance\n" +
                                "Using app: $haveKaak\n" +
                                "Is shipping: $storeHaveDelivery\n" +
                                "Is online: $isOpening\n" +
                                "Rating: $isRating\n" +
                                "Offset: $offset" +
                                "Limit: $limit\n")
                presenter.getListStoreOnMap(AdjustFragment.locationProvince!!.latitude,
                        AdjustFragment.locationProvince!!.longitude,
                        query,
                        categoryId,
                        distance, haveKaak, storeHaveDelivery, isOpening, isRating, offset, limit)
                updateMarkerWithCircle(AdjustFragment.locationProvince!!, distance.toDouble())
            } else {
                Log.e("*****SearchStore",
                        "Location: ${location.latitude}, ${location.longitude}\n" +
                                "keyword: $query\n" +
                                "CategoryId: $categoryId\n" +
                                "Distance: $distance\n" +
                                "Using app: $haveKaak\n" +
                                "Is shipping: $storeHaveDelivery\n" +
                                "Is online: $isOpening\n" +
                                "Rating: $isRating\n" +
                                "Offset: $offset" +
                                "Limit: $limit\n")
                presenter.getListStoreOnMap(location.latitude, location.longitude, query, categoryId, distance, haveKaak, storeHaveDelivery, isOpening, isRating, offset, limit)
                updateMarkerWithCircle(location, distance.toDouble())
            }//on prov

        }//Search on mylocation or province
    }

    override fun onGetListStoreOnMapSuccess(data: ArrayList<Store>) {
        loadingSearch?.visibility = View.GONE
        refreshStoreVertical?.isRefreshing = false
        loadingMoreStoreVertical?.visibility = View.GONE
        //markerMyLocation?.isVisible = true

        if (data.isEmpty() && !isLoadingMore)
            showPanelEmptyResult()
        else {
            layoutNote?.visibility = View.GONE
            layoutNoteVertical?.visibility = View.GONE
            lstStoreHorizontal?.visibility = View.VISIBLE
            lstStoreVertical?.visibility = View.VISIBLE

            if (!isLoadingMore) {
                listStore.clear()
                lstStoreHorizontal.scrollToPosition(1)
            }
            listStore.addAll(data)

            if (mapIsShowing)
                adapterStoreHorizontal?.notifyDataSetChanged()
            else
                adapterStoreVertical?.notifyDataSetChanged()
        }

        if (data.size < Constraint.LIMIT)
            nothingToLoadMore = true
        isLoadingMore = false
        loadingMore?.visibility = View.GONE

        addMarkerIntoMap(listStore)
    }

    private fun cleanStore() {
        listStore.clear()
        adapterStoreHorizontal?.notifyDataSetChanged()
        addMarkerIntoMap(listStore)
    }

    private fun showPanelEmptyResult() {
        layoutNote?.visibility = View.VISIBLE
        layoutNoteVertical?.visibility = View.VISIBLE

        if (querySearch.isEmpty()) {
            tvMessageNoResult?.text = activityParent.resources.getString(R.string.map_get_list_store_no_result_message_no_query, SearchFragment.categoryName, AdjustFragment.distanceChoosed, AdjustFragment.provinceChoosed)
            tvMessageNoResultVertical?.text = activityParent.resources.getString(R.string.map_get_list_store_no_result_message_no_query, SearchFragment.categoryName, AdjustFragment.distanceChoosed, AdjustFragment.provinceChoosed)
        } else {
            tvMessageNoResult?.text = activityParent.resources.getString(R.string.map_get_list_store_no_result_message_have_query, SearchFragment.categoryName, querySearch, AdjustFragment.distanceChoosed, AdjustFragment.provinceChoosed)
            tvMessageNoResultVertical?.text = activityParent.resources.getString(R.string.map_get_list_store_no_result_message_have_query, SearchFragment.categoryName, querySearch, AdjustFragment.distanceChoosed, AdjustFragment.provinceChoosed)
        }

        lstStoreHorizontal?.visibility = View.GONE
        lstStoreVertical?.visibility = View.GONE
    }

    private fun expandScopeSearch() {
        //distanceSource.size=5 so maximum position is 5
        if (adjustFragment!!.choosedDistance < 5) {
            adjustFragment!!.updateDistanceSearch(adjustFragment!!.choosedDistance + 1)
            doSearch(querySearch,
                    SearchFragment.categoryId,
                    AdjustFragment.locationProvince!!,
                    AdjustFragment.distance,
                    1,
                    if (AdjustFragment.storeHaveDelivery) 1 else 0,
                    if (AdjustFragment.storeIsOpening) 1 else 0,
                    1,
                    0,
                    COUNT_STORE_GET_FIRST)
            tvDistance.text = AdjustFragment.distanceChoosed
        } else
            showDialogMessage(null, activityParent.resources.getString(R.string.map_scope_is_max))
    }

    override fun onGetListStoreOnMapFail(message: String?) {
        loadingSearch?.visibility = View.GONE
        refreshStoreVertical?.isRefreshing = false
        loadingMoreStoreVertical?.visibility = View.GONE

        isLoadingMore = false
        loadingMore?.visibility = View.GONE
        if (message == null || message.isEmpty())
            GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, message, true)
    }

    override fun getListStoreFollowSuccess(data: ArrayList<Store>) {

    }

    override fun getListStoreFollowFail(message: String?) {

    }

    override fun getListStoreOfferSucces(data: ArrayList<Store>) {

    }

    override fun getListStoreOfferFail(message: String?) {

    }

    override fun sendLocationAtFirstSuccess() {
        Constraint.locationHadSent = true
        activityParent.getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(Constraint.INFO_USER_LOCATION_HAD_SEND, true)
                .apply()
        Log.d("${Constraint.TAG_LOG}sendLocation", "success")
    }

    override fun sendLocationAtFirstFail(msg: String?) {
        //nothing to do
        Log.d("${Constraint.TAG_LOG}sendLocation", "fail:$msg")
    }


    private fun showSpotLight() {
        val spotLightModeShow = createSpotLight(ibtnMode, activityParent.resources.getString(R.string.spotlight_mode_show_title), activityParent.resources.getString(R.string.spotlight_mode_show_content))
        val spotLightCategory = createSpotLight(tvCategory, activityParent.resources.getString(R.string.spotlight_category_title), activityParent.resources.getString(R.string.spotlight_category_content))


        Spotlight.with(activityParent)
                .setOverlayColor(R.color.colorMistApp)
                .setDuration(200L)
                .setAnimation(DecelerateInterpolator(5f))
                //.setTargets(simpleTarget1,simpleTarget2,simpleTarget3)
                .setTargets(spotLightModeShow, spotLightCategory)
                .setClosedOnTouchedOutside(true)
                .setOnSpotlightStateListener(object : OnSpotlightStateChangedListener {
                    override fun onStarted() {

                    }

                    override fun onEnded() {
                        activityParent.getSharedPreferences(Constraint.FIRST_USE, Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean(Constraint.INFO_USER_IS_FIRST_USE_MAP, false)
                                .apply()
                    }
                })
                .start()
    }

    private fun createSpotLight(view: View, title: String, description: String): SimpleTarget {
        val locationView = IntArray(2)
        view.getLocationInWindow(locationView)
        val width = view.width
        val height = view.height
        val radius = Math.sqrt(width.toDouble() * width + height * height) / 2

        //imgLauncher.getLocationOnScreen(location1)

        val target = SimpleTarget.Builder(activityParent)
                .setPoint(locationView[0].toFloat() + width / 2, locationView[1].toFloat() + height / 2)
                .setShape(com.takusemba.spotlight.shape.Circle(radius.toFloat()))
                .setTitle(title)
                .setDuration(200L)
                .setDescription(description)
                .setOnSpotlightStartedListener(object : OnTargetStateChangedListener<SimpleTarget> {
                    override fun onStarted(target: SimpleTarget) {
                        // do something
                    }

                    override fun onEnded(target: SimpleTarget) {
                        // do something
                    }
                })
                .build()

        return target
    }

    fun createSpotLight(x: Int, y: Int, r: Int, title: String, description: String): SimpleTarget {
        //imgLauncher.getLocationOnScreen(location1)

        return SimpleTarget.Builder(activityParent)
                .setPoint(x.toFloat(), y.toFloat())
                .setShape(com.takusemba.spotlight.shape.Circle(r.toFloat()))
                .setTitle(title)
                .setDuration(200L)
                .setDescription(description)
                .setOnSpotlightStartedListener(object : OnTargetStateChangedListener<SimpleTarget> {
                    override fun onStarted(target: SimpleTarget) {
                        // do something
                    }

                    override fun onEnded(target: SimpleTarget) {
                        // do something
                    }
                })
                .build()
    }

    private fun showFragmentOffer() {
        layout_root.animate()
                .alpha(0.0f)
                .setDuration(400)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        layout_root?.visibility = View.GONE
                        adapterStoreVertical?.notifyDataSetChanged()
                    }
                })
    }

    private fun initAdjust() {
        if (childFragmentManager.findFragmentByTag("ADJUST") == null) {
            adjustFragment = AdjustFragment()
            adjustFragment!!.onConfirmClickListener = object : AdjustFragment.OnConfirmCLickListener {
                override fun onConfirmClick() {
                    locationSearch = AdjustFragment.locationProvince
                    doSearch(querySearch,
                            SearchFragment.categoryId,
                            locationSearch!!,
                            AdjustFragment.distance,
                            1,
                            if (AdjustFragment.storeHaveDelivery) 1 else 0,
                            if (AdjustFragment.storeIsOpening) 1 else 0,
                            1, 0, COUNT_STORE_GET_FIRST)
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
            }
            adjustFragment!!.onAdjustValueChange = object : AdjustFragment.OnAdjustValueChange {
                override fun onDistanceChange(newDistance: String) {
                    tvDistance.text = newDistance
                }
            }
            childFragmentManager.beginTransaction().replace(R.id.layoutAdjustSearch, adjustFragment, "ADJUST").commit()
        }
    }

    private fun initSearch() {
        if (childFragmentManager.findFragmentByTag("SEARCH") == null) {
            searchFragment = SearchFragment()
            searchFragment!!.onSearchFragmentCallBack = object : SearchFragment.OnSearchFragmentCallBack {
                override fun onSummit(query: String?) {
                    locationSearch = AdjustFragment.locationProvince

                    setQuerySearch(query!!)
                    doSearch(querySearch, SearchFragment.categoryId,
                            locationSearch!!,
                            AdjustFragment.distance,
                            1,
                            if (AdjustFragment.storeHaveDelivery) 1 else 0,
                            if (AdjustFragment.storeIsOpening) 1 else 0,
                            1, 0, COUNT_STORE_GET_FIRST)

                    hideFragmentSearch()
                }

                override fun onCategoryChoosed(query: String?, idCategory: Int, nameCategory: String?) {
                    locationSearch = AdjustFragment.locationProvince
                    setQuerySearch(query!!)
                    doSearch(query, idCategory,
                            locationSearch!!,
                            AdjustFragment.distance,
                            1,
                            if (AdjustFragment.storeHaveDelivery) 1 else 0,
                            if (AdjustFragment.storeIsOpening) 1 else 0,
                            1, 0, COUNT_STORE_GET_FIRST)
                    tvCategory?.text = nameCategory
                    hideFragmentSearch()
                }

                override fun onBackClick() {
                    hideFragmentSearch()
                }

                override fun onSpaceClick() {
                    hideFragmentSearch()
                }
            }
            childFragmentManager.beginTransaction().replace(R.id.layoutForFragmentSearch, searchFragment, "SEARCH").commit()
        }
    }

    private fun showFragmentSearch(defaultQuery: String) {
        layoutForFragmentSearch?.visibility = View.VISIBLE

        searchFragment?.setQuery(defaultQuery)
        searchFragment?.focusToSearch()
        adapterStoreHorizontal?.notifyDataSetChanged()
    }

    private fun hideFragmentSearch() {
        layoutForFragmentSearch?.visibility = View.GONE
        layoutToolbar?.hasFocus()
    }

    private fun showAdjust() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private fun showMap() {
        layout_root?.visibility = View.VISIBLE
        layout_root.animate()
                .alpha(1.0f)
                .setDuration(400)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)

                    }
                })
    }

    private fun setQuerySearch(query: String) {
        querySearch = query
        if (query.isEmpty()) {
            tvEmptySearch.visibility = View.VISIBLE
            tvSearch.text = ""
        } else {
            tvEmptySearch.visibility = View.GONE
            tvSearch.text = query
        }
    }

    private fun isFirstUse(): Boolean {
        return activityParent.getSharedPreferences(Constraint.FIRST_USE, Context.MODE_PRIVATE)
                .getBoolean(Constraint.INFO_USER_IS_FIRST_USE_MAP, true)
    }

    private fun getMyLocationFromSaveInstanceState(bundle: Bundle?) {
        if (bundle?.getSerializable("MY_LOCATION") != null) {
            val tempLocation = bundle.getSerializable("MY_LOCATION") as SerializableLatLng
            mylocation = LatLng(tempLocation.latitude, tempLocation.longitude)
        }
    }
}
