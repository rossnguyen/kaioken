package agv.kaak.vn.kaioken.entity

class SpentMoney {
    var type: Long = 0
    var content: String? = null
    var money: Long = 0
    var date: String? = null

    constructor(type: Long, content: String?, money: Long) {
        this.type = type
        this.content = content
        this.money = money
    }

    constructor(type: Long, content: String?, money: Long, date: String?) {
        this.type = type
        this.content = content
        this.money = money
        this.date = date
    }


}