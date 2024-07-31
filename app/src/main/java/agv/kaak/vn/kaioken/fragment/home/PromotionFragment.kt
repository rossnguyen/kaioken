package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.*
import agv.kaak.vn.kaioken.adapter.NewsAdapter
import agv.kaak.vn.kaioken.adapter.PromotionAdapter
import agv.kaak.vn.kaioken.entity.DetailNews
import agv.kaak.vn.kaioken.entity.News
import agv.kaak.vn.kaioken.entity.Promotion
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.KiOfPageResult
import agv.kaak.vn.kaioken.entity.result.KiOfUserResult
import agv.kaak.vn.kaioken.entity.result.UserInfo
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.KiContract
import agv.kaak.vn.kaioken.mvp.contract.NewsContract
import agv.kaak.vn.kaioken.mvp.contract.PromotionContract
import agv.kaak.vn.kaioken.mvp.contract.UserContract
import agv.kaak.vn.kaioken.mvp.presenter.KiPresenter
import agv.kaak.vn.kaioken.mvp.presenter.NewsPresenter
import agv.kaak.vn.kaioken.mvp.presenter.PromotionPresenter
import agv.kaak.vn.kaioken.mvp.presenter.UserPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.icu.text.IDNA
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.fragment_promotion.*
import kotlin.math.roundToInt


