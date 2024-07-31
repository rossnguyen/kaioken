package agv.kaak.vn.kaioken.entity.define

class UpdateRealtime2Define {
    companion object {
        val JUST_CREATE_NEW_ORDER = 1
        val CONFIRM_ORDER = 2
        val CANCEL_ORDER = 3
        val REQUIRE_PAYMENT = 4
        val CONFIRM_PAYMENT_FROM_CASHIER = 5
        val CHEF_START = 6
        val CHEF_FINISH = 7
        val ADD_OR_REMOVE_FOOD = 8
        val CHANGE_TABLE = 9
        val ADD_SURCHARGE_OR_DISCOUNT_OR_VAT = 10
        val CHANGE_NOTIFI = 11
        val CHANGE_SOLD_OUT = 12
        val SHIPPING = 13
    }
}

