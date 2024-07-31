package agv.kaak.vn.kaioken.mvp.contract

interface LogoutContract {
    interface View{
        fun logoutSuccess()
        fun logoutFail(msg:String?)
    }

    interface Model{
        fun logout()
    }
}