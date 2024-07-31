package agv.kaak.vn.kaioken.entity

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import java.util.ArrayList


class DirectionMap : Serializable {
    var strOrigin: String=""
    var latlngOrigin: LatLng?=null
    var strDestination: String=""
    var latlngDestination: LatLng?=null
    var intDistance: Int = 0
    var distance: String=""
    var duration: String=""
    var arrayPoint: ArrayList<LatLng> = arrayListOf()
}