package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.DirectionMap
import agv.kaak.vn.kaioken.helper.AsyncGetDirection
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_direction.*
import java.net.URLEncoder
import java.util.ArrayList

class DirectionActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        val NAME_STORE_SEND = "NAME_STORE_SEND"
        val ADDRESS_STORE_SEND = "ADDRESS_STORE_SEND"
        val IMAGE_STORE_SEND = "IMAGE_STORE_SEND"
    }

    var linkGetDirection = "https://maps.googleapis.com/maps/api/directions/json?"


    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    private var destination: String = ""
    private var nameStore: String = ""
    private var addressStore: String = ""
    private var imageStore: String = ""
    private var googleMap: GoogleMap? = null
    private var mapFrag: SupportMapFragment? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var myLocation = LatLng(10.7721, 106.696)
    private val paths = ArrayList<Polyline>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction)

        getData()
        addEvent()

        //Kiểm tra xem thiết bị đã cấp quyền truy cập vị trí hay chưa
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission(this)
        }
        loading_bar.visibility = View.VISIBLE
        loadData()
    }

    private fun getData() {
        destination = intent.getStringExtra(Constraint.DATA_SEND)
        nameStore = intent.getStringExtra(NAME_STORE_SEND)
        addressStore = intent.getStringExtra(ADDRESS_STORE_SEND)
        imageStore = intent.getStringExtra(IMAGE_STORE_SEND)
    }

    private fun loadData() {
        tvName.text = nameStore
        tvAddress.text = addressStore
        mapFrag = map as SupportMapFragment
        mapFrag!!.getMapAsync(this)
    }

    private fun addEvent() {
        ibtnClose.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap!!.mapType = GoogleMap.MAP_TYPE_TERRAIN

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap!!.isMyLocationEnabled = true
            }
        } else {
            //Not in api-23, no need to prompt
            googleMap!!.isMyLocationEnabled = true
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        //Get my location
        this.mFusedLocationClient!!.lastLocation
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        myLocation = LatLng(location.latitude, location.longitude)

                        //draw direction after get my location
                        val asyncGetDirection = AsyncGetDirection()
                        asyncGetDirection.setOnGetDirectionResult(object : AsyncGetDirection.OnGetDirectionResult {
                            override fun onSuccess(direction: DirectionMap) {
                                loading_bar?.visibility = View.GONE
                                tvDistance?.text = direction.distance
                                tvDuration?.text = direction.duration
                                drawLine(direction)
                                //add marker origin and marker destination
                                addMarker(direction.latlngDestination!!)
                                zoomCamera(direction.latlngOrigin!!, direction.latlngDestination!!)
                            }

                            override fun onFail() {
                                tvDistance?.text = resources.getString(R.string.all_unknown)
                                tvDuration?.text = resources.getString(R.string.all_unknown)

                                GlobalHelper.showMessage(applicationContext, "Cann't find way", true)
                            }
                        })
                        //set complete link direction before excute
                        var link = ""
                        try {
                            link = linkGetDirection + "origin=" + myLocation.latitude + "," + myLocation.longitude + "&destination=" + URLEncoder.encode(destination, "utf-8") + "&language=vi&key=" + resources.getString(R.string.google_maps_key)
                        } catch (e: Exception) {
                        }

                        if (link.isNotEmpty())
                            asyncGetDirection.execute(link)
                        //asyncGetDirection.execute("https://maps.googleapis.com/maps/api/directions/json?origin=10.763031,%20106.682553&destination=Cho%20Ben%20Thanh&key=AIzaSyAwmJqaQKa746Lg-sLzOQqwglfcYqko-VA");
                    }
                }
    }

    //Draw line when get direction
    private fun drawLine(directionMap: DirectionMap) {
        //if exist draw line so delete it
        if (paths.size > 0)
            paths.removeAt(0)

        val polylineOptions = PolylineOptions().geodesic(true).color(Color.BLUE).width(8f)
        polylineOptions.add(directionMap.latlngOrigin)
        for (i in 0 until directionMap.arrayPoint.size)
            polylineOptions.add(directionMap.arrayPoint[i])
        polylineOptions.add(directionMap.latlngDestination)
        paths.add(googleMap!!.addPolyline(polylineOptions))
    }

    //zoomcamera to show 2 marker
    private fun zoomCamera(start: LatLng, end: LatLng) {
        val bounds = LatLngBounds.builder().include(start)
                .include(end)
                .build()
        val padding = ConvertHelper.dpToPx(applicationContext, 64)
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap!!.animateCamera(cameraUpdate)
    }

    //add marker start and marker end
    private fun addMarker(latlngDes: LatLng) {
        //googleMap!!.addMarker(MarkerOptions().title(resources.getString(R.string.all_your_location)).position(myLocation).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_red)))
        Glide.with(applicationContext).asBitmap().load(imageStore).apply(RequestOptions()
                .placeholder(R.drawable.place_holder_4).error(R.drawable.image_error))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                        val smallMarker = Bitmap.createScaledBitmap(resource, ConvertHelper.dpToPx(applicationContext, 24), ConvertHelper.dpToPx(applicationContext, 24), false)
                        val iconStore = ImageHelper.getRoundedCornerBitmap(smallMarker, ConvertHelper.dpToPx(applicationContext, 12))

                        googleMap!!.addMarker(MarkerOptions()
                                .title(nameStore)
                                .position(latlngDes)
                                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_green)))
                                .icon(BitmapDescriptorFactory.fromBitmap(iconStore)))
                    }
                })

    }

    private fun checkLocationPermission(activity: Activity): Boolean {
        if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        }
        return true

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(applicationContext,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap!!.isMyLocationEnabled = true

                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    GlobalHelper.showMessage(applicationContext, "permission deny", true)
                }
                return
            }
        }
    }
}
