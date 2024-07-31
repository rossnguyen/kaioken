package agv.kaak.vn.kaioken.helper

import agv.kaak.vn.kaioken.R
import android.content.Context
import android.util.Log
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class ConvertHelper {
    companion object {
        fun dpToPx(context: Context, dp: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            return (dp * displayMetrics.density + 0.5).toInt()
        }

        fun pxTodp(context: Context, px: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            return (px / displayMetrics.density + 0.5).toInt()
        }

        fun stringGlobalToDateTimeWithoutTimezone(string: String?): Date? {
            var result: Date? = null
            val sdp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                if (string !== "") {
                    result = sdp.parse(string)
                    return result
                } else
                    return null

            } catch (ex: Exception) {
                Log.e("errorTime", ex.message)
                return null
            }

        }

        fun stringGlobalToDateTime(string: String): Date? {
            var result: Date? = null
            val sdp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sdp.timeZone = TimeZone.getTimeZone("GMT")
            try {
                if (string !== "") {
                    result = sdp.parse(string)
                    return result
                } else
                    return null

            } catch (ex: Exception) {
                Log.e("errorTime", ex.message)
                return null
            }

        }

        fun stringToDateTime(string: String): Date? {
            var result: Date? = null
            val sdp = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            try {
                if (string !== "") {
                    result = sdp.parse(string)
                    return result
                } else
                    return null

            } catch (ex: Exception) {
                Log.e("errorTime", ex.message)
                return null
            }

        }

        fun dateToString(date: Date?): String {
            var result = ""
            val sdp = SimpleDateFormat("dd-MM-yyyy")
            if (date != null) {
                result = sdp.format(date)
            }

            return result
        }

        fun dateTimeToString(date: Date?): String {
            var result = ""
            val sdp = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            if (date != null) {
                result = sdp.format(date)
            }

            return result
        }

        fun dateToStringGlobal(date: Date?): String {
            var result = ""
            val sdp = SimpleDateFormat("yyyy-MM-dd")
            if (date != null) {
                result = sdp.format(date)
            }

            return result
        }

        fun dateTimeToStringGlobal(date: Date?): String {
            var result = ""
            val sdp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            if (date != null) {
                result = sdp.format(date)
            }

            return result
        }


        fun dateTimeToStringWithoutTimeZone(date: Date): String {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.HOUR, -7)
            var result = ""
            val sdp = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            result = sdp.format(cal.time)
            return result
        }

        fun removeAccents(text: String?): String? {
            return if (text == null)
                null
            else
                Normalizer.normalize(text, Normalizer.Form.NFD)
                        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "").replace("đ".toRegex(), "d").replace("Đ".toRegex(), "D")
        }

        fun reverseDate(dateInput: String, regex: String): String {
            val year = dateInput.split(regex)[0]
            val month = dateInput.split(regex)[1]
            val day = dateInput.split(regex)[2]

            return "$day-$month-$year"
        }

        fun doubleToMoney(mContext: Context, value: Double): String {
            var result = 0.toDouble()
            result = (value * 1000).toInt().toDouble() / 1000
            if (result % 1 == 0.toDouble())
                return "${result.toInt()}K"
            else
                return "${result}K"
        }

        fun doubleToKi(mContext: Context, value: Double): String {
            var result = 0.toDouble()
            result = (value * 1000).toInt().toDouble() / 1000
            if (result % 1 == 0.toDouble())
                return "${result.toInt()}Ki"
            else
                return "${result}Ki"
        }
    }
}