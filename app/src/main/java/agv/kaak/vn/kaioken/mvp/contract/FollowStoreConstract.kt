package agv.kaak.vn.kaioken.mvp.contract

interface FollowStoreConstract {
    interface View{
        fun followStoreSuccess()
        fun followStoreFail(msg:String?)
    }

    interface Model{
        fun followStore(uid:Int, pageId:Int)
    }
}