package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.DetailNews
import agv.kaak.vn.kaioken.entity.News
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.NewsContract
import agv.kaak.vn.kaioken.mvp.presenter.NewsPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import kotlinx.android.synthetic.main.activity_detail_news.*

class DetailNewsActivity : YouTubeFailureRecoveryActivity(), NewsContract.View {
    companion object {
        val TITLE_NEWS_SEND = "TITLE_NEWS_SEND"
    }

    var news_id = -1
    var title = ""
    var youtubeId = ""
    val detailNewsPresenter = NewsPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)

        getData()
        addEvent()
        loadData()
    }

    private fun getData() {
        news_id = intent.getIntExtra(Constraint.DATA_SEND, -1)
        title = intent.getStringExtra(TITLE_NEWS_SEND)
    }

    private fun loadData() {
        tvTitle.text = title
        detailNewsPresenter.getDetailNews(news_id)
    }

    private fun addEvent() {
        ibtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun getListNewsSuccess(listNews: ArrayList<News>) {
        //nothing to do
    }

    override fun getListNewFail(msg: String?) {
        //nothing to do
    }

    override fun getDetailNewsSuccess(detailNews: DetailNews) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            process.setProgress(100, true)
        }
        process.isIndeterminate = false
        val content = detailNews.content


        val head = "<head><style>img{max-width: 100%; width:auto; height: auto;}a{color: blue;text-decoration: none;}</style></head>"
        wvContent.loadData("<html>$head<body>$content</body></html>", "text/html", "UTF-8")

        if (detailNews.banner != null && detailNews.banner!!.isNotEmpty()) {
            layoutVideo.visibility = View.VISIBLE
            ImageHelper.loadImage(applicationContext, imgThumbnail, detailNews.banner, PlaceHolderType.IMAGE)
        }

        //load youtube
        if (detailNews.youTubeId != null && detailNews.youTubeId!!.isNotEmpty()) {
            youtubeId = detailNews.youTubeId!!
            val youTubePlayerFragment = fragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment
            youTubePlayerFragment.initialize(applicationContext.resources.getString(R.string.google_maps_key), this)
        } else {
            layoutYoutube.visibility = View.GONE
        }

    }

    override fun getDetailNewsFail(msg: String?) {
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(applicationContext, msg, true)
    }

    override fun getYouTubePlayerProvider(): YouTubePlayer.Provider {
        return fragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
        if (!p2)
            p1!!.cueVideo(youtubeId)
    }
}
