package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.NewsContract
import agv.kaak.vn.kaioken.mvp.model.NewsModel

class NewsPresenter(val view:NewsContract.View) {
    val model=NewsModel(view)

    fun getListNews(){
        model.getListNews()
    }

    fun getDetailNews(newsId:Int){
        model.getDetailNews(newsId)
    }
}