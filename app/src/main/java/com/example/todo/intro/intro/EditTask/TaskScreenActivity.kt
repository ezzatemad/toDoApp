package com.example.todo.intro.intro.EditTask

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.todo.intro.intro.database.userData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskScreenActivity : AppCompatActivity() {
    lateinit var dataBinding: ActivityTaskScreenBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityTaskScreenBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        initView()
    }

    fun initView(){
        auth = Firebase.auth
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

            dataBinding.tvEditChangeTime?.setOnClickListener {
//                showDatePickerPopup(dataBinding.tvEditChangeTime.text.toString())
                showDatePicker(dataBinding.tvEditChangeTime, Calendar.getInstance())
            }

            dataBinding.ivClose.setOnClickListener {
                finish()
            }

            dataBinding.btnEditTask.setOnClickListener {
                val editedTitle = dataBinding.tvEditTitle.text.toString()
                val editedDescription = dataBinding.tvEditDescription.text.toString()
                val editedDateText = dataBinding.tvEditChangeTime.text.toString()

                // Assuming you have taskId available
                val taskId = intent.getStringExtra("edit_task_id")

                updateTask(auth.currentUser?.uid!!, taskId!!, editedTitle, editedDescription, editedDateText,false)

            }

        }
        dataBinding.tvEditDelete.setOnClickListener {
            onDeleteButtonShowPopupWindowClick(it,dataBinding.tvEditTitle.text.toString())
        }
    }


    private fun updateTask(userid: String, taskId: String, updatedTitle: String,
                           updatedDescription: String, updatedTime: String, updatedDone: Boolean) {
        val db = Firebase.firestore
        val userCollection = db.collection(userData.USER)
        val userDoc = userCollection.document(userid)
        val taskCollection = userDoc.collection(taskData.TASK)
        val taskDoc = taskCollection.document(taskId)

        val updatedData = hashMapOf(
            "title" to updatedTitle,
            "description" to updatedDescription,
            "time" to updatedTime,
            "done" to updatedDone
            // Add other fields if needed
        )

        taskDoc
            .update(updatedData as Map<String, Any>)
            .addOnSuccessListener {
                // Document successfully updated
                Log.d("TAG", "DocumentSnapshot successfully updated!")
                finish()
            }
            .addOnFailureListener { e ->
                // Handle errors here
                Log.w("TAG", "Error updating document", e)
            }
    }


    private fun deleteTask(userid: String, taskId: String) {
        val db = Firebase.firestore
        val userCollection = db.collection(userData.USER)
        val userDoc = userCollection.document(userid)
        val taskCollection = userDoc.collection(taskData.TASK)
        val taskDoc = taskCollection.document(taskId)

        taskDoc
            .delete()
            .addOnSuccessListener {
                // Document successfully deleted
                Log.e("TAG", "DocumentSnapshot successfully deleted!")
                finish()
            }
            .addOnFailureListener { e ->
                // Handle errors here
                Log.e("TAG", "Error deleting document", e)
            }
    }

    private fun showDatePickerPopup(defaultDate: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.edit_time_pop, null)

        val taskTimeText = dataBinding.tvEditChangeTime.text.toString()
        val dateInTextView = getDateFromString(taskTimeText)

        dataBinding.tvEditChangeTime.setOnClickListener {
            if (dateInTextView != null) {
                showDatePicker(dataBinding.tvEditChangeTime, dateInTextView)
            }

        }

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.showAtLocation(dataBinding.tvEditChangeTime, Gravity.CENTER, 0, 0)
    }

    private fun showDatePicker(editTextDate: TextView, defaultDate: Calendar) {
        Log.d("TAG", "showDatePicker function called")  // Add this log statement

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                Log.d("TAG", "DatePickerDialog callback")  // Add this log statement

                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                // Format the selected date
                val dateFormat = SimpleDateFormat("dd / MM / yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Save current time
                val currentTime = getCurrentTime()

                // Update tvEditChangeTime with selected date and current time
                val updatedDateTime = "$formattedDate At $currentTime"
                editTextDate.text = updatedDateTime
            },
            defaultDate.get(Calendar.YEAR),
            defaultDate.get(Calendar.MONTH),
            defaultDate.get(Calendar.DAY_OF_MONTH)
        )

        Log.d("TAG", "Showing DatePickerDialog")  // Add this log statement
        datePickerDialog.show()
    }


    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        return "$hour:$minute"
    }

    private fun getDateFromString(dateString: String): Calendar? {
        // Check for the special case "Time : "
        if (dateString.equals("Time : ", ignoreCase = true)) {
            // Handle the special case here, return null or create a default Calendar instance
            return null
        }

        try {
            // Specify the format pattern based on the actual format of your date string
            val dateFormat = SimpleDateFormat("dd / MM / yyyy HH:mm", Locale.getDefault())
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            date?.let { calendar.time = it }
            return calendar
        } catch (e: ParseException) {
            // Handle the parsing exception, e.g., log the error or return null
            Log.e("TAG", "Error parsing date", e)
            return null
        }
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
    fun onDeleteButtonShowPopupWindowClick(view: View, title: String) {
        // Inflate the layout of the popup window
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.delete_task_pop, null)

        // Find the EditText views within the inflated layout
        val popTitle = popupView.findViewById<TextView>(R.id.tv_delete_task_title)
        val btnPopDelete = popupView.findViewById<Button>(R.id.btn_pop_delete)
        val btnPopCancel = popupView.findViewById<Button>(R.id.btn_pop_cancel)

        // Set the text of the EditText views with the provided values
        popTitle.setText("Task Title: ${title}")


        // Create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // Lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        btnPopCancel.setOnClickListener {
            popupWindow.dismiss()
        }
        btnPopDelete.setOnClickListener {
            val userid = auth.currentUser?.uid
            val taskId = intent.getStringExtra("edit_task_id")


            deleteTask(userid!!, taskId!!)

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