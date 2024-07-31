package agv.kaak.vn.kaioken.fragment.finance

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.MoneyListAdapter
import agv.kaak.vn.kaioken.entity.BillDetailInfo
import agv.kaak.vn.kaioken.fragment.base.BaseFragment_
import agv.kaak.vn.kaioken.mvp.contract.FinanceDetailContract
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import agv.kaak.vn.kaioken.entity.response.wallet.ItemWallet
import agv.kaak.vn.kaioken.mvp.presenter.FinanceDetailPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.recyclerview.MoneyTouchHelperCallback
import agv.kaak.vn.kaioken.utils.recyclerview.RecyclerTouchListener
import kotlinx.android.synthetic.main.fragment_finance_guest.*

/**
 * Created by shakutara on 28/01/2018.
 */
class FinanceDetailFragment : BaseFragment_(), FinanceDetailContract.View, MoneyListAdapter.MoneyListAdapterCallback {
    override fun getBillDetailSuccess(billDetailInfo: BillDetailInfo?) {
        hideLoading()
    }

    override fun getBillDetailFailed(msg: String?) {
        hideLoading()
        showMessage(msg)
    }

    override fun deleteMoneyItem(itemWallet: ItemWallet) {
        callback.getMoneyDeleted(itemWallet)
    }

    override fun showLoading() {
        loader.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loader.visibility = View.GONE
    }

    private var moneyListAdapter: MoneyListAdapter? = null
    private val itemWallets = arrayListOf<ItemWallet>()
    private lateinit var callback: OnDeleteMoneyListener

    private val mPresenter = FinanceDetailPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finance_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  get item wallet list
        itemWallets.addAll(arguments?.getSerializable("ITEM_WALLET_LIST") as ArrayList<ItemWallet>)

        //  init adapter
        moneyListAdapter = MoneyListAdapter(context!!, itemWallets)
        val mLayoutManager = LinearLayoutManager(context)
        rvDetail?.layoutManager = mLayoutManager
        rvDetail?.itemAnimator = DefaultItemAnimator()
        rvDetail?.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        rvDetail?.adapter = moneyListAdapter

        //  items click
        rvDetail?.addOnItemTouchListener(RecyclerTouchListener(context!!, rvDetail, object : RecyclerTouchListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                if (itemWallets[position].invoiceNo != null) {
                    showLoading()
                    mPresenter.getBillDetail(itemWallets[position].invoiceNo)
                } else showMessage(context?.getString(R.string.all_cannot_process))
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }))

        //  set touch events
        if (!Constraint.IS_LIVING_CASHIER_TABLE) { // only customer is allowed deleting money item
            val callback = MoneyTouchHelperCallback(moneyListAdapter!!)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(rvDetail)
        }

        //  adapter get OnListenerResponse
        moneyListAdapter!!.getCallbackMoneyListAdapter(this)
    }

//    //////////////

    interface OnDeleteMoneyListener {
        fun getMoneyDeleted(itemWallet: ItemWallet?)
    }

    fun setOnDeleteMoneyListener(callback: OnDeleteMoneyListener) {
        this.callback = callback
    }

//    //////////////

}