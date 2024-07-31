package agv.kaak.vn.kaioken.utils

import agv.kaak.vn.kaioken.entity.AddFoodToOrder
import agv.kaak.vn.kaioken.entity.CreateBook
import agv.kaak.vn.kaioken.entity.CreateDelivery
import agv.kaak.vn.kaioken.entity.CreateOrder
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.util.Log
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class BaseSocketEmit {
    val gson = Gson()

    /**
     * Customer
     */

    fun getOrderByTableId(tableId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("table_id", tableId)
        Constraint.BASE_SOCKET?.emit("getOrderByTableId", jsonObject)
    }

    fun listCountNotyGroupByStatus(uid: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("uid", uid)
        Constraint.BASE_SOCKET?.emit("listCountNotyGroupByStatus", jsonObject)
    }

    fun listTab(pageId: Int?, departmentId:Int?){
        val jsonObject = JSONObject()
        jsonObject.put("page_id", pageId)
        jsonObject.put("department_id", departmentId)
        Constraint.BASE_SOCKET?.emit("listTab", jsonObject)
    }

    fun confirmSeenNoty(notifyId:Int){
        val jsonObject = JSONObject()
        jsonObject.put("notify_id", notifyId)
        Constraint.BASE_SOCKET?.emit("confirmSeenNoty",jsonObject)
    }

    fun listCountNotyByDepartment(pageId: Int?, departmentId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", pageId)
        jsonObject.put("department_id", departmentId)
        Constraint.BASE_SOCKET?.emit("listCountNotyByDepartment", jsonObject)
    }

    fun callWaiters(pageId: Int?, message: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", pageId)
        jsonObject.put("message", message)
        Constraint.BASE_SOCKET?.emit("callWaiters", jsonObject)
    }

    fun callWaiters(pageId: Int?, tableId: Int?, message: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", pageId)
        jsonObject.put("table_id", tableId)
        jsonObject.put("message", message)
        Constraint.BASE_SOCKET?.emit("callWaiters", jsonObject)
    }

    fun requestBilling(orderId: Int?, customerMoney: Double?, paymentType:Int, couponCode:String) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        jsonObject.put("customer_money", customerMoney)
        jsonObject.put("payment_type",paymentType)
        jsonObject.put("coupon_code", couponCode)
        Constraint.BASE_SOCKET?.emit("customerRequestBilling", jsonObject)
        Log.d("****RequestBilling",jsonObject.toString())
    }

    fun createOrder(createOrder: CreateOrder?) {
        Log.d("****CreateOrder","just emit at ${Calendar.getInstance().time.toString()}")
        Constraint.BASE_SOCKET?.emit("createOrderS1", createOrder?.toJsonObject())
    }

    fun extraFoodItemList(addFoodToOrder: AddFoodToOrder){
        Constraint.BASE_SOCKET?.emit("extraFoodItemList",addFoodToOrder.toJsonObject())
    }

    fun createDelivery(createDelivery: CreateDelivery?) {
        Log.d("****CreateDelivery","just emit at ${Calendar.getInstance().time.toString()}")
        Constraint.BASE_SOCKET?.emit("createOrderS1", createDelivery?.toJsonObject())
    }

    fun createBook(createBook: CreateBook?) {
        Log.d("****CreateBook","${createBook.toString()}")
        Constraint.BASE_SOCKET?.emit("createOrderS1", createBook?.toJsonObject())
    }

    fun getListNotifyFromDepartment(pageId: String?, departmentId: String?, creatorDepartmentId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", pageId)
        jsonObject.put("department_id", departmentId)
        jsonObject.put("creator_department_id", creatorDepartmentId)
        Constraint.BASE_SOCKET?.emit("getListNotifyFromDepartment", jsonObject)
    }

    fun getListNotifyFromStatus(statusId: Int?, pageId: Int?, departmentId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("status_id", statusId)
        jsonObject.put("page_id", pageId)
        jsonObject.put("department_id", departmentId)
        Constraint.BASE_SOCKET?.emit("getListNotifyFromStatus", jsonObject)
    }

    fun deleteListNotyByUser(notifyId: ArrayList<Int>?, userId: Int) {
        var jsonObject = JSONObject()
        var jsonArray= JSONArray(notifyId)
        jsonObject.put("list_notify_id", jsonArray)
        jsonObject.put("user_id",userId)
        Constraint.BASE_SOCKET?.emit("deleteListNotyByUser", jsonObject)
    }

    fun completeNoty(notifyId: Int?, departmentId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("notify_id", notifyId)
        jsonObject.put("department_id", departmentId)
        Constraint.BASE_SOCKET?.emit("completeNoty", jsonObject)
    }


    fun callOtherStaff(pageId:Int, message:String?){
        val jsonObject = JSONObject()
        jsonObject.put("page_id",pageId)
        jsonObject.put("message", message)
        Constraint.BASE_SOCKET?.emit("callOtherStaff", jsonObject)
    }

    fun getOrderAndOrderItem(orderId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        Constraint.BASE_SOCKET?.emit("getOrderAndOrderItem", jsonObject)
    }


    /*fun getOrderAndRelateInfo(orderId:Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        Constraint.BASE_SOCKET?.emit("getOrderAndRelateInfoS2", jsonObject)
    }*/

    /*fun extraFoodItemList(addFoodToOrderForStaff: AddFoodToOrderForStaff?) {
        Constraint.BASE_SOCKET?.emit("extraFoodItemList", addFoodToOrderForStaff?.toJsonObject())
    }*/


    /**
     * Cashier
     */
    fun joinPage(storeId: String?, departmentId: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", storeId)
        jsonObject.put("department_id", departmentId)
        Constraint.BASE_SOCKET?.emit("joinPage", jsonObject)
    }

    fun leavePage(storeId: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", storeId)
        Constraint.BASE_SOCKET?.emit("leavePage", jsonObject)
    }

    fun getDefaultInterface(storeId: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", storeId)
        Constraint.BASE_SOCKET?.emit("getDefaultInterface", jsonObject)
    }

    fun getTableServingByArea(areaId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("area_id", areaId)
        Constraint.BASE_SOCKET?.emit("getTableServingByArea", jsonObject)
    }

    fun confirmOrder(orderId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        Constraint.BASE_SOCKET?.emit("confirmOrder", jsonObject)
    }

    fun getOrderItem(orderId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        if (Constraint.BASE_SOCKET?.connected()!!)
            Constraint.BASE_SOCKET?.emit("getOrderItem", jsonObject)
    }

    fun addOrderExtra(orderId: Int?, extraName: String?, extraValue: Double?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        jsonObject.put("extra_name", extraName)
        jsonObject.put("extra_value", extraValue)
        Constraint.BASE_SOCKET?.emit("addOrderExtra", jsonObject)
    }

    fun addOrderDiscount(orderId: Int?, discountName: String?, discountValue: Int?, discountType: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        jsonObject.put("discount_name", discountName)
        jsonObject.put("discount_value", discountValue)
        jsonObject.put("discount_type", discountType)
        Constraint.BASE_SOCKET?.emit("addOrderDiscount", jsonObject)
    }

    fun addOrderVAT(orderId: Int?, vat: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        jsonObject.put("vat", vat)
        Constraint.BASE_SOCKET?.emit("addOrderVAT", jsonObject)
    }

    fun getTableReadyAndEmpty(storeId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", storeId)
        Constraint.BASE_SOCKET?.emit("getTableReadyAndEmpty", jsonObject)
    }

    /*fun changeTable(newTableRequest: NewTableRequest?) {
        val jsonObject = JSONObject(gson.toJson(newTableRequest))
        Constraint.BASE_SOCKET?.emit("changeTable", jsonObject)
    }*/

    fun getOrderAndRelateInfo(orderId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        Constraint.BASE_SOCKET?.emit("getOrderAndRelateInfoS2", jsonObject)
    }

    fun getPageRevenueInfo(year: Int?, month: Int?, day: Int?, storeId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("year", year)
        jsonObject.put("month", month)
        jsonObject.put("day", day)
        jsonObject.put("page_id", storeId)
        Constraint.BASE_SOCKET?.emit("getPageRevenueInfo", jsonObject)
    }

    fun addPageRevenue(storeId: Int?, type: Int?, amount: Int?, title: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", storeId)
        jsonObject.put("wallet_type", type)
        jsonObject.put("amount", amount)
        jsonObject.put("title", title)
        Constraint.BASE_SOCKET?.emit("addPageRevenue", jsonObject)
    }

    fun deletePageRevenue(pageRevenueId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_revenue_id", pageRevenueId)
        Constraint.BASE_SOCKET?.emit("deletePageRevenue", jsonObject)
    }

    fun cancelOrder(orderId: Int?, reason: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        jsonObject.put("reason", reason)
        Constraint.BASE_SOCKET?.emit("cancelOrder", jsonObject)
    }

    /**
     * Chef
     */
    fun getListTableOrderForChef(storeId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", storeId)
        Constraint.BASE_SOCKET?.emit("getListTableOrderForChef", jsonObject)
    }

    fun finishCooking(orderDetailId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_detail_id", orderDetailId)
        Constraint.BASE_SOCKET?.emit("finishCooking", jsonObject)
    }

    /*fun acceptCooking(acceptCookingRequest: AcceptCookingRequest?) {
        val jsonObject = JSONObject(gson.toJson(acceptCookingRequest))
        Constraint.BASE_SOCKET?.emit("acceptCooking", jsonObject)
    }*/

    fun getItemSumary(storeId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", storeId)
        Constraint.BASE_SOCKET?.emit("getItemSumary", jsonObject)
    }

    fun cancelFoodItem(orderId: Int?, orderDetailId: Int?, reason: String?, amount: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        jsonObject.put("order_detail_id", orderDetailId)
        jsonObject.put("reason", reason)
        jsonObject.put("amount", amount)
        Constraint.BASE_SOCKET?.emit("cancelFoodItem", jsonObject)
    }

    fun getOutOfFoodList(storeId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("page_id", storeId)
        Constraint.BASE_SOCKET?.emit("listMonhet", jsonObject)
    }

    fun setOutOfFood(menuItemId: Int?, isOutOfFood: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("menu_item_id", menuItemId)
        jsonObject.put("is_monhet", isOutOfFood)
        Constraint.BASE_SOCKET?.emit("setMonhet", jsonObject)
    }

    fun getOrderInfo(orderId: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        Constraint.BASE_SOCKET?.emit("getOrderInfo", jsonObject)
    }

    fun completeOrder(orderId: Int?, paidMoney: Int?) {
        val jsonObject = JSONObject()
        jsonObject.put("order_id", orderId)
        jsonObject.put("customer_money", paidMoney)
        Constraint.BASE_SOCKET?.emit("completeOrder", jsonObject)
    }

    fun getBillByInvoiceNo(invoice: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("invoice_no", invoice)
        Constraint.BASE_SOCKET?.emit("getBillByInvoiceNo", jsonObject)
    }
}