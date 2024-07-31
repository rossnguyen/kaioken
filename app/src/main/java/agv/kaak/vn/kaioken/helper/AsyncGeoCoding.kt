package agv.kaak.vn.kaioken.helper

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject

class AsyncGeoCoding: AsyncTask<String, Void, LatLng>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: String?): LatLng {
        val httpHandler: HttpHandler = HttpHandler()
        val stringResponse: String = httpHandler.makeServiceCall(params[0])
        val jsonResponse: JSONObject = JSONObject(stringResponse)
        val status: String = jsonResponse.getString("status")
        Log.d("****GeoCoding",stringResponse)
        //if not exist location
        if (status.equals("ZERO_RESULTS"))
            return LatLng(1.0, 1.0)
        else {
            val lat: Double = jsonResponse.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location")
                    .getDouble("lat")
            val lng: Double = jsonResponse.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location")
                    .getDouble("lng")
            return LatLng(lat, lng)
        }
    }

    override fun onProgressUpdate(vararg values: Void?) {
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: LatLng?) {
        super.onPostExecute(result)
        if (geoCodingAddress != null) {
            if (result?.latitude == 1.0 && result.longitude == 1.0)
                geoCodingAddress.onFail()
            else
                geoCodingAddress.onSuccess(result)
        }

    }

    interface GeoCodingAddress {
        fun onSuccess(result: LatLng?)
        fun onFail()
    }

    lateinit var geoCodingAddress: GeoCodingAddress
}