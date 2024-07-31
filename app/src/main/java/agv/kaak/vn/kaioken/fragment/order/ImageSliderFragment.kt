package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.widget.ImageView
import android.widget.LinearLayout
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.fragment_image_slider.*
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ImageSliderFragment : BaseFragment() {
    companion object {
        const val LIST_IMAGE_SEND = "LIST_IMAGE"
        const val IMAGE_SEND = "IMAGE_SEND"
    }

    var listImage: ArrayList<String>? = arrayListOf()
    var listImageView: ArrayList<ImageView> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_slider, container, false)
    }

    override fun getData() {
        listImage = arguments!!.getStringArrayList(LIST_IMAGE_SEND)
    }

    override fun loadData() {
        //init view pager
        val adapter = ImageSliderAdapter(mContext, listImage!!)
        for (i in listImage!!.indices) {
            //adapter.addFragment(ShowImageFragment(), listImage!![i])

            val img = ImageView(context)
            img.layoutParams = LinearLayout.LayoutParams(ConvertHelper.dpToPx(mContext, 20), ViewGroup.LayoutParams.MATCH_PARENT)
            img.setImageResource(R.drawable.ic_dot_page_gray_choosed)
            img.tag = i
            listImageView.add(img)
            layoutIndex.addView(img)
        }
        viewpager.adapter = adapter

        if (listImageView.size > 0)
            listImageView[0].setImageResource(R.drawable.ic_dot_page_gray)
    }

    override fun addEvent() {
        viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                for (img in listImageView) {
                    img.setImageResource(R.drawable.ic_dot_page_gray_choosed)
                }
                listImageView[position].setImageResource(R.drawable.ic_dot_page_gray)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
    /*inner class ImageSliderAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private var listFragment = ArrayList<Fragment>()
        private var arrayListImage = ArrayList<String>()

        override fun getItem(position: Int): Fragment {

            return listFragment[position]
        }

        override fun getCount(): Int {
            return listFragment.size
        }

        fun addFragment(fragment: Fragment, linkImage: String) {
            val bundle = Bundle()
            bundle.putString(IMAGE_SEND, linkImage)
            fragment.arguments = bundle

            listFragment.add(fragment)
            arrayListImage.add(linkImage)
        }
    }*/

    inner class ImageSliderAdapter(val mContext: Context, val sourceImage: ArrayList<String>) : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return sourceImage.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val photoView = PhotoView(container.context)
            photoView.scaleType = ImageView.ScaleType.CENTER_CROP
            ImageHelper.loadImage(mContext, photoView, sourceImage[position], PlaceHolderType.IMAGE)
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return photoView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}

