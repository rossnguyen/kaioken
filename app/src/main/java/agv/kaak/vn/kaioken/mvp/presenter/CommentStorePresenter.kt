package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.CommentStoreContract
import agv.kaak.vn.kaioken.mvp.model.CommentStoreModel
import java.util.ArrayList

class CommentStorePresenter(val view:CommentStoreContract.View) {
    val commentStoreModel:CommentStoreModel

    init {
        commentStoreModel= CommentStoreModel(view)
    }

    fun createComment(uId: String, sId: String, pageId: Int, userId: Int, content: String, listLinkImage: ArrayList<String>){
        this.commentStoreModel.createComment(uId, sId, pageId, userId, content, listLinkImage)
    }
    fun getComment(pageId: Int){
        this.commentStoreModel.getComment(pageId)
    }
}