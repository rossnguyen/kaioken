package agv.kaak.vn.kaioken.fragment.info_store


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ShowImageStoreAdapter
import agv.kaak.vn.kaioken.dialog.ShowImageFoodFragment
import agv.kaak.vn.kaioken.entity.result.ImageDetailRestautantResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.GetListImageStoreContract
import agv.kaak.vn.kaioken.mvp.presenter.GetListImageStorePresenter
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.widget.GridSpacingDecoration
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.fragment_image_store.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ImageStoreFragment : BaseFragment(), GetListImageStoreContract.View {

    private val getListImagePresenter = GetListImageStorePresenter(this)
    val sourceImage: ArrayList<String> = arrayListOf()
    var adapterImage:ShowImageStoreAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListImage()
    }

    override fun getData() {
        //nothing to do
    }

    override fun loadData() {
        getListImagePresenter.getListImageStore(Constraint.ID_STORE_ORDERING)
    }

    override fun addEvent() {
        //nothing to do
    }

    private fun bindData(listImage: ArrayList<ImageDetailRestautantResult>) {
        sourceImage.clear()
        listImage.forEach {
            sourceImage.add(it.url!!)
        }
        adapterImage?.notifyDataSetChanged()
    }

    override fun getListImageStoreSuccess(listImage: ArrayList<ImageDetailRestautantResult>) {
        bindData(listImage)
        showEmptyLayout(listImage.isEmpty())
    }

    override fun getListImageStoreFail(message: String) {
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_get_data_fail_message), true)
        Log.e("****errorGetImage", message)
    }

    private fun showEmptyLayout(isEmpty: Boolean) {
        if (isEmpty)
            layoutEmptyImage?.visibility = View.VISIBLE
        else
            layoutEmptyImage?.visibility = View.GONE
    }

    private fun initListImage(){
        adapterImage=ShowImageStoreAdapter(mContext, sourceImage)
        adapterImage?.onItemtemClickListener = object : ShowImageStoreAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                val showImageFoodFragment = ShowImageFoodFragment()
                val bundle = Bundle()
                bundle.putStringArrayList(ShowImageFoodFragment.IMAGE_SEND, sourceImage)
                bundle.putInt(ShowImageFoodFragment.POSITION_SENT, position)
                showImageFoodFragment.arguments = bundle
                showImageFoodFragment.show(mFragmentManager, "SHOW_IMAGE")
            }
        }
        val layoutManager = GridLayoutManager(mContext, 3)
        gridImage?.layoutManager = layoutManager

        gridImage?.adapter = adapterImage
        gridImage?.addItemDecoration(GridSpacingDecoration(ConvertHelper.dpToPx(mContext, 2), 3))
    }
}