class PromotionFragment : BaseFragment(), PromotionContract.View,
        NewsContract.View, SwipeRefreshLayout.OnRefreshListener,
        KiContract.View,
        View.OnClickListener {

    private var listNews: ArrayList<News> = arrayListOf()
    private var listPromotion: ArrayList<Promotion> = arrayListOf()

    private var newsAdapter: NewsAdapter? = null
    private var promotionAdapter: PromotionAdapter? = null

    private val promotionPresenter = PromotionPresenter(this)
    private val newsPresenter = NewsPresenter(this)
    private val kiPresenter = KiPresenter(this)

    private var countRefresh = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        initListNewAdapter()
        initListPromotionAdapter()
        promotionPresenter.getListPromotion()
        newsPresenter.getListNews()
        kiPresenter.getListKiOfUser(0, 1)
    }

    override fun getData() {
        //nothing to get
    }

    override fun loadData() {

    }

    override fun addEvent() {
        layoutRefresh.setOnRefreshListener(this)
        ibtnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        layoutUserInfo.setOnClickListener(this)
        layoutKi.setOnClickListener(this)
        imgAvatar.setOnClickListener(this)
        tvLike.setOnClickListener(this)
        tvContact.setOnClickListener(this)
        tvSettings.setOnClickListener(this)
        tvLogout.setOnClickListener(this)
    }

    private fun initListNewAdapter() {
        newsAdapter = NewsAdapter(mContext, listNews)
        newsAdapter?.onItemClickListener = object : NewsAdapter.OnItemClickListener {
            override fun onClick(newsId: Int, title: String) {
                showDetailNews(newsId, title)
            }
        }
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        lstNews.adapter = newsAdapter
        val scaleTransformer = ScaleTransformer.Builder()
                .setMaxScale(1f)
                .setMinScale(0.9f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build()
        lstNews.setItemTransformer(scaleTransformer)
        lstNews.setOffscreenItems(5)

        //lstNews.setSlideOnFlingThreshold(1000)
    }

    private fun initListPromotionAdapter() {
        promotionAdapter = PromotionAdapter(mContext, listPromotion)
        promotionAdapter?.onItemPromotionClick = object : PromotionAdapter.OnItemPromotionClick {
            override fun onCLick(promotionId: Int, promotionTitle: String) {
                showDetailNews(promotionId, promotionTitle)
            }

            override fun onPageClick(pageId: Int?) {
                goToDetailStore(pageId)
            }
        }
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstPromotion.adapter = promotionAdapter
        lstPromotion.layoutManager = layoutManager
    }

    override fun getListPromotionSuccess(listPromotion: ArrayList<Promotion>) {

        this.listPromotion.clear()
        this.listPromotion.addAll(listPromotion)
        promotionAdapter?.notifyDataSetChanged()

        countRefresh++
        updateRefresh()
    }

    override fun getListPromotionFail(msg: String?) {

        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(mContext, resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, msg, true)

        countRefresh++
        updateRefresh()
    }

    override fun getListNewsSuccess(listNews: ArrayList<News>) {

        this.listNews.clear()
        this.listNews.addAll(listNews)
        newsAdapter?.notifyDataSetChanged()
        if (listNews.size > 2)
            lstNews?.scrollToPosition(1)

        countRefresh++
        updateRefresh()
    }

    override fun getListNewFail(msg: String?) {
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(mContext, resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, msg, true)

        countRefresh++
        updateRefresh()
    }

    override fun getDetailNewsSuccess(detailNews: DetailNews) {
        //nothing to do

    }

    override fun getDetailNewsFail(msg: String?) {
        //nothing to do

    }

    override fun getListKiOfUserSuccess(listKiOfUser: KiOfUserResult) {

        tvName.text = listKiOfUser.name
        if (listKiOfUser.totalKi != null)
            tvCoint.text = "${listKiOfUser.totalKi!!.roundToInt()}"
        else
            tvCoint.text = "0"
        ImageHelper.loadImage(mContext, imgAvatar, listKiOfUser.linkAvatar, PlaceHolderType.AVATAR)

        ImageHelper.loadImage(mContext, imgUserCover, listKiOfUser.coverSmall, PlaceHolderType.IMAGE)
        ImageHelper.loadImage(mContext, imgUserAvatar, listKiOfUser.linkAvatar, PlaceHolderType.AVATAR)
        tvUserName.text = listKiOfUser.name
        tvUserPhone.text = listKiOfUser.phone

        countRefresh++
        updateRefresh()
    }

    override fun getListKiOfUserFail(msg: String?) {

        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(mContext, resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, msg, true)

        countRefresh++
        updateRefresh()
    }

    override fun getKiOfPageSuccess(kiOfPage: KiOfPageResult) {
        //nothing to do
    }

    override fun getKiOfPageFail(msg: String?) {
        //nothing to do
    }

    override fun onRefresh() {
        countRefresh = 0
        promotionPresenter.getListPromotion()
        newsPresenter.getListNews()
        kiPresenter.getListKiOfUser(0, 1)
    }

    override fun onClick(v: View?) {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (v) {
            layoutUserInfo -> goToActivityInfoUser()
            imgAvatar -> goToActivityInfoUser()
            layoutKi -> showFragmentKi()
            tvContact -> showDialogContact()
            tvLogout -> GlobalHelper.doLogout(activityParent)
            tvLike -> showListLiked()
            else -> GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_feature_is_developing), true)
        }

    }

    private fun updateRefresh() {
        if (countRefresh == 3)
            layoutRefresh?.isRefreshing = false
    }

    private fun goToActivityInfoUser() {
        startActivity(Intent(activityParent, InfoUserActivity::class.java))
    }

    private fun showFragmentKi() {
        if (onFeaturesClickListenter != null)
            onFeaturesClickListenter!!.onKiClick()
    }

    private fun showListLiked() {
        startActivity(Intent(activityParent, LikedActivity::class.java))
    }

    private fun showDetailNews(newsId: Int, title: String) {
        val intentToDetailNews = Intent(activityParent, DetailNewsActivity::class.java)
        intentToDetailNews.putExtra(Constraint.DATA_SEND, newsId)
        intentToDetailNews.putExtra(DetailNewsActivity.TITLE_NEWS_SEND, title)
        startActivity(intentToDetailNews)
    }

    private fun goToDetailStore(pageId: Int?) {
        val intentDetailStore = Intent(activityParent, InfoStoreActivity::class.java)
        intentDetailStore.putExtra(InfoStoreActivity.NAME_STORE_SEND, "")
        intentDetailStore.putExtra(InfoStoreActivity.ID_STORE_SEND, pageId)
        startActivity(intentDetailStore)
    }

    private fun showDialogContact() {
        val intentContact = Intent(activityParent, AboutActivity::class.java)
        startActivity(intentContact)
    }

    var onFeaturesClickListenter: OnFeaturesClickListener? = null

    interface OnFeaturesClickListener {
        fun onKiClick()
        fun onNotifyClick()
    }
}
