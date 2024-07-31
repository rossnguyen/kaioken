package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.City

interface ListCityContract {
    interface View{
        fun getListCitySuccess(listCity:ArrayList<City>)
        fun getListCityFail(msg:String?)
    }

    interface Model{
        fun getListCity()
    }
}