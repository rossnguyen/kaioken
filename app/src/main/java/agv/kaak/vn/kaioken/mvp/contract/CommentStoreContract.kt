package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.CreateStoreCommentResult
import agv.kaak.vn.kaioken.entity.result.GetStoreCommentResult
import java.util.ArrayList

interface CommentStoreContract {
    interface View{
        fun onCreateCommentSuccess(data: CreateStoreCommentResult)
        fun onCreateCommentFail(message: String)

        fun onGetCommentSuccess(data: ArrayList<GetStoreCommentResult>)
        fun onGetCommentFail(message: String)
    }

    interface Model{
        fun createComment(uId: String, sId: String, pageId: Int, userId: Int, content: String, listLinkImage: ArrayList<String>)
        fun getComment(pageId: Int)
    }
}