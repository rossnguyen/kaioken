package agv.kaak.vn.kaioken.fragment.base

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.application.MyApplication
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import io.socket.client.Socket
import javax.inject.Inject

abstract class BaseFragment_ : Fragment() {

    @Inject
    lateinit var socket: Socket

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupComponent()

        progressDialog = ProgressDialog(context)
    }

    fun routeToNewActivity(activity: Class<*>) {
        val intent = Intent(getActivity(), activity)
        startActivity(intent)
    }

    fun routeToNewActivity(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(getActivity(), activity)
        intent.putExtra(Constraint.TAG_BUNDLE, bundle)
        startActivity(intent)
    }

    fun showMessage(msg: String?) {
        if (isAdded)
            GlobalHelper.showMessage(activity, msg, true)
    }

    fun showSnackbarNoAction(view: View, message: String) {

        val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val myView = snack.view
        val tv: TextView = myView.findViewById(android.support.design.R.id.snackbar_text)
        tv.setTextColor(Color.WHITE)
        snack.show()
    }

    private fun setupComponent() {
//        detect tablet
        initSocket()
    }

    /***
     * init socket when device is out of memory
     */
    private fun initSocket() {
        (activity?.application as MyApplication).getAppComponent().inject(this)
        Constraint.BASE_SOCKET = socket
    }

    /*fun chooseImage(onlyCamera: Boolean, multipleMode: Boolean, showCamera: Boolean, folderTitle: String, maxImage: Int, selectedImages: ArrayList<Image>) {
        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(onlyCamera)               //  Camera mode
                .setMultipleMode(multipleMode)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(showCamera)                //  Show camera button
                .setFolderTitle(folderTitle)           //  Folder title (works with FolderMode = true)
                .setImageTitle("GALLERY")         //  Image title (works with FolderMode = false)
                .setDoneTitle(context?.getString(R.string.all_save))               //  Done button title
                .setLimitMessage(context?.getString(R.string.all_choose_max_images, maxImage))    // Selection limit message
                .setMaxSize(maxImage)                     //  Max images can be selected
                .setSavePath(context?.getString(R.string.app_name))         //  Image capture folder name
                .setSelectedImages(selectedImages)          //  Selected images
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start()                           //  Start ImagePicker
    }*/

//    fun showFragment(fragment: BaseFragment) {
//        if (Constraint.isTablet)
//            activity?.supportFragmentManager?.beginTransaction()
//                    ?.add(R.id.layoutTaskRight, fragment)
//                    ?.addToBackStack(null)
//                    ?.commit()
//        else if (Constraint.isPhone)
//            activity?.supportFragmentManager?.beginTransaction()
//                    ?.add(R.id.layoutTaskLeft, fragment)
//                    ?.addToBackStack(null)
//                    ?.commit()
//    }

    fun saveToSharedPreferences(key: String, value: String) {
        //  save StoreID to SharedPreferences
        val sharedPreferences = activity?.getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

//    fun hideHomeBottomFragment() {
//        val homeBottomFragment: HomeBottomFragment = activity?.supportFragmentManager?.findFragmentByTag(HomeBottomFragment::class.java.simpleName) as HomeBottomFragment
//        homeBottomFragment.hideHomeBottomFragment()
//    }
//
//    fun showHomeBottomFragment() {
//        val homeBottomFragment: HomeBottomFragment = activity?.supportFragmentManager?.findFragmentByTag(HomeBottomFragment::class.java.simpleName) as HomeBottomFragment
//        homeBottomFragment.showHomeBottomFragment()
//    }

    /*fun showFragment(fragment: BaseFragment) {

        activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.layoutTaskLeft, fragment, fragment::class.java.simpleName)
                ?.addToBackStack(fragment::class.java.simpleName)
                ?.commit()
    }
*/
    fun showSocketLoadingDialog() {
        if (!progressDialog.isShowing) {
            progressDialog.setMessage("Mất kết nối đến máy chủ, đang kết nối lại...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.setCancelable(false)
            progressDialog.show()
        }
    }

    fun hideSocketLoadingDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    fun showDialogMessage(title:String?, message:String?){
        AlertDialog.Builder(context!!)
                .setMessage(message)
                .setNegativeButton(resources.getString(R.string.all_close), DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .setTitle(title)
                .setCancelable(true)
                .show()
    }

    abstract fun showLoading()
    abstract fun hideLoading()

}