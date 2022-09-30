package uz.exemple.notesapp_sqlite_kotlin.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.exemple.notesapp_sqlite_kotlin.model.Notes

class MySqliteHelper(var context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,VERSION){
    companion object {
        private val DATABASE_NAME = "note.db"
        private val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val table1 =
            "create table " + TableSchema.note.TABLE_NAME.toString() + "(" + TableSchema.note.ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TableSchema.note.DESCRIPTION.toString() + " TEXT, " + TableSchema.note.DATE.toString() + " TEXT );"
        db.execSQL(table1)
    }

    fun saveNote(model: Notes): Boolean {
        val database = this.writableDatabase
        val cv = ContentValues()
        //cv.put(TableSchema.note.TITLE,model.getTitle());
        cv.put(TableSchema.note.DESCRIPTION, model.note)
        cv.put(TableSchema.note.DATE, model.date)
        val id = database.insert(TableSchema.note.TABLE_NAME, null, cv)
        return id != -1L
    }
    fun updateNote(model: Notes): Boolean {
        val database = this.writableDatabase
        val cv = ContentValues()

        cv.put(TableSchema.note.DESCRIPTION, model.note)
        cv.put(TableSchema.note.DATE, model.date)
        database.update(TableSchema.note.TABLE_NAME, cv, "id = ?", arrayOf(model.id.toString()))

        return model.id !== -1
    }
    @SuppressLint("Range")
    fun getAllNotes(): List<Notes> {
        val database = this.readableDatabase
        val cols = arrayOf(TableSchema.note.ID, TableSchema.note.DESCRIPTION, TableSchema.note.DATE)
        val cursor = database.query(
            TableSchema.note.TABLE_NAME,
            cols,
            null,
            null,
            null,
            null,
            TableSchema.note.ID + " DESC"
        )
        val list = ArrayList<Notes>()
        while (cursor.moveToNext()) {
            val model = Notes()
            model.id = cursor.getInt(cursor.getColumnIndex(TableSchema.note.ID))
            model.note = cursor.getString(cursor.getColumnIndex(TableSchema.note.DESCRIPTION))
            model.date = cursor.getString(cursor.getColumnIndex(TableSchema.note.DATE))
            list.add(model)
        }
        cursor.close()
        database.close()
        return list
    }

    fun deleteNote(id: String): Boolean {
        val database = this.writableDatabase
        return database.delete(
            TableSchema.note.TABLE_NAME,
            TableSchema.note.ID + "=" + id,
            null
        ) > 0
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TableSchema.note.TABLE_NAME)
        }
    }


}