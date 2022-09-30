package uz.exemple.notesapp_sqlite_kotlin.model


open class Notes {

    var id = 0
    lateinit var note: String
    lateinit var date: String

    constructor() {}
    constructor(note: String, date: String) {
        id = 0
        this.note = note
        this.date = date
    }

    constructor(id: Int, note: String, date: String) {
        this.id = id
        this.note = note
        this.date = date
    }
}