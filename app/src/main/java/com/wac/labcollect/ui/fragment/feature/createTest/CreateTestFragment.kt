package com.wac.labcollect.ui.fragment.feature.createTest

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.wac.labcollect.R
import com.wac.labcollect.databinding.CreateTestFragmentBinding
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.utils.Utils.createUniqueName
import com.wac.labcollect.utils.Utils.currentTimestamp
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class CreateTestFragment : BaseFragment<CreateTestFragmentBinding>() {
    private val myCalendar: Calendar = Calendar.getInstance()
    private var currentDatePicker: Int? = null
    private val viewModel: CreateTestViewModel by viewModels { CreateTestViewModelFactory(testRepository, googleApiRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextStep.setOnClickListener {
            val test = Test(
                startTime = binding.startDate.text.toString(),
                endTime = binding.endDate.text.toString(),
                isPublic = binding.publishTestSwitch.isChecked,
                owner = FirebaseAuth.getInstance().currentUser?.email.toString(),
                title = binding.testName.text.toString(),
                uniqueName = binding.testName.text.toString().createUniqueName(),
                type = binding.testType.text.toString(),
                createTimestamp = currentTimestamp().toString()
            )

            context?.let { context ->
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.choose_template)
                    .setMessage(R.string.create_test_message)
                    .setPositiveButton(R.string.create_new) { _, _ ->
                        Timber.e("Create new template")
                        navigate(CreateTestFragmentDirections.actionCreateTestFragmentToCreateTemplateFragment(test))
                    }
                    .setNegativeButton(R.string.select_existed_template) { _, _ ->
                        Timber.e("Use existed template")
                        navigate(CreateTestFragmentDirections.actionCreateTestFragmentToManageTemplateFragment(test))
                    }
                    .setCancelable(true)
                    .show()
            }
        }
        binding.startDate.setOnClickListener {
            currentDatePicker = it.id
            DatePickerDialog(requireContext(), dateSetListener, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH]).show()
        }
        binding.endDate.setOnClickListener {
            currentDatePicker = it.id
            DatePickerDialog(requireContext(), dateSetListener, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH]).show()
        }
    }

    private var dateSetListener = OnDateSetListener { _, year, month, day ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, day)
        currentDatePicker?.let { binding.root.findViewById<TextInputEditText>(it).updateStartDate2Ui() }
    }

    private fun TextInputEditText.updateStartDate2Ui() {
        val myFormat = "dd/MM/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.ROOT)
        this.setText(dateFormat.format(myCalendar.time))
    }
}