package uz.exemple.notesapp_sqlite_kotlin.database

class TableSchema {

    object note {
        const val TABLE_NAME = "tbl_note"
        const val ID = "id"
        // public static final String TITLE="title";
        const val DESCRIPTION = "description"
        const val DATE = "date"
    }
}