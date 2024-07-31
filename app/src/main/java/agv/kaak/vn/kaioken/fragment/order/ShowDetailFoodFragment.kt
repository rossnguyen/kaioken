package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.ImageDetailItemFood
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.MenuItemDetailContract
import agv.kaak.vn.kaioken.mvp.presenter.MenuItemDetailPresenter
import android.content.res.Resources
import android.util.Log
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.fragment_show_detail_food.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ShowDetailFoodFragment : BaseDialogFragment(), MenuItemDetailContract.View {

    private lateinit var imageSliderFragment: ImageSliderFragment
    private lateinit var menuFood: MenuFood
    private val menuItemDetailPresenter = MenuItemDetailPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_show_detail_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resizeHeightSlide()
        Log.d("****height", layoutCover.height.toString())
    }

    private fun resizeHeightSlide() {
        val widthDevice = Resources.getSystem().displayMetrics.widthPixels
        //width dialog ~ 84% 😁
        val widthDialog = widthDevice * 84 / 100
        val layoutParams = layoutShowImage.layoutParams
        //square for view
        layoutParams.height = widthDialog
        layoutParams.width = widthDialog
        layoutShowImage.layoutParams = layoutParams
    }

    override fun getData() {
        menuFood = arguments!!.getSerializable(ShowMenuItemFragment.DETAIL_FOOD_SEND) as MenuFood
    }

    override fun loadData() {
        tvName.text = menuFood.name
        tvDescription.text = menuFood.description
        tvPrice.setupValue(menuFood.price.toString())

        menuItemDetailPresenter.getMenuItemDetail(menuFood.id)
        //setHeightForSlide(activityParent.resources.displayMetrics.widthPixels)
    }

    override fun addEvent() {
        ibtnBack.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun listenSocket() {
        //nothing to do
    }

    override fun offSocket() {
        //nothing to do
    }

    fun setHeightForSlide(height: Int) {
        //ViewGroup.LayoutParams paramsAll = activityParent.getWindow().getAttributes();
        //set width is 16:9 ratio
        //int width =container.getWidth();
        val paramsCover = layoutCover.layoutParams as FrameLayout.LayoutParams
        paramsCover.height = height
        layoutCover.layoutParams = paramsCover
    }

    override fun getMenuItemDetailSuccess(data: MenuFood) {
        //init slider
        val listImage = java.util.ArrayList<String>()
        for (eachImage in data.listImage) {
            listImage.add(eachImage.url)
        }

        val fragTransaction = mFragmentManager.beginTransaction()

        imageSliderFragment = ImageSliderFragment()
        val bundle = Bundle()
        //bundle.putStringArrayList(ImageSliderFragment.LIST_IMAGE_SEND,getListImage());
        bundle.putStringArrayList(ImageSliderFragment.LIST_IMAGE_SEND, listImage)
        imageSliderFragment.arguments = bundle

        fragTransaction.add(R.id.layoutShowImage, imageSliderFragment)
        fragTransaction.commitNowAllowingStateLoss()
    }

    override fun getMenuItemDetailFail(message: String) {
        GlobalHelper.showMessage(mContext, activityParent.getString(R.string.all_get_list_image_fail), true)
        Log.e("****errorLoadImage", message)
    }
}
