package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.DetailNews
import agv.kaak.vn.kaioken.entity.News

interface NewsContract {
    interface View{
        fun getListNewsSuccess(listNews:ArrayList<News>)
        fun getListNewFail(msg:String?)

        fun getDetailNewsSuccess(detailNews:DetailNews)
        fun getDetailNewsFail(msg:String?)
    }

    interface Model{
        fun getListNews()
        fun getDetailNews(newsId:Int)
    }
}