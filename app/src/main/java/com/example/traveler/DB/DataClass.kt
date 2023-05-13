

class DataClass {
    var dataName: String? = null
        private set
    var dataDesc: String? = null
        private set
    var dataImage: String? = null
        private set
    var key: String? = null


    constructor(dataName: String?, dataDesc: String?, dataImage: String?) {
        this.dataName = dataName
        this.dataDesc = dataDesc
        this.dataImage = dataImage
    }

    constructor() {}
}