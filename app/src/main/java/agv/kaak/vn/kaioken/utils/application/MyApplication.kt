package agv.kaak.vn.kaioken.utils.application

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.di.AppComponent
import agv.kaak.vn.kaioken.di.AppModule
import agv.kaak.vn.kaioken.di.DaggerAppComponent
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.request.target.ViewTarget

//import com.zing.zalo.zalosdk.oauth.ZaloSDKApplication

class MyApplication : Application() {
    private lateinit var mAppComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        //ZaloSDKApplication.wrap(this)

        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(applicationContext))
                .build()

        //track life cycle app
        registerActivityLifecycleCallbacks(AppLifecycleTracker())

        // Initialize the SDK before executing any other operations
        //FacebookSdk.sdkInitialize(applicationContext)
        //AppEventsLogger.activateApp(this)

        //this code solve for: You must not call setTag() on a view Glide is targeting
        ViewTarget.setTagId(R.id.glide_tag)

        //register for zalo

    }

    fun getAppComponent(): AppComponent {
        return mAppComponent
    }

    fun restartAppComponent(): AppComponent {
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(applicationContext))
                .build()
        return mAppComponent
    }

    class AppLifecycleTracker : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
            //nothing to do
        }

        override fun onActivityResumed(activity: Activity?) {
            //nothing to do
        }

        override fun onActivityDestroyed(activity: Activity?) {
            //nothing to do
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            //nothing to do
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            //nothing to do
        }

        private var numStarted = 0

        override fun onActivityStarted(activity: Activity?) {
            if (numStarted == 0) {
                // app went to foreground
                Constraint.appRunOnBackground = false
            }
            numStarted++
        }

        override fun onActivityStopped(activity: Activity?) {
            numStarted--
            if (numStarted == 0) {
                // app went to background
                Constraint.appRunOnBackground = true
                /*if(Constraint.BASE_SOCKET!=null){
                    Constraint.BASE_SOCKET?.disconnect()
                    Log.d("****Socket","Moi disconnect khi tat man hinh")
                }*/

            }
        }
    }
}