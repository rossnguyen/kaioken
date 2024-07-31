package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ViewPagerAdapter
import agv.kaak.vn.kaioken.dialog.DialogChooseTypeUse
import agv.kaak.vn.kaioken.dialog.DialogDetailPromotion
import agv.kaak.vn.kaioken.dialog.DialogShowScan
import agv.kaak.vn.kaioken.entity.OrderForm
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.entity.result.DetailStoreResult
import agv.kaak.vn.kaioken.fragment.info_store.*
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.DetailStoreContract
import agv.kaak.vn.kaioken.mvp.contract.FollowStoreConstract
import agv.kaak.vn.kaioken.mvp.presenter.DetailStorePresenter
import agv.kaak.vn.kaioken.mvp.presenter.FollowStorePresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.view.animation.ScaleAnimation
import kotlinx.android.synthetic.main.activity_info_store.*
import android.view.WindowManager
import android.os.Build



class InfoStoreActivity : BaseActivity(), DetailStoreContract.View, FollowStoreConstract.View {
    companion object {
        val ID_STORE_SEND = "ID_STORE_SEND"
        val INFO_STORE_SEND = "INFO_STORE_SEND"
        val NAME_STORE_SEND = "NAME_STORE_SEND"
        val IMAGE_STORE_SEND = "IMAGE_STORE_SEND"

        val NAME_MAP_TRANSITION = "NAME_MAP_TRANSITION"
        val COVER_MAP_TRANSITION = "COVER_MAP_TRANSITION"
    }

    lateinit var mPagerAdapter: ViewPagerAdapter
    val getDetailStorePresenter = DetailStorePresenter(this)
    val followStorePresenter = FollowStorePresenter(this)
    var dialogChooseTypeUse: DialogChooseTypeUse? = null
    var isFollowing = false
    var itemLike: MenuItem? = null
    var idStore = 0
    var nameStore = ""
    var coverStore: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.allowEnterTransitionOverlap = true
        setContentView(R.layout.activity_info_store)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // in Activity's onCreate() for instance
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        //setup toolbar
        setSupportActionBar(mToolbar)
        val actionbar = supportActionBar

        actionbar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        title = ""

        //get data
        idStore = intent.getIntExtra(ID_STORE_SEND, 0)

        //set elevation for bottom navigation
        ViewCompat.setElevation(layoutBottomNavigate, resources.getDimension(R.dimen.all_masterial_elevation_bottom_nav_bar))
        ViewCompat.setElevation(btnChooseOrder, resources.getDimension(R.dimen.all_masterial_elevation_raised_button_rest))

        //add event
        addEvent()

        //load data
        ViewCompat.setTransitionName(imgCover, COVER_MAP_TRANSITION)
        ViewCompat.setTransitionName(collapsing_toolbar, COVER_MAP_TRANSITION)

        getData()

