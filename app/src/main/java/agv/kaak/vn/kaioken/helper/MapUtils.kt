package agv.kaak.vn.kaioken.helper

import agv.kaak.vn.kaioken.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.FragmentManager
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*

class MapUtils {
    companion object {
        fun moveIconMyLocation(fragmentManager: FragmentManager, mapFragment: SupportMapFragment, marginLeft: Int, marginTop: Int, marginRight: Int, marginBottom: Int) {
            val mapView = mapFragment.view
            if (mapView?.findViewById<View>(Integer.parseInt("1")) != null) {
                // Get the button viewDetail
                val locationButton = (mapView.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
                // and next place it, on bottom right (as Google Maps app)
                val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
                // position on right bottomint marinTop=caculateMarginTop();
                layoutParams.apply {
                    addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                    addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
                    addRule(RelativeLayout.ALIGN_PARENT_START, 0)
                    addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
                    addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                    setMargins(marginLeft, marginTop, marginRight, marginEnd)
                }

                //sharedElementEnterTransition
                locationButton.requestLayout()
                //locationButton.layoutParams = layoutParams
            }
        }

        fun zoomCameraBoundListLatLng(googleMap: GoogleMap?, listLatLng: ArrayList<LatLng>, padding: Int) {
            val builder = LatLngBounds.builder()
            listLatLng.forEach {
                builder.include(it)
            }
            val bounds = builder.build()
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            googleMap?.animateCamera(cameraUpdate)
        }

        fun addMarker(mContext: Context, googleMap: GoogleMap?, listMarker: ArrayList<Marker>, title: String, icon: Int, latlngDes: LatLng) {
            val smallMarker = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.resources, icon), ConvertHelper.dpToPx(mContext, 24), ConvertHelper.dpToPx(mContext, 24), false)
            val iconBitmap = ImageHelper.getRoundedCornerBitmap(smallMarker, ConvertHelper.dpToPx(mContext, 12))

            listMarker.add(googleMap!!.addMarker(MarkerOptions()
                    .title(title)
                    .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
                    .position(latlngDes)))
        }

        fun addMarker(mContext: Context, googleMap: GoogleMap?, listMarker: ArrayList<Marker>, title: String, linkImage: String, latlngDes: LatLng) {
            //googleMap!!.addMarker(MarkerOptions().title(resources.getString(R.string.all_your_location)).position(myLocation).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_red)))
            Glide.with(mContext).asBitmap().load(linkImage).apply(RequestOptions()
                    .placeholder(R.drawable.place_holder_4).error(R.drawable.image_error))
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val smallMarker = Bitmap.createScaledBitmap(resource, ConvertHelper.dpToPx(mContext, 24), ConvertHelper.dpToPx(mContext, 24), false)
                            val iconStore = ImageHelper.getRoundedCornerBitmap(smallMarker, ConvertHelper.dpToPx(mContext, 12))
                            if (googleMap != null) {
                                listMarker.add(googleMap!!.addMarker(MarkerOptions()
                                        .title(title)
                                        .position(latlngDes)
                                        //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_green)))
                                        .icon(BitmapDescriptorFactory.fromBitmap(iconStore))))
                            }
                        }
                    })
        }

        fun animateMarker(googleMap: GoogleMap, marker: Marker?, toPosition: LatLng,
                          hideMarker: Boolean) {
            val handler = Handler()
            val start = SystemClock.uptimeMillis()
            val proj = googleMap.projection
            val startPoint = proj.toScreenLocation(marker?.position)
            val startLatLng = proj.fromScreenLocation(startPoint)
            val duration: Long = 500

            val interpolator = LinearInterpolator()

            handler.post(object : Runnable {
                override fun run() {
                    val elapsed = SystemClock.uptimeMillis() - start
                    val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                    val lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                    val lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude
                    marker?.position = LatLng(lat, lng)

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16)
                    } else {
                        marker?.isVisible = !hideMarker
                    }
                }
            })
        }

        fun getAddressFromCoordinates(mContext: Context, coordinates: LatLng?): String {
            val result = ""

            val geoCoder = Geocoder(mContext, Locale.getDefault())

            try {
                val addresses = geoCoder.getFromLocation(coordinates!!.latitude, coordinates.longitude, 1)

                if (addresses.size > 0) {
                    val fetchedAddress = addresses[0]
                    val strAddress = StringBuilder()
                    strAddress.append(fetchedAddress.getAddressLine(0))
                    return strAddress.toString()
                }

            } catch (e: IOException) {
                e.printStackTrace()
                GlobalHelper.showMessage(mContext, "Could not get address..!", true)
            }
            return result
        }
    }
}