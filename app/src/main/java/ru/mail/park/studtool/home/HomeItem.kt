package ru.mail.park.studtool.home


class HomeItem {
    lateinit var desc: String
    var id: Int = 0

    constructor() {

    }

    constructor(desc: String, id: Int) {
        this.desc = desc
        this.id = id
    }
}