package agv.kaak.vn.kaioken.helper;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import agv.kaak.vn.kaioken.entity.DirectionMap;

/**
 * Created by MyPC on 04/11/2017.
 */

public class AsyncGetDirection extends AsyncTask <String, Void, DirectionMap> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected DirectionMap doInBackground(String... strings) {
        DirectionMap result=new DirectionMap();
        HttpHandler httpHandler=new HttpHandler();
        for(int j=0;j<strings.length;j++){
            String jsonStr=httpHandler.makeServiceCall(strings[j]);
            if(jsonStr!=null){
                try{
                    JSONObject jsonResult=new JSONObject(jsonStr);
                    JSONArray routes=jsonResult.getJSONArray("routes");
                    if(routes.length()>0){
                        JSONObject route=routes.getJSONObject(0);
                        JSONArray legs=route.getJSONArray("legs");
                        if(legs.length()>0){
                            JSONObject leg=legs.getJSONObject(0);
                            JSONObject distance=leg.getJSONObject("distance");
                            result.setDistance(distance.getString("text"));
                            result.setIntDistance(distance.getInt("value"));
                            JSONObject duration=leg.getJSONObject("duration");
                            result.setDuration(duration.getString("text"));

                            result.setStrDestination(leg.getString("end_address"));

                            JSONObject end_location=leg.getJSONObject("end_location");
                            LatLng destinationLocation=new LatLng(end_location.getDouble("lat"),end_location.getDouble("lng"));
                            result.setLatlngDestination(destinationLocation);

                            result.setStrOrigin(leg.getString("start_address"));

                            JSONObject start_location=leg.getJSONObject("start_location");
                            LatLng startLocation=new LatLng(start_location.getDouble("lat"),start_location.getDouble("lng"));
                            result.setLatlngOrigin(startLocation);
                        }

                        JSONObject overViewPolyline=route.getJSONObject("overview_polyline");
                        String points=overViewPolyline.getString("points");
                        result.setArrayPoint((ArrayList<LatLng>) PolyUtil.decode(points));

                        return result;
                    }

                }catch (Exception e){
                    Log.e("ErrorParse","JSON Parsing errror: "+e.getMessage());
                }
            }
            else {
                Log.e("GetJSON","Couldn't get JSON from server");
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(DirectionMap data) {
        super.onPostExecute(data);
        if(data==null)
            return;
        if(data.getArrayPoint().size()!=0) {
            if (onGetDirectionResult != null)
                onGetDirectionResult.onSuccess(data);

        }
        else
            if(onGetDirectionResult!=null)
                onGetDirectionResult.onFail();
    }

    public interface OnGetDirectionResult{
        public void onSuccess(DirectionMap direction);
        public void onFail();
    }

    OnGetDirectionResult onGetDirectionResult;
    public void setOnGetDirectionResult(OnGetDirectionResult onGetDirectionResult){
        this.onGetDirectionResult=onGetDirectionResult;
    }
}
