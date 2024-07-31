package agv.kaak.vn.kaioken.utils

import agv.kaak.vn.kaioken.R
import android.content.Context
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by shakutara on 27/02/2018.
 */
class DateUtils {

    companion object {
        fun convertStringToDate(dateString: String): Date? {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                return format.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun convertStringToDate(dateString: String, format: String): Date? {
            val format = SimpleDateFormat(format)
            try {
                return format.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun convertDateToString(myDate: Date, format: String): String? {
            val dateFormat = SimpleDateFormat(format)
            try {
                return dateFormat.format(myDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun convertDateToStringShort(myDate: Date): String? {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            try {
                return dateFormat.format(myDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun convertDateToStringFull(myDate: Date): String? {
            val dateFormat = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy")
            try {
                return dateFormat.format(myDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun convertDateToYearString(myDate: Date): String? {
            val dateFormat = SimpleDateFormat("yyyy")
            try {
                return dateFormat.format(myDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun convertDateToMonthYearString(myDate: Date): String? {
            val dateFormat = SimpleDateFormat("MM/yyyy")
            try {
                return dateFormat.format(myDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun convertDateToTimeFull(myDate: Date): String? {
            val timeFormat = SimpleDateFormat("HH:mm:ss")
            try {
                return timeFormat.format(myDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun getDistanceTimeTMP0(context: Context, date: Date?): String {
            if (date == null)
                return context.getString(R.string.all_unknown)
            else {
                //because date get from server have +0 TMP but equal value of +7 TMP
                //so add now to 7 hour
                //or minus date to 7 hour
                var cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.HOUR_OF_DAY, -7)

                val date7TMP = cal.time

                val now = Date()
                val diff = now.time - date7TMP.time
                val diffSeconds = diff / 1000 % 60
                val diffMinutes = diff / (60 * 1000) % 60
                val diffHours = diff / (60 * 60 * 1000) % 24
                val diffDays = diff / (24 * 60 * 60 * 1000)

                if (diffDays > 1)
                    return context.getString(R.string.all_day_at, date7TMP.date, date7TMP.month, date7TMP.hours, date7TMP.minutes)
                else {
                    if (diffDays > 0)
                        return context.getString(R.string.all_yesterday_at, date7TMP.hours, date7TMP.minutes)
                    else {
                        if (diffHours > 0)
                            return context.getString(R.string.all_x_hour_before, diffHours)
                        else
                            return context.getString(R.string.all_x_minus_before, diffMinutes)
                    }
                }
            }
        }

        fun getDistanceTimeTMP7(context: Context, date: Date?): String {
            if (date == null)
                return context.getString(R.string.all_unknown)
            else {
                val now = Date()

                val diff = now.time - date.time
                val diffSeconds = diff / 1000 % 60
                val diffMinutes = diff / (60 * 1000) % 60
                val diffHours = diff / (60 * 60 * 1000) % 24
                val diffDays = diff / (24 * 60 * 60 * 1000)

                if (diffDays > 1)
                    return context.getString(R.string.all_day_at, date.date, date.month, date.hours, date.minutes)
                else {
                    if (diffDays > 0)
                        return context.getString(R.string.all_yesterday_at, date.hours, date.minutes)
                    else {
                        if (diffHours > 0)
                            return context.getString(R.string.all_x_hour_before, diffHours)
                        else
                            return context.getString(R.string.all_x_minus_before, diffMinutes)
                    }
                }
            }
        }


    }

}