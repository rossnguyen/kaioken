package agv.kaak.vn.kaioken.utils.firebase

import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Context
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


/**
 * Created by shakutara on 24/01/2018.
 */
class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()

        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.e("====token", refreshedToken)

        //  save token to SharedPreference
        val sharedPreference = getSharedPreferences(Constraint.SHARE_PRE_CLOUD_MESSAGING, Context.MODE_PRIVATE).edit()
        sharedPreference.putString(Constraint.SHARED_PRE_CLOUD_MESSAGING_TOKEN, refreshedToken)
        sharedPreference.apply()
    }

}