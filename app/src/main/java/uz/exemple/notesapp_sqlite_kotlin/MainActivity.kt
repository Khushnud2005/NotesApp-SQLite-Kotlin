package uz.exemple.notesapp_sqlite_kotlin

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.exemple.notesapp_sqlite_kotlin.adapter.NotesAdapter
import uz.exemple.notesapp_sqlite_kotlin.database.MySqliteHelper
import uz.exemple.notesapp_sqlite_kotlin.model.Notes
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var btn_add: FrameLayout
    lateinit var recyclerView: RecyclerView
    lateinit var context: Context

    lateinit var helper: MySqliteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }
    fun initViews(){
        context = this
        btn_add = findViewById(R.id.btn_add)
        recyclerView = findViewById(R.id.recyclerview)
        helper = MySqliteHelper(this)

        val manager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = manager
        btn_add.setOnClickListener { openAlert() }
        val adapter = NotesAdapter(this, getNotes())
        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(this, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {

                    Log.d("@@@", "Posittion - $position")
                    val item = getNotes()[position]
                    updateAlert(item.id, item.note)
                }

                override fun onLongItemClick(view: View, position: Int) {
                    // do whatever
                }
            })
        )
    }

    fun openAlert() {
        val editText = EditText(this)
        editText.hint = "Enter Your Note"
        editText.setHintTextColor(Color.parseColor("#C6C6C6"))
        editText.setPadding(32, 0, 16, 32)
        editText.height = 100
        editText.isCursorVisible = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.setTextCursorDrawable(R.drawable.ic_cursor_24)
        }
        val titleView = TextView(context)
        titleView.text = "New Note"
        titleView.gravity = Gravity.LEFT
        titleView.setPadding(20, 20, 20, 5)
        titleView.textSize = 20f
        titleView.setTypeface(Typeface.DEFAULT_BOLD)
        titleView.setTextColor(Color.parseColor("#00C6AE"))
        val dialog = AlertDialog.Builder(this)
            .setCustomTitle(titleView)
            .setView(editText)
            .setPositiveButton("Save") { dialogInterface, i ->
                val note = editText.text.toString().trim { it <= ' ' }
                if (!note.isEmpty()) {
                    saveNote(note)
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#00C6AE"))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#00C6AE"))
        }
        dialog.show()
    }
    fun saveNote(note: String) {
        val dateT: String = getTime()
        val new_note = Notes(note, dateT)
        helper.saveNote(new_note)
    }

    fun updateAlert(id: Int, note: String) {
        val editText = EditText(this)
        editText.setText(note)
        editText.isCursorVisible = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.setTextCursorDrawable(R.drawable.ic_cursor_24)
        }
        val titleView = TextView(context)
        titleView.text = "Update Note"
        titleView.gravity = Gravity.LEFT
        titleView.setPadding(20, 20, 20, 5)
        titleView.textSize = 20f
        titleView.setTypeface(Typeface.DEFAULT_BOLD)
        titleView.setTextColor(Color.parseColor("#00C6AE"))

        val dialog = AlertDialog.Builder(this)
            .setCustomTitle(titleView)
            .setView(editText)
            .setPositiveButton("Update") { dialogInterface, i ->
                val new_note = editText.text.toString()
                updateNote(id, new_note)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            .setNegativeButton("Delete") { dialog, which ->

                helper.deleteNote(id.toString())
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            .setNeutralButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#00C6AE"))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#00C6AE"))
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                .setTextColor(Color.parseColor("#00C6AE"))
        }
        dialog.show()
    }

    fun updateNote(id: Int, note: String) {
        val dateT = getTime()
        val new_note = Notes(id, note, dateT)
        helper.updateNote(new_note)
    }



    fun getNotes(): ArrayList<Notes> {
        val all_notes = helper.getAllNotes() as ArrayList<Notes>
        val notes = ArrayList<Notes>()
        for (n in all_notes) {
            notes.add(n)
        }
        return notes
    }
    fun getTime(): String {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("LLL dd")
        return simpleDateFormat.format(calendar.time).toString()
    }
}