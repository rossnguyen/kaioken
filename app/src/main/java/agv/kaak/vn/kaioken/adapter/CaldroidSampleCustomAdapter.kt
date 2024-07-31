package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.finance.FinanceFragment
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidGridAdapter
import agv.kaak.vn.kaioken.entity.response.wallet.ItemWallet
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.custom_cell_calendar.view.*
import java.util.*

class CaldroidSampleCustomAdapter(context: Context, month: Int, year: Int,
                                  caldroidData: Map<String, Any>,
                                  extraData: Map<String, Any>) : CaldroidGridAdapter(context, month, year, caldroidData, extraData) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var cellView = convertView

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell_calendar, null)
        }

        val topPadding = cellView!!.paddingTop
        val leftPadding = cellView.paddingLeft
        val bottomPadding = cellView.paddingBottom
        val rightPadding = cellView.paddingRight

        val tvDay = cellView.tvDay
        val imgMoney = cellView.imgMoney

        tvDay.setTextColor(Color.BLACK)

        // Get dateTime of this cell
        val dateTime = this.datetimeList[position]
        val resources = context.resources

        // Set color of the dates in previous / next month
        if (dateTime.month != month) {
            tvDay.setTextColor(Color.WHITE)
        }

        var shouldResetDiabledView = false
        var shouldResetSelectedView = false

        // Customize for disabled dates and date outside min/max dates
        if (minDateTime != null && dateTime.lt(minDateTime)
                || maxDateTime != null && dateTime.gt(maxDateTime)
                || disableDates != null && disableDates.indexOf(dateTime) != -1) {

            tvDay.setTextColor(CaldroidFragment.disabledTextColor)
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell)
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable)
            }

        } else {
            shouldResetDiabledView = true
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGreenAppPrimary))
            tvDay.setTextColor(Color.BLACK)
        } else {
            shouldResetSelectedView = true
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime == getToday()) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border)
                tvDay.setTextColor(ContextCompat.getColor(context,R.color.colorGreenAppPrimary))

                tvDay.typeface = Typeface.DEFAULT_BOLD
            } else {
                cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg)
            }
        }

        tvDay.text = "" + dateTime.day!!

        val data = getExtraData()["DATA_CALDROID"] as HashMap<Date, ArrayList<ItemWallet>>
        val spentMoneyArrayList = data[FinanceFragment.parseDate(year.toString() + "-" + month + "-" + dateTime.day)]
        if (spentMoneyArrayList != null && dateTime.month == month) {
            var tongTien = 0
            for (i in spentMoneyArrayList.indices) {
                tongTien += Integer.parseInt(spentMoneyArrayList[i].amount!!)
            }
            imgMoney.visibility = View.VISIBLE
        } else
            imgMoney.visibility = View.INVISIBLE


        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding)

        // Set custom color if required
        setCustomResources(dateTime, cellView, tvDay)

        return cellView
    }
}