package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.InfoStoreActivity
import agv.kaak.vn.kaioken.adapter.KiOfUserAdapter
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.response.KiInfo
import agv.kaak.vn.kaioken.entity.result.KiOfPageResult
import agv.kaak.vn.kaioken.entity.result.KiOfUserResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.KiContract
import agv.kaak.vn.kaioken.mvp.presenter.KiPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.widget.AbsListView
import kotlinx.android.synthetic.main.fragment_list_ki.*
import kotlinx.android.synthetic.main.item_ki_of_user.view.*
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 *
 */
class ListKiFragment : BaseFragment() {

    var listKiOfUser: ArrayList<KiInfo> = arrayListOf()
    lateinit var listKiAdapter: KiOfUserAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_ki, container, false)
    }

    override fun getData() {
        //nothing to get
        listKiOfUser.clear()
        listKiOfUser.addAll(arguments?.getSerializable(Constraint.DATA_SEND) as ArrayList<KiInfo>)
    }

    override fun loadData() {
        if (listKiOfUser.isEmpty())
            showLayoutEmpty()
        else {
            initListKi()
            showListKi()
        }
    }

    override fun addEvent() {
        //nothing to do
        ibtnBack.setOnClickListener {
            if (onBackButtonClick != null)
                onBackButtonClick!!.onClick(ibtnBack)
        }
    }

    private fun initListKi() {
        listKiAdapter = KiOfUserAdapter(mContext, listKiOfUser)
        gridKiStore.numColumns = 2
        gridKiStore.adapter = listKiAdapter

        gridKiStore.setOnItemClickListener { parent, view, position, id ->
            val kiInfo = listKiOfUser[position]
            //listKiOfUser[position].imageBitmap = (view.imgStore.drawable as BitmapDrawable).bitmap
            val intent = Intent(activityParent, InfoStoreActivity::class.java)
            intent.putExtra(InfoStoreActivity.ID_STORE_SEND, kiInfo.pageId)
            intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, kiInfo.pageName)
            startActivity(intent)
        }
    }

    private fun showLayoutEmpty() {
        layoutEmptyKi.visibility = View.VISIBLE
        gridKiStore.visibility = View.GONE
    }

    private fun showListKi(){
        layoutEmptyKi.visibility = View.GONE
        gridKiStore.visibility = View.VISIBLE
    }

    var onBackButtonClick: View.OnClickListener? = null
}
