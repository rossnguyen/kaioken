package agv.kaak.vn.kaioken.mvp.contract

interface CheckTokenContract {
    interface View{
        fun checkTokenValid()
        fun checkTokenInValid(msg: String?)
        fun checkTokenError(msg:String?)
    }

    interface Model{
        fun checkToken(uid:String, sid: String)
    }
}