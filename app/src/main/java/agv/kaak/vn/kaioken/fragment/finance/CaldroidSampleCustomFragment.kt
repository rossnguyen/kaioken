package agv.kaak.vn.kaioken.fragment.finance

import agv.kaak.vn.kaioken.adapter.CaldroidSampleCustomAdapter
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidGridAdapter

class CaldroidSampleCustomFragment:CaldroidFragment() {

    override fun getNewDatesGridAdapter(month: Int, year: Int): CaldroidGridAdapter {
        return CaldroidSampleCustomAdapter(activity!!, month, year, getCaldroidData(), extraData)
    }
}