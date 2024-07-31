package agv.kaak.vn.kaioken.dialog


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment
import agv.kaak.vn.kaioken.helper.AsyncGeoCoding
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_choose_coordinates.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ChooseCoordinates : BaseDialogFragment(), OnMapReadyCallback {
    var address: String = ""
    var coordinates: LatLng? = null
    private var mGoogleMap: GoogleMap? = null
    private var mapFrag: SupportMapFragment? = null
    private var marker: Marker? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_coordinates, container, false)
    }

    override fun getData() {
        address = arguments!!.getString(Constraint.DATA_SEND)

    }

    override fun loadData() {
        val a=0
        /*if (mGoogleMap == null) {
            mapFrag = map as SupportMapFragment
        }
        mapFrag!!.getMapAsync(this)*/
    }

    override fun addEvent() {

    }

    private fun getCoordinates(){
        val asyncGeoCoding = AsyncGeoCoding()
        asyncGeoCoding.geoCodingAddress = object : AsyncGeoCoding.GeoCodingAddress {
            override fun onSuccess(result: LatLng?) {
                val markerOptions = MarkerOptions()
                markerOptions.position(result!!)
                markerOptions.title(address)
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_store))
                marker = mGoogleMap!!.addMarker(markerOptions)
                mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(result, 15.toFloat()))
            }


            override fun onFail() {
                GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_can_not_find_address), true)
            }
        }

        val link = "https://maps.googleapis.com/maps/api/geocode/json?address=${address.replace(" ".toRegex(),"+")}&key=${activityParent.resources.getString(R.string.google_maps_key)}"

        asyncGeoCoding.execute(link)
    }

    override fun listenSocket() {
        //nothing to do
    }

    override fun offSocket() {
        //nothing to do
    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        mGoogleMap!!.mapType = GoogleMap.MAP_TYPE_TERRAIN

        this.mGoogleMap!!.setOnMapClickListener {
            marker!!.position = it
        }

        getCoordinates()
    }
}
