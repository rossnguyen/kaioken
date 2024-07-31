package agv.kaak.vn.kaioken.dialog


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ThumbnailFoodAdapter
import agv.kaak.vn.kaioken.adapter.ViewPagerShowImageFoodAdapter
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.fragment_show_image_food.*
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class ShowImageFoodFragment : BaseDialogFragment() {
    companion object {
        const val IMAGE_SEND = "IMAGE"
        const val POSITION_SENT = "POSITION"
    }

    private lateinit var adapterViewpager: ViewPagerShowImageFoodAdapter
    lateinit var adapterThumbnail: ThumbnailFoodAdapter

    private var arrayImage: ArrayList<String>? = ArrayList()
    private var positionStart = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_image_food, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen)
    }

    override fun getData() {
        arrayImage = arguments!!.getStringArrayList(IMAGE_SEND)
        positionStart = arguments!!.getInt(POSITION_SENT)
    }

    override fun loadData() {
        dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    private fun initViewPager() {
        //init list large image
        adapterViewpager = ViewPagerShowImageFoodAdapter(activityParent, arrayImage!!)
        vpPager.adapter = adapterViewpager

        //init list small image (on bottom)
        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        lstThumbnails.layoutManager = linearLayoutManager
        adapterThumbnail = ThumbnailFoodAdapter(activityParent, arrayImage!!, positionStart)
        lstThumbnails.adapter = adapterThumbnail

        vpPager.currentItem = positionStart
    }

    override fun addEvent() {
        initViewPager()

        adapterThumbnail.onItemClickListener = object : ThumbnailFoodAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                vpPager.currentItem = position
                adapterThumbnail.notifyDataSetChanged()
                scrollThumbnailToPosition(position)
            }
        }

        vpPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                //Xét xem change page do người dùng lướt hay là click hình nhỏ ở dưới
                //Nếu mà change bằng click thì không phải notifidatasetchange lại
                //Bởi vì nó là chạy 1 lần trước đó rồi
                if (!adapterThumbnail.changeByClick) {
                    adapterThumbnail.tempPosition = position
                    adapterThumbnail.notifyDataSetChanged()
                    scrollThumbnailToPosition(position)
                }
                adapterThumbnail.changeByClick = false
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        ibtnClose.setOnClickListener {
            dismiss()
        }

        //move to image thumbnail at first
        scrollThumbnailToPosition(positionStart)

        ibtnFullScreen.setOnClickListener {
            fullScreenImage()
        }

        ibtnScaleScreen.setOnClickListener{
            scaleScreenImage()
        }
    }

    override fun listenSocket() {

    }

    override fun offSocket() {

    }

    private fun scrollThumbnailToPosition(position: Int) {
        lstThumbnails.scrollToPosition(position)
    }

    private fun fullScreenImage() {
        layoutThumbnail.visibility = View.GONE
        ibtnFullScreen.visibility = View.GONE
        ibtnScaleScreen.visibility = View.VISIBLE
        val layoutParams = layoutMainImage.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
    }

    private fun scaleScreenImage(){
        layoutThumbnail.visibility=View.VISIBLE
        ibtnFullScreen.visibility=View.VISIBLE
        ibtnScaleScreen.visibility=View.GONE
        val layoutParams = layoutMainImage.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(0, mContext.resources.getDimensionPixelOffset(R.dimen.all_show_slide_image_margin_top), 0, 0)
    }
}
