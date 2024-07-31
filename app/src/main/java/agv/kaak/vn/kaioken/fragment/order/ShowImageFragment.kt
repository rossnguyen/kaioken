package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_show_image.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ShowImageFragment : BaseFragment() {
    internal var linkImage: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_image, container, false)
    }

    override fun getData() {
        linkImage = arguments!!.getString(ImageSliderFragment.IMAGE_SEND)
    }

    override fun loadData() {
        linkImage = arguments!!.getString(ImageSliderFragment.IMAGE_SEND)
        ImageHelper.loadImage(mContext, img, linkImage, PlaceHolderType.IMAGE)
    }

    override fun addEvent() {
        //nothing to do
    }
}
