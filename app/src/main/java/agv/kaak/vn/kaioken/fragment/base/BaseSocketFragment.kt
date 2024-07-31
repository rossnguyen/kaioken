package agv.kaak.vn.kaioken.fragment.base


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import io.socket.client.Socket
import io.socket.emitter.Emitter

abstract class BaseSocketFragment : Fragment() {

    lateinit var mContext: Context
    lateinit var activityParent: Activity
    lateinit var mFragmentManager: FragmentManager
    var socketReady: Boolean = false

    lateinit var dialog: ProgressDialog
    var dialogCanNotConnectToServer: AlertDialog? = null

    private var countDisconnectSocket = 0
    private var textViewStatusSocket: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return TextView(activity).apply {
            setText(R.string.hello_blank_fragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewStatusSocket = setupComponentStatusSocket()
        addEvent()
        getData()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        activityParent = context as Activity
        mFragmentManager = childFragmentManager
    }

    override fun onResume() {
        super.onResume()
        //listen
        Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT, onSocketConnected)
        Constraint.BASE_SOCKET?.on(Socket.EVENT_DISCONNECT, onSocketDisconnect)
        Constraint.BASE_SOCKET?.on(Socket.EVENT_RECONNECT, onSocketConnecting)

        //init dialog connecting to server if socket not connected
        dialog = ProgressDialog(activityParent, R.style.progressDialogPrimary)
        dialog.setMessage(activity!!.resources.getString(R.string.all_connecting_to_server))
        dialog.setCancelable(false)

        if (Constraint.DIALOG_ERROR_SOCKET_IS_SHOWING) {
            dialogCanNotConnectToServer?.dismiss()
            Constraint.DIALOG_ERROR_SOCKET_IS_SHOWING = false
        }

        if (Constraint.DIALOG_ERROR_NETWORK_IS_SHOWING) {
            GlobalHelper.dialogCanNotConnectToNetWork?.dismiss()
            Constraint.DIALOG_ERROR_NETWORK_IS_SHOWING = false
        }

        //if socket connected: get data
        //not connected: on event CONNECT and get data after connect success (show dialog during connecting)

        if (Constraint.BASE_SOCKET != null) {
            if (Constraint.BASE_SOCKET!!.connected()) {
                offSocket()
                listenSocket()
                socketReady = true
                loadData()
                textViewStatusSocket?.visibility = View.GONE
            } else {
                Handler().postDelayed({
                    if (!Constraint.BASE_SOCKET!!.connected() && !Constraint.DIALOG_ERROR_SOCKET_IS_SHOWING) {
                        //showDialogCanNotConnectServer()
                        textViewStatusSocket?.run {
                            text = activityParent.resources.getString(R.string.all_not_connected)
                            setTextColor(ContextCompat.getColor(activityParent, R.color.colorWhiteApp))
                            setBackgroundColor(ContextCompat.getColor(activityParent, R.color.colorRedPrimary))
                            visibility = View.VISIBLE
                        }

                        countDisconnectSocket++
                        //Constraint.DIALOG_ERROR_SOCKET_IS_SHOWING = true
                    }
                }, Constraint.durationRestartSocket)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        offSocket()
        socketReady = false
        Constraint.BASE_SOCKET?.off(Socket.EVENT_CONNECT)
        Constraint.BASE_SOCKET?.off(Socket.EVENT_DISCONNECT)
        Constraint.BASE_SOCKET?.off(Socket.EVENT_RECONNECT)

        //Log.d("****Socket", "Co do on pause")
        //Constraint.BASE_SOCKET?.off(Socket.EVENT_RECONNECTING)
    }

    private val onSocketConnected = Emitter.Listener {
        activityParent.runOnUiThread {
            if (countDisconnectSocket > 0) {
                textViewStatusSocket?.run {
                    Handler().postDelayed({
                        text = activityParent.resources.getString(R.string.all_connect_success)
                        setBackgroundColor(ContextCompat.getColor(activityParent, R.color.colorBlueLight))
                        setTextColor(ContextCompat.getColor(activityParent, R.color.colorWhiteApp))
                        Handler().postDelayed({
                            visibility = View.GONE
                        }, 1000)
                    }, 1000)
                }
            }

            Log.d("${Constraint.TAG_LOG}StatusSocket", "Connected")


            if (dialogCanNotConnectToServer != null && dialogCanNotConnectToServer!!.isShowing)
                dialogCanNotConnectToServer!!.dismiss()

            Handler().postDelayed({
                offSocket()
                listenSocket()
                socketReady = true
                loadData()
            }, Constraint.delayBeforeOnSocket)
        }
    }

    private val onSocketDisconnect = Emitter.Listener {
        socketReady = false
        activityParent.runOnUiThread {
            Handler().postDelayed({
                if (!Constraint.BASE_SOCKET!!.connected() && !Constraint.DIALOG_ERROR_SOCKET_IS_SHOWING) {
                    //showDialogCanNotConnectServer()
                    textViewStatusSocket?.run {
                        text = activityParent.resources.getString(R.string.all_not_connected)
                        setTextColor(ContextCompat.getColor(activityParent, R.color.colorWhiteApp))
                        setBackgroundColor(ContextCompat.getColor(activityParent, R.color.colorRedPrimary))
                        visibility = View.VISIBLE
                    }
                    Log.d("${Constraint.TAG_LOG}StatusSocket", "Disconnected")

                    countDisconnectSocket++
                    //Constraint.DIALOG_ERROR_SOCKET_IS_SHOWING = true
                }
            }, Constraint.durationRestartSocket)
        }
    }

    private val onSocketConnecting = Emitter.Listener {
        activityParent.runOnUiThread {
            textViewStatusSocket?.run {
                text = activityParent.resources.getString(R.string.all_connecting)
                setTextColor(ContextCompat.getColor(activityParent, R.color.colorGreenAppPrimary))
                setBackgroundColor(ContextCompat.getColor(activityParent, R.color.colorOrangeLight))
                Log.d("****Connecting", "socket connecting")
                //visibility=View.VISIBLE
            }
        }
    }

    fun showDialogMessage(title: String?, message: String?) {
        AlertDialog.Builder(activityParent)
                .setMessage(message)
                .setNegativeButton(resources.getString(R.string.all_close)) { dialog, which ->
                    dialog.dismiss()
                }
                .setTitle(title)
                .setCancelable(true)
                .show()
    }

    abstract fun getData()
    abstract fun loadData()
    abstract fun addEvent()
    abstract fun listenSocket()
    abstract fun offSocket()
    abstract fun setupComponentStatusSocket(): TextView?
}
