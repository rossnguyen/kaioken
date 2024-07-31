package agv.kaak.vn.kaioken.utils.recyclerview

/**
 * Created by shakutara on 11/22/17.
 */
interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)

}