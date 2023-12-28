package com.example.todo.intro.intro.EditTask

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.example.todo.R
import com.example.todo.databinding.ActivityTaskScreenBinding
import com.example.todo.intro.intro.database.taskData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskScreenActivity : AppCompatActivity() {
    lateinit var dataBinding: ActivityTaskScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityTaskScreenBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        initView()
    }

    fun initView(){
        val taskId = intent.getStringExtra("taskId")
        val receivedItem = intent.getSerializableExtra("edit_task")as? taskData
        val receivedItem_date = intent.getSerializableExtra("edit_task_date")as? Date
        val intent_position = intent.getIntExtra("position",-1)
        if (receivedItem != null) {
            dataBinding.tvEditTitle.setText(receivedItem.title)
            dataBinding.tvEditDescription.setText(receivedItem.description)
            dataBinding.tvEditChangeTime.setText(receivedItem.time)

            dataBinding.ivEditEdit.setOnClickListener {
                onButtonShowPopupWindowClick(it, receivedItem.title?:"no",receivedItem.description?:"np")
            }

            dataBinding.tvEditChangeTime.setOnClickListener {
                showDatePickerPopup(receivedItem.time!!)
            }

            dataBinding.ivClose.setOnClickListener {
                finish()
            }


            dataBinding.btnEditTask.setOnClickListener {
                val editedTitle = dataBinding.tvEditTitle.text.toString()
                val editedDescription = dataBinding.tvEditDescription.text.toString()
                val editedDateText = dataBinding.tvEditTime.text.toString()

                val resultIntent = Intent()
                resultIntent.putExtra("edited_title", editedTitle)
                resultIntent.putExtra("edited_description", editedDescription)
                resultIntent.putExtra("edited_position", intent_position)
                resultIntent.putExtra("edited_date", editedDateText)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

        }
    }






    private fun showDatePickerPopup(defaultDate: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.edit_time_pop, null)

        val taskTimeText = dataBinding.tvEditChangeTime.text.toString()
        val dateInTextView = getDateFromString(taskTimeText)

        dataBinding.tvEditChangeTime.setOnClickListener {
            showDatePicker(dataBinding.tvEditChangeTime, dateInTextView)

        }

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.showAtLocation(dataBinding.tvEditChangeTime, Gravity.CENTER, 0, 0)
    }


    private fun showDatePicker(editTextDate: TextView, defaultDate: Calendar) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)


                // Format the selected date
                val dateFormat = SimpleDateFormat("dd / MM / yyyy HH:mm", Locale.getDefault())
                var formattedDate = dateFormat.format(selectedDate.time)

                // Save current time
                var currentTime = getCurrentTime()

                // Update tvEditChangeTime with selected date and current time
                dataBinding.tvEditChangeTime.text = "$formattedDate At $currentTime"
            },
            defaultDate.get(Calendar.YEAR),
            defaultDate.get(Calendar.MONTH),
            defaultDate.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }
    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        return "$hour:$minute"
    }

    private fun getDateFromString(dateString: String): Calendar {
        // Specify the format pattern based on the actual format of your date string
        val dateFormat = SimpleDateFormat("dd / MM / yyyy HH:mm", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        date?.let { calendar.time = it }
        return calendar
    }
    fun onButtonShowPopupWindowClick(view: View, title: String, description: String) {
        // Inflate the layout of the popup window
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.edit_title_description_pop, null)

        // Find the EditText views within the inflated layout
        val popTitle = popupView.findViewById<EditText>(R.id.et_pop_title)
        val popDescription = popupView.findViewById<EditText>(R.id.et_pop_description)
        val btnPopEdit = popupView.findViewById<Button>(R.id.btn_edit)
        val btnPopCancel = popupView.findViewById<Button>(R.id.btn_pop_cancel)

        // Set the text of the EditText views with the provided values
        popTitle.setText(title)
        popDescription.setText(description)


        // Create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // Lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        btnPopCancel.setOnClickListener {
            popupWindow.dismiss()
        }
        btnPopEdit.setOnClickListener {

            dataBinding.tvEditTitle.text = popTitle.text
            dataBinding.tvEditDescription.text = popDescription.text

            popupWindow.dismiss()
        }
        // Show the popup window
        // The view you pass in doesn't matter; it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Dismiss the popup window when touched
        popupView.setOnTouchListener { _, event ->
            popupWindow.dismiss()
            true
        }
    }


}