package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.MapUtils
import agv.kaak.vn.kaioken.utils.Constraint
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.view.View
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_choose_coordinates.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import java.io.IOException
import java.util.*


class ChooseCoordinatesActivity : BaseActivity(), OnMapReadyCallback {

    companion object {
        const val COORDINATES_CALL_BACK = "COORDINATES_CALL_BACK"
        const val ADDRESS_CALL_BACK = "ADDRESS_CALL_BACK"
    }

    private val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1234

    var defaultAddress = ""
    private var mGoogleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_coordinates)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getData()
        addEvent()
        loadData()
    }

    private fun getData() {
        defaultAddress = intent.getStringExtra(Constraint.DATA_SEND)
    }

    private fun addEvent() {
        btnSelectedCoordinates.setOnClickListener {
            callBackAddress()
        }

        tvAddressFromCoordinates.setOnClickListener {
            showAutoCompleteAddress()
        }
    }

    private fun loadData() {
        tvAddressFromCoordinates.text = defaultAddress
        initMap()
    }

    private fun initMap() {
        if (mGoogleMap == null)
            (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
    }

    override fun onConnectSocketSuccess() {
        //do nothing
    }

    override fun onConnectSocketFail() {
        //do nothing
    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        mGoogleMap!!.mapType = GoogleMap.MAP_TYPE_TERRAIN

        /*mGoogleMap!!.setOnMapClickListener {
            marker!!.position = it
            coordinates = it
        }*/

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap!!.isMyLocationEnabled = true
            }
        } else {
            //Not in api-23, no need to prompt
            mGoogleMap!!.isMyLocationEnabled = true
        }

        MapUtils.moveIconMyLocation(supportFragmentManager,
                (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment),
                0, 0, resources.getDimensionPixelOffset(R.dimen.all_margin_short), 0)

        doGetMyLocation()

        initCameraIdle()
    }

    private fun initCameraIdle() {
        mGoogleMap?.setOnCameraIdleListener {
            val center = mGoogleMap?.cameraPosition?.target
            tvAddressFromCoordinates.text = MapUtils.getAddressFromCoordinates(applicationContext, center)
        }
    }

    private fun createMarker(latlng: LatLng?, title: String) {
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17.toFloat()))
        imgMarker.visibility = View.VISIBLE
    }

    private fun doGetMyLocation() {
        layoutLoading.visibility = View.VISIBLE
        Handler().postDelayed({
            requestLocation()
        }, 1000)
    }


    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        fusedLocationClient.requestLocationUpdates(
                LocationRequest.create(),
                object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult?) {
                        layoutLoading?.visibility = View.GONE
                        p0 ?: return
                        createMarker(LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude), "")
                    }
                }, null)
    }

    private fun callBackAddress() {
        val coordinates = "${mGoogleMap?.cameraPosition?.target?.latitude}, ${mGoogleMap?.cameraPosition?.target?.longitude}"

        val returnIntent = Intent()
        returnIntent.putExtra(ADDRESS_CALL_BACK, tvAddressFromCoordinates.text.toString())
        returnIntent.putExtra(COORDINATES_CALL_BACK, coordinates)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }


    private fun showAutoCompleteAddress() {
        try {
            val typeFilter = AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .setCountry("VN")
                    .build()
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
        } catch (e: GooglePlayServicesRepairableException) {
            showMessage("Google Play Service Repair")
        } catch (e: GooglePlayServicesNotAvailableException) {
            showMessage("Google Play Service Not Available")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                if (!place.address.toString().contains(place.name)) {
                    tvAddressFromCoordinates.text = "${place.name}, ${place.address}"
                } else {
                    tvAddressFromCoordinates.text = place.address
                }

                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16f)
                mGoogleMap?.animateCamera(cameraUpdate);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                showMessage("Error in retrieving place info")
            }
        }
    }
}
