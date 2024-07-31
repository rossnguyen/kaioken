package agv.kaak.vn.kaioken.entity.define

class OrderStatus {
    companion object {
        const val SEND_ORDER = 0
        const val CONFIRM = 1
        const val CHEF_START = 2
        const val SERVING = 3
        const val REQUIRE_PAYMENT = 4
        const val FINISH = 5
        const val CANCEL = 6
        const val CHEF_FINISH = 7
        const val SHIPPING=8
    }
}