        getDetailStorePresenter.getDetailStore(idStore, Constraint.uid!!.toInt())
    }

    override fun onBackPressed() {
        Constraint.backFromInfoStore = true
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_store_info, menu)
        itemLike = menu!!.findItem(R.id.ibtnLike)
        if (isFollowing)
            itemLike?.setIcon(R.drawable.ic_heart_bold)
        else
            itemLike?.setIcon(R.drawable.ic_heart)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.ibtnLike -> {
                followStorePresenter.followStore(Constraint.uid!!.toInt(), idStore)
            }
        }
        return false
    }

    fun getData() {
        nameStore = intent.getStringExtra(NAME_STORE_SEND)
        val byteArray = intent?.getByteArrayExtra(IMAGE_STORE_SEND)
        if (byteArray != null)
            coverStore = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imgCover.setImageBitmap(coverStore)
        collapsing_toolbar.title = nameStore
    }

    fun loadData(data: DetailStoreResult) {
        nameStore = data.name!!
        collapsing_toolbar.title = nameStore
        //ViewCompat.setElevation(findViewById(R.id.header), resources.getDimension(R.dimen.toolbar_elevation))
        ImageHelper.loadImage(applicationContext, imgCover, data.cover, PlaceHolderType.NONE)
        //bind data to viewpager
        initViewPager(data)

        initDialogChooseType(data.id)
        //collapsing_toolbar.title = data.name

        isFollowing = (data.pageFollow == 1)
        if (isFollowing)
            itemLike?.setIcon(R.drawable.ic_heart_bold)
        else
            itemLike?.setIcon(R.drawable.ic_heart)


    }

    private fun initViewPager(storeInfo: DetailStoreResult) {
        mPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        //add tab Menu
        val storeMenuFragment = StoreMenuFragment()
        val bundle = Bundle()
        bundle.putSerializable(Constraint.DATA_SEND, storeInfo)
        bundle.putString(DialogDetailPromotion.NAME_SEND, storeInfo.name)
        bundle.putString(DialogDetailPromotion.LINK_IMAGE_SEND, storeInfo.cover)
        storeMenuFragment.arguments = bundle
        mPagerAdapter.addFragment(storeMenuFragment, resources.getString(R.string.all_menu))

        //add tab info
        val storeDetailFragment = StoreDetailFragment()
        storeDetailFragment.arguments = bundle
        mPagerAdapter.addFragment(storeDetailFragment, resources.getString(R.string.all_info))

        //add tab comment
        mPagerAdapter.addFragment(CommentFragment(), resources.getString(R.string.all_comment))

        //add tab image
        val imageStoreFragment = ImageStoreFragment()
        mPagerAdapter.addFragment(imageStoreFragment, resources.getString(R.string.all_image))

        //set up viewpager
        pager.adapter = mPagerAdapter
        tabLayout.setupWithViewPager(pager)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //nothing to do
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //nothing to do
            }

            override fun onPageSelected(position: Int) {
                val positionComment = 2
                if (position == positionComment)
                    showButtonAddComment()
                else
                    hideButtonAddComment()
            }
        })
    }

    private fun initDialogChooseType(pageId: Int) {
        dialogChooseTypeUse = DialogChooseTypeUse(this@InfoStoreActivity, pageId)
        dialogChooseTypeUse?.callBack = object : DialogChooseTypeUse.OnItemChooseTypeUseClick {
            override fun onClick(type: Int) {
                Constraint.TYPE_USE = type
                Constraint.ID_STORE_ORDERING = idStore

                //show scan QR if choose local
                when (type) {
                    UseType.LOCAL_HAVE_TABLE -> {
                        //show scan QR
                        showScanQR()
                    }
                    else -> {
                        startActivity(Intent(this@InfoStoreActivity, OrderActivity::class.java))
                        Constraint.NAME_STORE_ORDERING = nameStore
                    }
                }
                dialogChooseTypeUse?.dismiss()
            }
        }
    }

    private fun addEvent() {
        btnChooseOrder.setOnClickListener {
            dialogChooseTypeUse?.show()
        }

        btnAddComment.setOnClickListener {
            if (btnAddComment.visibility == View.VISIBLE)
                startActivity(Intent(this@InfoStoreActivity, CreateCommentActivity::class.java))
        }
    }

    private fun showScanQR() {
        val dialogShowScan = DialogShowScan()
        dialogShowScan.show(supportFragmentManager, "SCAN")
    }

    override fun getDetailStoreSuccess(data: DetailStoreResult) {
        Log.d("${Constraint.TAG_LOG}StoreId", data.id.toString())
        Constraint.ID_STORE_ORDERING = data.id
        pager.visibility = View.VISIBLE
        loadData(data)
    }

    override fun getDetailStoreFail(msg: String?) {
        //layoutLoading.visibility = View.GONE
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(applicationContext, msg, true)
    }

    override fun onConnectSocketSuccess() {
        //nothing to do
    }

    override fun onConnectSocketFail() {
        //nothing to do
    }

    override fun followStoreSuccess() {
        isFollowing = !isFollowing

        if (isFollowing)
            itemLike?.setIcon(R.drawable.ic_heart_bold)
        else
            itemLike?.setIcon(R.drawable.ic_heart)
    }

    override fun followStoreFail(msg: String?) {
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(applicationContext, msg, true)
    }

    private fun showButtonAddComment() {
        if (btnAddComment.visibility == View.GONE) {
            btnAddComment.visibility = View.VISIBLE
            val scaleAnimation = ScaleAnimation(0f,
                    1f,
                    0f,
                    1f,
                    btnAddComment.pivotX,
                    btnAddComment.pivotY)
            scaleAnimation.fillAfter = true
            scaleAnimation.duration = 300
            btnAddComment.startAnimation(scaleAnimation)
        }
    }

    private fun hideButtonAddComment() {
        if (btnAddComment.visibility == View.VISIBLE) {
            val scaleAnimation = ScaleAnimation(1f,
                    0f,
                    1f,
                    0f,
                    btnAddComment.pivotX,
                    btnAddComment.pivotY)
            scaleAnimation.fillAfter = true
            scaleAnimation.duration = 300
            btnAddComment.startAnimation(scaleAnimation)
            btnAddComment.visibility = View.GONE
        }
    }
}
