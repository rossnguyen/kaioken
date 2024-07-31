package agv.kaak.vn.kaioken.entity

import java.io.Serializable

class SerializableLatLng : Serializable {
    // mark it transient so defaultReadObject()/defaultWriteObject() ignore it
    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()

    constructor() {}

    constructor(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }
}