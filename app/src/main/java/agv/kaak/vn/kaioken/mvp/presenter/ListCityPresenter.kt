package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.ListCityContract
import agv.kaak.vn.kaioken.mvp.model.ListCityModel

class ListCityPresenter(val view:ListCityContract.View) {
    val model=ListCityModel(view)

    fun getListCity(){
        model.getListCity()
    }
}