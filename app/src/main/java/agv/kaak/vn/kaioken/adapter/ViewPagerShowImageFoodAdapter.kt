package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.ArrayList

class ViewPagerShowImageFoodAdapter(var context: Context, var arraySource: ArrayList<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return arraySource.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.item_show_image, container, false)
        val img = v.findViewById(R.id.img) as ImageView
        ImageHelper.loadImage(context, img, arraySource[position], PlaceHolderType.IMAGE)
//        img.setImageResource(R.drawable.place_holder_4)
        container.addView(v)

        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.refreshDrawableState()
    }
}
