package agv.kaak.vn.kaioken.utils

import agv.kaak.vn.kaioken.R
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import io.socket.client.Socket
import retrofit2.Retrofit

class Constraint {
    companion object {
        // Shared Preferences name
        val SHARE_PRE_INFO_USER = "INFO_USER"
        val SHARE_PRE_CLOUD_MESSAGING = "SHARE_PRE_CLOUD_MESSAGING"
        val SHARED_PRE_CLOUD_MESSAGING_TOKEN = "SHARED_PRE_CLOUD_MESSAGING_TOKEN"
        val SHARE_PRE_PRINTER = "SHARE_PRE_PRINTER"
        val SHARE_PRE_INVENTORY = "SHARE_PRE_INVENTORY"

        val INFO_USER_NAME_KEY = "USER_NAME"
        val INFO_USER_EMAIL_KEY = "USER_EMAIL"
        val INFO_USER_ID_KEY = "USER_ID"
        val INFO_USER_SID_KEY = "SID"
        val INFO_USER_LOGIN_TYPE = "LOGIN_TYPE"
        val INFO_USER_AVATAR_KEY = "AVATAR"
        val INFO_USER_PHONE_KEY = "PHONE"
        val INFO_USER_SHOW_EXPLANATION = "SHOW_EXPLANATION"
        val INFO_USER_LOCATION = "USER_LOCATION"
        val INFO_USER_LOCATION_HAD_SEND = "LOCATION_HAD_SEND"

        val FIRST_USE = "FIRST_USE"
        val INFO_USER_IS_FIRST_USE = "INFO_USER_IS_FIRST_USE"
        val INFO_USER_IS_FIRST_USE_MAP = "INFO_USER_IS_FIRST_USE_MAP"


        //  Base API
        /*val BASE_URL = "http://178.128.96.70:81/v2/" // DEV
        val SOCKET_CONNECT_URL = "http://178.128.96.70:3000" // DEV
        val BASE_IMAGE = "http://178.128.96.70:84/" // DEV*/

        val BASE_URL = "https://api.kaioken.vn/v2/" // PRODUCTION
        val SOCKET_CONNECT_URL = "https://api.kaioken.vn:5000" // PRODUCTION
        val BASE_IMAGE = "https://upload.kaioken.vn/" // PRODUCTION
        var mRetrofit: Retrofit? = null

        var mRetrofitImage: Retrofit? = null
        var uid: String? = "-1"

        //check destination call open app is from Notificaton or not
        var callFromNotification = false

        var sid: String? = ""
        //login type=0: anonymous, type=1: login real
        var loginType: Int = 0
        var avatar = ""
        var phoneUser = ""
        var nameUser = ""
        var locationHadSent = true

        //  Socket
        var DIALOG_ERROR_SOCKET_IS_SHOWING = false
        var DIALOG_ERROR_NETWORK_IS_SHOWING = false
        var BASE_SOCKET: Socket? = null

        val delayBeforeOnSocket: Long = 500
        val delayCheckSocketWhenResume: Long = 10000
        val durationReconnectSocket: Long = 15000
        val durationRestartSocket: Long = 5000
        val delayCheckNetwork: Long = 10000
        val timeOutSocket = 10000L

        //        Status API
        val STATUS_RIGHT = 1
        val STATUS_WRONG = 0

        //Error code
        var errorMap: HashMap<Int, String>? = null
        val TAG_LOG = "****"

        //send data
        val DATA_SEND = "DATA_SEND"

        //detail store ordering
        var ID_STORE_ORDERING = -1
        var ID_TABLE_ORDERING = -1
        var IS_ADD_FOOD = false
        var ID_ORDERING = -1
        var NAME_STORE_ORDERING = ""
        var ADDRESS_STORE_ORDERING = ""
        var NAME_CUSTOMER = ""
        var backFromInfoStore = false
        // type use -> local, book or delivery
        var TYPE_USE = 3

        //key send data global
        val ORDER_ID_SEND = "ORDER_ID_SEND"

        //location
        var myLocation: LatLng? = null

        //  Change password
        var IS_CHANGE_PASSWORD = false

        //status service
        var SERVICE_IS_ON = false


        /////////////////////////////////////////////////////////////////////////////
        //  Date format
        val DATE_FORMAT_FULL = "HH:mm:ss - dd/MM/yyyy"
        val DATE_FORMAT_TEXT_FULL = "yyyy-MM-dd HH:mm:ss"


        //  Wallet types
        val WALLET_TYPE_COLLECT = "1"
        val WALLET_TYPE_SPEND = "2"
        val WALLET_TYPE_DEBT = "3"
        val WALLET_TYPE_LOAN = "4"

        //        INTENT
        val TAG_BUNDLE = "BUNDLE"

        //  check whether user is living in CashierTableFragment
        var IS_LIVING_CASHIER_TABLE = false

        var STORE_WORKING_ID: String? = "-1"

        //        Load more recycler view
        val OFFSET = 0
        val LIMIT = 10
        val VISIBLE_THRESHOLD = LIMIT - 3

        //        Money management
        val GET_MONEY = R.id.rbGetMoney
        val SPEND_MONEY = R.id.rbSpendMoney
        val LOAN_MONEY = R.id.rbLoanMoney
        val DEBT_MONEY = R.id.rbDebtMoney

        //  WALLET LIMIT STORE TYPES
        val WALLET_LIMIT_PERSONAL_TYPE = 1
        val WALLET_LIMIT_STORE_TYPE = 2

        //  Discount type
        val DISCOUNT_TYPE_PERCENT = 2
        val DISCOUNT_TYPE_VALUE = 1

        //status app
        var appRunOnBackground = false

        val splitCharacters = "!@#$%^&*"
    }
}
