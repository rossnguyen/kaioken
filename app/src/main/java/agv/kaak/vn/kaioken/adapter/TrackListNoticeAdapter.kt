package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken._interface.OnItemCheckedChangeListener
import agv.kaak.vn.kaioken._interface.OnLongClickListener
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.NotificationResult
import agv.kaak.vn.kaioken.fragment.home.NotificationFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.DateUtils
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_notification_notice_customer.view.*
import java.util.*

class TrackListNoticeAdapter(val context: Context, var listNoti: ArrayList<NotificationResult>) : RecyclerView.Adapter<TrackListNoticeAdapter.ViewHolder>() {

    private var allowChooseItem = false

    companion object {
        const val STATUS_NOTI_JUST_CREATE = 0
        const val STATUS_NOTI_RECEIVED = 1
        const val STATUS_NOTI_SEEN = 2
        const val STATUS_NOTI_COMPLETED = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification_notice_customer, parent, false))
    }

    override fun getItemCount(): Int {
        return listNoti.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = listNoti[position]
        if (notification.info!!.pageInfo != null) {
            holder!!.tvContent.text = Html.fromHtml("<b>${notification.info!!.pageInfo!!.pageName}</b> ${notification.content}")
            //holder!!.tvContent.text=context.resources.getString(R.string.notification_format_content,notification.info!!.pageInfo!!.pageName, notification.content)
            ImageHelper.loadImage(context, holder.imgAvatar, notification.info?.pageInfo?.linkImage, PlaceHolderType.RESTAURANT)
        } else {
            holder!!.tvContent.text = context.resources.getString(R.string.notification_format_content, notification.content, "")
        }

        if (notification.statusId == STATUS_NOTI_JUST_CREATE || notification.statusId == STATUS_NOTI_RECEIVED)
            holder.layoutRoot.background = context.resources.getDrawable(R.drawable.custom_background_0_radius_masterial1_border_gray_top_bottom)
        else
            holder.layoutRoot.background = context.resources.getDrawable(R.drawable.custom_background_0_radius_masterial4_border_gray_top_bottom)


        holder.tvTimeHandler.text = DateUtils.getDistanceTimeTMP7(context, ConvertHelper.stringGlobalToDateTime(notification.dateCreated!!))

        holder.layoutRoot.setOnClickListener {
            if (allowChooseItem)
                holder.ckbChoosed.performClick()
            else {
                if (onItemNoticeClickListener != null) {
                    notification.imageBitmap = (holder.imgAvatar.drawable as BitmapDrawable).bitmap
                    onItemNoticeClickListener!!.onSeen(notification.id)
                    onItemNoticeClickListener!!.onNotifiClick(holder, notification)
                }
            }
        }

        holder.layoutRoot.setOnLongClickListener {
            if (onItemLongClickListener != null) {
                onItemLongClickListener!!.onLongClick(position,NotificationFragment.FOR_NOTICE)
                holder.ckbChoosed.isChecked = true
                Log.d("${Constraint.TAG_LOG}LongClick", "co do")
            }

            return@setOnLongClickListener true
        }

        holder.ckbChoosed.setOnCheckedChangeListener { buttonView, isChecked ->
            notification.isChecked = isChecked
            if (onItemCheckedChangeListener != null)
                onItemCheckedChangeListener!!.onChange(isChecked)
        }

        if (allowChooseItem)
            holder.ckbChoosed.visibility = View.VISIBLE
        else
            holder.ckbChoosed.visibility = View.GONE

        holder.ckbChoosed.isChecked = notification.isChecked
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutRoot = view.layoutRoot
        val imgAvatar = view.imgAvatar
        val tvContent = view.tvContent
        val tvTimeHandler = view.tvTimeHandler
        val ckbChoosed = view.ckbChoosed
    }

    fun allowChooseItem(allow: Boolean) {
        this.allowChooseItem = allow
    }

    var onItemNoticeClickListener: OnItemNoticeClickListener? = null
    var onItemLongClickListener: OnLongClickListener? = null
    var onItemCheckedChangeListener: OnItemCheckedChangeListener? = null

    interface OnItemNoticeClickListener {
        fun onSeen(notifyId: Int)
        fun onNotifiClick(holder: ViewHolder, notifi: NotificationResult)
    }
}