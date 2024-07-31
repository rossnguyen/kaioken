package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.ModeOfUseActivity
import agv.kaak.vn.kaioken.dialog.DialogChangePassword
import agv.kaak.vn.kaioken.dialog.DialogEdit
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.UploadImageResult
import agv.kaak.vn.kaioken.entity.result.UserInfo
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.UploadImageContract
import agv.kaak.vn.kaioken.mvp.contract.UserContract
import agv.kaak.vn.kaioken.mvp.presenter.UploadImagePresenter
import agv.kaak.vn.kaioken.mvp.presenter.UserPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.util.Log
import android.view.inputmethod.EditorInfo
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : BaseFragment(), UserContract.View, View.OnClickListener, UploadImageContract.View {
    val PERMISSIONS_REQUEST_READ_STORAGE_AND_CAMERA = 101

    private val EDIT_NAME = 1
    private val EDIT_DOB = 2
    private val EDIT_GENDER = 3
    private val EDIT_PHONE = 4
    private val EDIT_EMAIL = 5
    private val EDIT_AVATAR = 6
    private val EDIT_COVER = 7

    val userPresenter = UserPresenter(this)
    private var uploadImagePresenter: UploadImagePresenter = UploadImagePresenter(this)
    lateinit var userInfo: UserInfo

    //save value edit, if save success -> set this value like new value
    var valueTemple = ""
    var editType = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun getData() {
        //nothing to do
    }

    override fun loadData() {
        userPresenter.getUserInfo()
    }

    override fun addEvent() {
        layoutUpdateCover.setOnClickListener(this)
        ibtnEditPhoneNumber.setOnClickListener(this)
        ibtnEditEmail.setOnClickListener(this)
        ibtnEditDOB.setOnClickListener(this)
        ibtnEditPassword.setOnClickListener(this)
        ibtnEditName.setOnClickListener(this)
        imgAvatar.setOnClickListener(this)
        radMale.setOnCheckedChangeListener { _, isChecked ->
            editType = EDIT_GENDER
            if (isChecked) {
                userInfo.gender = 0
                valueTemple = "0"
            } else {
                userInfo.gender = 1
                valueTemple = "1"
            }
            doSaveToServer()
        }
    }

    override fun getUserInfoSuccess(userInfo: UserInfo) {
        bindData(userInfo)
    }

    override fun getUserInfoFail(msg: String?) {
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.update_profile_get_user_info_fail), true)
        Log.e("****GetUserFail", msg)
    }

    override fun updateUserInfoSuccess() {
        when (editType) {
            EDIT_NAME -> tvName?.text = valueTemple
            EDIT_DOB -> tvDOB?.text = valueTemple
            EDIT_GENDER -> {
                if (valueTemple == "0")
                    radMale?.isChecked = true
                else
                    radFemale?.isChecked = true
            }
            EDIT_PHONE -> tvPhoneNumber?.text = valueTemple
            EDIT_EMAIL -> tvEmailAddress?.text = valueTemple
        }
        if (editType != EDIT_GENDER)
            GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_update_success), true)

    }

    override fun updateUserInfoFail(msg: String?) {
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.update_profile_fail), true)
        Log.e("****UpdateUserFail", msg)
    }

    override fun changePasswordSuccess() {
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.update_profile_update_password_success), true)
    }

    override fun changePasswordFail(msg: String?) {
        GlobalHelper.showMessage(mContext, "${activityParent.resources.getString(R.string.update_profile_update_password_fail)}\n$msg", true)
        Log.e("****ChangePasswordFail", msg)
    }

    override fun uploadImageSuccess(data: UploadImageResult) {
        if (editType == EDIT_AVATAR) {
            ImageHelper.loadImage(mContext, imgAvatar, valueTemple, PlaceHolderType.IMAGE)
            GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.update_profile_avatar_success), true)
        } else {
            ImageHelper.loadImage(mContext, imgCover, valueTemple, PlaceHolderType.IMAGE)
            GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.update_profile_cover_success), true)
        }
    }

    override fun uploadImageFail(msg: String) {
        if (editType == EDIT_AVATAR)
            GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.update_profile_avatar_fail), true)
        else
            GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.update_profile_cover_fail), true)
    }

    override fun onClick(v: View?) {
        when (v) {
            ibtnEditDOB -> showDialogChooseDate()
            ibtnEditName -> showDialogEdit(EDIT_NAME)
            ibtnEditEmail -> showDialogEdit(EDIT_EMAIL)
            ibtnEditPhoneNumber -> showDialogEdit(EDIT_PHONE)
            ibtnEditPassword -> showDialogChangePassword()
            imgAvatar -> doUpdateAvatar()
            layoutUpdateCover -> doUpdateCover()
        }
    }

    private fun bindData(userInfo: UserInfo) {
        this.userInfo = userInfo
        tvName?.text = userInfo.name!!
        tvDOB?.text = ConvertHelper.dateToString(userInfo.birthDay)
        if (userInfo.gender == 0)
            radMale?.isChecked = true
        else {
            radFemale?.isChecked = true
        }

        tvPhoneNumber?.text = userInfo.phone!!
        tvEmailAddress?.text = userInfo.email!!

        ImageHelper.loadImage(mContext, imgCover, userInfo.linkCoverLarge, PlaceHolderType.IMAGE)
//        ImageHelper.loadImage(mContext, imgCover, "abc", PlaceHolderType.IMAGE)
        ImageHelper.loadImage(mContext, imgAvatar, userInfo.linkAvarta, PlaceHolderType.AVATAR)
    }

    private fun showDialogChooseDate() {
        var oldDate = Calendar.getInstance().time.date
        var oldMonth = Calendar.getInstance().time.month
        var oldYear = Calendar.getInstance().time.year

        if (!tvDOB.text.isEmpty()) {
            oldDate = tvDOB.text.split("-")[0].toInt()
            oldMonth = tvDOB.text.split("-")[1].toInt()
            oldYear = tvDOB.text.split("-")[2].toInt()
        }

        val datePicker = DatePickerDialog(mContext,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val newValue = Date(year - 1900, month, dayOfMonth)
                    userInfo.birthDay = newValue
                    valueTemple = ConvertHelper.dateToString(userInfo.birthDay)
                    editType = EDIT_DOB
                    doSaveToServer()
                },
                oldYear,
                oldMonth - 1,
                oldDate)
        datePicker.show()

    }

    private fun showDialogEdit(editType: Int) {
        this.editType = editType
        var oldValue = ""
        var inputType = EditorInfo.TYPE_CLASS_TEXT
        when (editType) {
            EDIT_NAME -> {
                oldValue = tvName.text.toString()
                inputType = EditorInfo.TYPE_CLASS_TEXT
            }

            EDIT_EMAIL -> {
                oldValue = tvEmailAddress.text.toString()
                inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }

            EDIT_PHONE -> {
                oldValue = tvPhoneNumber.text.toString()
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
            }
        }
        val onClickSaveListener = object : DialogEdit.ClickSaveListener {
            override fun onCLick(newValue: String) {
                valueTemple = newValue
                when (editType) {
                    EDIT_NAME -> userInfo.name = newValue
                    EDIT_EMAIL -> userInfo.email = newValue
                    EDIT_PHONE -> userInfo.phone = newValue
                }
                doSaveToServer()
            }
        }

        val dialogEdit = DialogEdit(activityParent, oldValue, onClickSaveListener, inputType)
        dialogEdit.show()
    }

    private fun showDialogChangePassword() {
        val dialogChangePassword = DialogChangePassword(activityParent,
                object : DialogChangePassword.CLickChangePasswordListener {
                    override fun onClickSave(oldPassword: String, newPassword: String) {
                        userPresenter.changePassword(oldPassword, newPassword, newPassword)
                    }
                })
        dialogChangePassword.show()
    }

    fun doSaveToServer() {
        userPresenter.updateUserInfo(Constraint.uid!!,
                Constraint.sid!!,
                userInfo.name!!,
                userInfo.email!!,
                userInfo.gender!!,
                ConvertHelper.dateToStringGlobal(userInfo.birthDay),
                userInfo.phone!!)
    }

    private fun doUpdateAvatar() {
        editType = EDIT_AVATAR
        chooseImage()
    }

    private fun doUpdateCover() {
        editType = EDIT_COVER
        chooseImage()
    }

    /**
     * Check permission camera
     */
    private fun checkCameraAndStorage() {
        val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(activityParent, permission, PERMISSIONS_REQUEST_READ_STORAGE_AND_CAMERA)
    }

    /**
     * Choose image from library
     */
    private fun chooseImage() {
        if (ContextCompat.checkSelfPermission(activityParent, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activityParent, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
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
        when (requestCode) {
            Config.RC_PICK_IMAGES -> {
                if (data != null) {
                    val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
                    onPickImageSuccess(images[0].path)
                }
            }
        }
    }

    private fun onPickImageSuccess(link: String) {
        valueTemple = link
        if (editType == EDIT_AVATAR)
            uploadImagePresenter.uploadImage(Constraint.uid!!, "3", "2", link)
        else
            uploadImagePresenter.uploadImage(Constraint.uid!!, "3", "1", link)
    }
}
