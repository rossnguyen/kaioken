package agv.kaak.vn.kaioken.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NonSwipableViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var isEnable: Boolean = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.isEnable) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.isEnable) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPagingEnabled(enabled: Boolean) {
        this.isEnable = enabled
    }
}