package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.InfoStoreActivity
import agv.kaak.vn.kaioken.adapter.StoreOfferAdapter
import agv.kaak.vn.kaioken.dialog.DialogDetailPromotion
import agv.kaak.vn.kaioken.entity.Store
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.StoreOnMapContract
import agv.kaak.vn.kaioken.mvp.presenter.StoreOnMapPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.fragment_offer_store.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OfferStoreFragment : BaseFragment(), StoreOnMapContract.View {

    private val listOffer: ArrayList<Store> = arrayListOf()
    private lateinit var adapterOffer: StoreOfferAdapter
    var nothingOfferToLoadMore = false
    var isLoadingMoreOffer = false

    val listStorePresenter = StoreOnMapPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListOffer()
        refreshOffer.setOnRefreshListener {
            reloadListOffer()
            //refreshOffer?.isRefreshing = false
        }
    }

    override fun getData() {
        //nothing to get
    }

    override fun loadData() {
        nothingOfferToLoadMore = false
        listStorePresenter.getListStoreOffer(Constraint.myLocation!!.latitude, Constraint.myLocation!!.longitude, AdjustFragment.distance, listOffer.size, Constraint.LIMIT)
    }

    override fun addEvent() {
        //nothing to do
    }

    override fun onGetListStoreOnMapSuccess(data: ArrayList<Store>) {
        //nothing to do
    }

    override fun onGetListStoreOnMapFail(message: String?) {
        //nothing to do
    }

    override fun getListStoreFollowSuccess(data: ArrayList<Store>) {
        //nothing to do
    }

    override fun getListStoreFollowFail(message: String?) {
        //nothing to do
    }

    override fun sendLocationAtFirstSuccess() {
        //nothing to do
    }

    override fun sendLocationAtFirstFail(msg: String?) {
        //nothing to do
    }

    override fun getListStoreOfferSucces(data: ArrayList<Store>) {
        loadingOffer?.visibility = View.GONE
        loadingMoreOffer?.visibility = View.GONE
        refreshOffer?.isRefreshing = false

        if (data.isEmpty() && !isLoadingMoreOffer) {
            layoutEmptyOffer?.visibility = View.VISIBLE
            listOffer.clear()
            adapterOffer.notifyDataSetChanged()
        } else {
            layoutEmptyOffer?.visibility = View.GONE
            if (!isLoadingMoreOffer)
                listOffer.clear()
            listOffer.addAll(data)
            adapterOffer.notifyDataSetChanged()
        }

        if (data.size < Constraint.LIMIT)
            nothingOfferToLoadMore = true
        isLoadingMoreOffer = false
    }

    override fun getListStoreOfferFail(message: String?) {
        loadingOffer?.visibility = View.GONE
        refreshOffer?.isRefreshing = false
        loadingMoreOffer?.visibility = View.GONE
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.map_get_list_offer_fail), true)
        Log.e("****errorGetListOffer", message)
        isLoadingMoreOffer = false
        nothingOfferToLoadMore = false
    }

    private fun initListOffer() {
        adapterOffer = StoreOfferAdapter(mContext, listOffer)
        lstOffer?.adapter = adapterOffer
        adapterOffer.onStoreClickListener = object : StoreOfferAdapter.OnStoreClickListener {
            override fun onClick(holder: StoreOfferAdapter.ViewHolder, storeData: Store) {
                val intent = Intent(activityParent, InfoStoreActivity::class.java)
                intent.putExtra(InfoStoreActivity.ID_STORE_SEND, storeData.id)
                intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, storeData.name)

                //Convert to byte array
                //because width of image too large
                //so skip send image
                /*val stream = ByteArrayOutputStream()
                storeData.imageBitMap!!.compress(Bitmap.CompressFormat.PNG, 100, stream);
                val byteArray = stream.toByteArray()
                intent.putExtra(InfoStoreActivity.IMAGE_STORE_SEND, byteArray)*/

                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activityParent,

                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        Pair<View, String>(holder.tvName,
                                InfoStoreActivity.NAME_MAP_TRANSITION),
                        Pair<View, String>(holder.imgCover,
                                InfoStoreActivity.COVER_MAP_TRANSITION))

                //startActivity(intent, activityOptions.toBundle())
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
                dialogPromotion.show(mFragmentManager, "PROMOTION")
            }
        }

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        lstOffer?.layoutManager = layoutManager

        lstOffer?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (!nothingOfferToLoadMore && (layoutManager.findLastCompletelyVisibleItemPosition() == listOffer.size - 1)) {
                    listStorePresenter.getListStoreOffer(Constraint.myLocation!!.latitude, Constraint.myLocation!!.longitude, 5000, listOffer.size, Constraint.LIMIT)
                    isLoadingMoreOffer = true
                    loadingMoreOffer?.visibility = View.VISIBLE
                }
            }
        })

        lstOffer?.layoutManager = layoutManager
        lstOffer?.adapter = adapterOffer
    }

    private fun reloadListOffer() {
        loadingOffer?.visibility = View.VISIBLE
        isLoadingMoreOffer = false
        nothingOfferToLoadMore = false
        listStorePresenter.getListStoreOffer(Constraint.myLocation!!.latitude, Constraint.myLocation!!.longitude, AdjustFragment.distance, 0, Constraint.LIMIT)
    }
}
