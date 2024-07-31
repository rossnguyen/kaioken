package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.CommentStoreAdapter
import agv.kaak.vn.kaioken.adapter.ImageUploadCommentAdapter
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.CreateStoreCommentResult
import agv.kaak.vn.kaioken.entity.result.GetStoreCommentResult
import agv.kaak.vn.kaioken.entity.result.UploadImageResult
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.CommentStoreContract
import agv.kaak.vn.kaioken.mvp.contract.UploadImageContract
import agv.kaak.vn.kaioken.mvp.presenter.CommentStorePresenter
import agv.kaak.vn.kaioken.mvp.presenter.UploadImagePresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_create_comment.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.item_show_image.view.*

class CreateCommentActivity : BaseActivity(), View.OnClickListener, CommentStoreContract.View, UploadImageContract.View {
    val PERMISSIONS_REQUEST_READ_STORAGE_AND_CAMERA = 101

    private lateinit var adapterImageUpload: ImageUploadCommentAdapter
    private var arrayPath = arrayListOf<String>()
    private var arrayLinkImage = arrayListOf<String>()

    private var restaurantCommentPresenter: CommentStorePresenter = CommentStorePresenter(this)
    private var uploadImagePresenter: UploadImagePresenter = UploadImagePresenter(this)

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)
        toolbar.tvTitle.text=resources.getString(R.string.all_comment)
        initListImageUpload()
        ViewCompat.setElevation(ibtnAddImage, resources.getDimension(R.dimen.all_masterial_elevation_raised_button_rest))
        ViewCompat.setElevation(toolbar, resources.getDimension(R.dimen.all_masterial_elevation_appbar))
        addEvent()
    }

    fun addEvent(){
        btnSend.setOnClickListener(this)
        ibtnAddImage.setOnClickListener(this)
        adapterImageUpload.onItemClickedListener=object : ImageUploadCommentAdapter.OnItemClickedListener {
            override fun onClickDelete(path: String, position: Int) {
                arrayPath.removeAt(position)
                adapterImageUpload.notifyDataSetChanged()
            }

            override fun onClickImage(path: String, bitmap: Bitmap) {
                //Show image when clicked
                val dialog = Dialog(this@CreateCommentActivity)
                val view = LayoutInflater.from(applicationContext).inflate(R.layout.item_show_image,null)
                val imgPicture = view.img as ImageView
                ImageHelper.loadImage(applicationContext,imgPicture,path, PlaceHolderType.IMAGE)
                dialog.setContentView(view)
                dialog.window.attributes.height=Resources.getSystem().displayMetrics.widthPixels
                dialog.window.setBackgroundDrawable(ColorDrawable(Color.BLACK))
                dialog.show()
            }
        }
        toolbar.ibtnBack.setOnClickListener(this)
    }

    fun initListImageUpload(){
        adapterImageUpload = ImageUploadCommentAdapter(applicationContext, arrayPath)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        lstImageUpload.adapter = adapterImageUpload
        lstImageUpload.layoutManager = layoutManager
    }

    override fun onConnectSocketSuccess() {
        //nothing to do
    }

    override fun onConnectSocketFail() {
        //nothing to do
    }

    override fun onClick(v: View?) {
        when (v) {
            btnSend -> doSendComment()
            ibtnAddImage -> chooseImage()
            ibtnBack -> onBackPressed()
        }
    }

    //send comment
    private fun doSendComment() {
        progressDialog=GlobalHelper.createProgressDialogHandling(this,null)
        progressDialog.show()
        //clear list link because if client click send again: array link will duplicate
        arrayLinkImage.clear()
        //show loadding bar
        if (arrayPath.size > 0)
            for (path in arrayPath)
                uploadImagePresenter.uploadImage(
                        Constraint.ID_STORE_ORDERING.toString(),
                        "0",
                        "1",
                        path)
        else {
            restaurantCommentPresenter.createComment(
                    Constraint.uid!!,
                    Constraint.sid!!,
                    Constraint.ID_STORE_ORDERING,
                    Constraint.uid!!.toInt(),
                    etContent.text.toString(),
                    arrayLinkImage)
        }
    }

    /**
     * Check permission camera
     */
    private fun checkCameraAndStorage() {
        val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(this, permission, PERMISSIONS_REQUEST_READ_STORAGE_AND_CAMERA)
    }

    /**
     * Choose image from library
     */
    private fun chooseImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            checkCameraAndStorage()
        else {
            ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                    .setToolbarColor("#212121")         //  Toolbar color
                    .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                    .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                    .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                    .setProgressBarColor("#4CAF50")     //  ProgressBar color
                    .setBackgroundColor("#212121")      //  Background color
                    .setCameraOnly(false)               //  Camera mode
                    .setMultipleMode(true)              //  Select multiple images or single image
                    .setFolderMode(true)                //  Folder mode
                    .setShowCamera(true)                //  Show camera button
                    .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                    .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                    .setDoneTitle("Done")               //  Done button title
                    .setLimitMessage("You have reached selection limit")    // Selection limit message
                    .setMaxSize(10)                     //  Max images can be selected
                    .setSavePath("ImagePicker")         //  Selected images
                    .setKeepScreenOn(true)              //  Keep screen on when selecting images
                    .start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Config.RC_PICK_IMAGES -> {
                if (data != null){
                    val images=data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
                    addImageChoosed(images)
                    adapterImageUpload.notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * Add iamge choosed to arrayPath
     *
     * @param paths
     */
    private fun addImageChoosed(selecImages: ArrayList<Image>) {
        selecImages.forEach {
            if(!pathIsExist(it.path))
                arrayPath.add(it.path)
        }
    }


    /**
     * Check path is exist on arrayPath
     *
     * @param path
     * @return
     */
    private fun pathIsExist(path: String): Boolean {
        for (eachPath in arrayPath)
            if (path.equals(eachPath))
                return true
        return false
    }

    override fun onCreateCommentSuccess(data: CreateStoreCommentResult) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()
        GlobalHelper.showMessage(applicationContext,resources.getString(R.string.comment_succes),true)
        onBackPressed()
        //back to activity store info
    }

    override fun onCreateCommentFail(message: String) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        showDialogMessage(null,"${resources.getString(R.string.comment_fail)}\n${message}")
    }

    override fun onGetCommentSuccess(data: java.util.ArrayList<GetStoreCommentResult>) {
        //nothing to do
    }

    override fun onGetCommentFail(message: String) {
        //nothing to do
    }

    override fun uploadImageSuccess(data: UploadImageResult) {
        arrayLinkImage.add(data.url)
        //get all link success
        if (arrayPath.size == arrayLinkImage.size) {
            restaurantCommentPresenter.createComment(
                    Constraint.uid!!,
                    Constraint.sid!!,
                    Constraint.ID_STORE_ORDERING,
                    Constraint.uid!!.toInt(),
                    etContent.text.toString(),
                    arrayLinkImage)
        }
    }

    override fun uploadImageFail(msg: String) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()
        GlobalHelper.showMessage(applicationContext,resources.getString(R.string.all_upload_image_fail),true)
    }
}
