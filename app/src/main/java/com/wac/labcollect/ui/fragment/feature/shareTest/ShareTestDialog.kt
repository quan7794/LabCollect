package com.wac.labcollect.ui.fragment.feature.shareTest

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import coil.load
import com.wac.labcollect.MainApplication
import com.wac.labcollect.databinding.ShareTestDialogBinding

class ShareTestDialog : DialogFragment() {
    var _binding: ShareTestDialogBinding? = null
    val binding : ShareTestDialogBinding
        get() = _binding!!

    private val viewModel: ShareTestDialogViewModel by viewModels {
        ShareTestDialogViewModelFactory((requireActivity().application as MainApplication).googleApiRepository)
    }

    private var spreadId = ""

    companion object {

        const val SPREAD_ID_KEY = "SPREAD_ID_KEY"

        fun newInstance(spreadId: String): ShareTestDialog {
            val fragment = ShareTestDialog()
            val bundle = Bundle()
            bundle.putString(SPREAD_ID_KEY, spreadId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ShareTestDialogBinding.inflate(inflater)
        spreadId = arguments?.getString(SPREAD_ID_KEY, "") ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            testUrl.isSelected = true
        }

        viewModel.init(spreadId)
        viewModel.qrImage.observe(viewLifecycleOwner) {
            binding.qrCodeImage.load(it)
        }
        dialog?.window?.apply {
            setLayout((resources.displayMetrics.widthPixels), WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}