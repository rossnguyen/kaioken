package agv.kaak.vn.kaioken.fragment.info_store


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.CreateCommentActivity
import agv.kaak.vn.kaioken.adapter.CommentStoreAdapter
import agv.kaak.vn.kaioken.dialog.ShowImageFoodFragment
import agv.kaak.vn.kaioken.entity.result.CreateStoreCommentResult
import agv.kaak.vn.kaioken.entity.result.GetStoreCommentResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CommentStoreContract
import agv.kaak.vn.kaioken.mvp.presenter.CommentStorePresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_comment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CommentFragment : BaseFragment(), CommentStoreContract.View{
    private lateinit var adapterComment: CommentStoreAdapter
    private var restaurantCommentPresenter: CommentStorePresenter = CommentStorePresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun getData() {

    }

    override fun loadData() {
        loading_bar.visibility = View.VISIBLE
        restaurantCommentPresenter.getComment(Constraint.ID_STORE_ORDERING)
    }

    override fun addEvent() {
        //nothing to do
    }

    override fun onCreateCommentSuccess(data: CreateStoreCommentResult) {
        //nothing to do
    }

    override fun onCreateCommentFail(message: String) {
        //nothing to do
    }

    override fun onGetCommentSuccess(data: ArrayList<GetStoreCommentResult>) {
        loading_bar?.visibility = View.GONE
        loadListComment(data)
        showEmptyLayout(data.isEmpty())
    }

    override fun onGetCommentFail(message: String) {
        loading_bar?.visibility = View.GONE
        GlobalHelper.showMessage(mContext, activityParent.getString(R.string.all_get_data_fail_message), true)
    }


    //load list comment from server
    private fun loadListComment(data: ArrayList<GetStoreCommentResult>?) {
        adapterComment = CommentStoreAdapter(mContext, data!!)
        adapterComment.onImageClicked = object : CommentStoreAdapter.OnImageClicked {
            override fun onClick(sourceImgae: java.util.ArrayList<String>, positionComment: Int, positionImage: Int) {
                //show expand image
                val showImageFoodFragment = ShowImageFoodFragment()
                val bundle = Bundle()
                bundle.putStringArrayList(ShowImageFoodFragment.IMAGE_SEND, sourceImgae)
                bundle.putInt(ShowImageFoodFragment.POSITION_SENT, positionImage)
                showImageFoodFragment.arguments = bundle
                showImageFoodFragment.show(mFragmentManager, "ShowImage")
            }
        }
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstComment?.adapter = adapterComment
        lstComment?.layoutManager = layoutManager
    }

    private fun showEmptyLayout(isShow: Boolean) {
        if (isShow)
            layoutEmptyComment?.visibility = View.VISIBLE
        else
            layoutEmptyComment?.visibility = View.GONE
    }
}
