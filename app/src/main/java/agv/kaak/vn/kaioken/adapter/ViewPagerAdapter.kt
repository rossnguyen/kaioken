package agv.kaak.vn.kaioken.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val listFragment: MutableList<Fragment>
    private val listTitle: MutableList<String>

    init {
        this.listFragment = ArrayList()
        this.listTitle = ArrayList()
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        listFragment.add(fragment)
        listTitle.add(title)
    }

    fun replaceFragment(fragmentNew: Fragment, position: Int) {
        listFragment[position] = fragmentNew
    }

    fun addPosition(fragmentNew: Fragment, position: Int) {
        listFragment.add(position, fragmentNew)
    }

    fun clear() {
        listFragment.clear()
        listTitle.clear()
    }
}