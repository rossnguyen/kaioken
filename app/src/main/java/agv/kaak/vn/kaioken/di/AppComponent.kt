package agv.kaak.vn.kaioken.di

import agv.kaak.vn.kaioken.activity.BaseActivity
import agv.kaak.vn.kaioken.activity.HomeActivity
import agv.kaak.vn.kaioken.fragment.base.BaseFragment_
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(homeActivity: HomeActivity)
    fun inject(baseActivity: BaseActivity)
    fun inject(baseFragment_: BaseFragment_)
}