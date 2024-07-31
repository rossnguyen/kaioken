package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.StoreOfferAdapter
import agv.kaak.vn.kaioken.dialog.DialogDetailPromotion
import agv.kaak.vn.kaioken.entity.Store
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.StoreOnMapContract
import agv.kaak.vn.kaioken.mvp.presenter.StoreOnMapPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_liked.*

class LikedActivity : AppCompatActivity(), StoreOnMapContract.View {

    val listStore: ArrayList<Store> = arrayListOf()
    lateinit var storeLikedAdapter: StoreOfferAdapter
    val storeOnMapPresenter = StoreOnMapPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked)
        addEvent()
        initListLiked()
        layoutLoading.visibility = View.VISIBLE
        if (Constraint.myLocation != null)
            storeOnMapPresenter.getListStoreFollow(Constraint.uid!!.toInt(), Constraint.myLocation!!.latitude, Constraint.myLocation!!.longitude, 0, 0)
    }

    private fun addEvent() {
        ibtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initListLiked() {
        storeLikedAdapter = StoreOfferAdapter(applicationContext, listStore)
        storeLikedAdapter.onStoreClickListener = object : StoreOfferAdapter.OnStoreClickListener {
            override fun onClick(holder: StoreOfferAdapter.ViewHolder, storeData: Store) {
                val intent = Intent(this@LikedActivity, InfoStoreActivity::class.java)
                intent.putExtra(InfoStoreActivity.ID_STORE_SEND, storeData.id)
                intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, storeData.name)
                startActivity(intent)
            }

            override fun onDiscountClick(pageId: Int?, name: String, linkImage: String?) {
                //Waiting for a Nam
                if (pageId == null)
                    return
                val dialogPromotion = DialogDetailPromotion()
                val bundle = Bundle()
                bundle.putInt(Constraint.DATA_SEND, pageId)
                bundle.putString(DialogDetailPromotion.NAME_SEND, name)
                bundle.putString(DialogDetailPromotion.LINK_IMAGE_SEND, linkImage)
                dialogPromotion.arguments = bundle
                dialogPromotion.show(supportFragmentManager, "PROMOTION")
            }
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstLiked.adapter = storeLikedAdapter
        lstLiked.layoutManager = layoutManager
    }

    override fun onGetListStoreOnMapSuccess(data: ArrayList<Store>) {
        //nothing to do
    }

    override fun onGetListStoreOnMapFail(message: String?) {
        //nothing to do
    }

    override fun getListStoreFollowSuccess(data: ArrayList<Store>) {
        layoutLoading.visibility = View.GONE
        listStore.clear()
        listStore.addAll(data)
        storeLikedAdapter.notifyDataSetChanged()

        if (data.isEmpty())
            layoutEmptyLiked.visibility = View.VISIBLE
    }

    override fun getListStoreFollowFail(message: String?) {
        layoutLoading.visibility = View.GONE
        GlobalHelper.showMessage(applicationContext, resources.getString(R.string.map_get_list_follow_fail), true)
        Log.e("****errorGetListOffer", message)
    }

    override fun getListStoreOfferSucces(data: ArrayList<Store>) {
        //nothing to do
    }

    override fun getListStoreOfferFail(message: String?) {
        //nothing to do
    }

    override fun sendLocationAtFirstSuccess() {
        //nothing to do
    }

    override fun sendLocationAtFirstFail(msg: String?) {
        //nothing to do
    }
}

