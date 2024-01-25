package com.example.todo.intro.intro.fragment.bottomSheet

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todo.databinding.FragmentBottomSheetBinding
import com.example.todo.intro.intro.EditTask.taskScreenViewmodel
import com.example.todo.intro.intro.database.addTaskToFireStore
import com.example.todo.intro.intro.database.taskData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class bottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var calendar: Calendar
    lateinit var dataBinding: FragmentBottomSheetBinding
    private lateinit var auth: FirebaseAuth
    lateinit var time: String

    lateinit var viewModel: bottomSheetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentBottomSheetBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this)[bottomSheetViewModel::class.java]

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {
        auth = Firebase.auth
        calendar = Calendar.getInstance()
        time = getCurrentDateTime()
        dataBinding.ivClock.setOnClickListener {
            showDatePicker()
        }
        dataBinding.ivSend.setOnClickListener {
            val userID = auth.currentUser?.uid ?: ""
            val title = dataBinding.etTaskTitle.text.toString()
            val description = dataBinding.etTaskDescription.text.toString()

            viewModel.addTaskToFireStoreDB(userID, title, description, time,
                onSuccess = {
                    dataBinding.etTaskTitle.text.clear()
                    dataBinding.etTaskDescription.text.clear()
                    dismiss()
                },
                onFailure = { errorMessage ->
                    Log.w("TAG", errorMessage)
                }
            )

        }
    }

        private fun showDatePicker() {
        val datePicker = DatePickerDialog(requireContext())
        datePicker.show()
        datePicker.setOnDateSetListener { datePicker, year, month, day ->
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)

            // Format the selected date and time
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            time = "At ${dateFormat.format(calendar.time)}"
        }
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return "$hour:$minute"
    }

    fun getCurrentDateTime(): String {

        val calendar = Calendar.getInstance()

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val time = getCurrentTime()

        return "$day / $month / $year  $time"
    }
}
