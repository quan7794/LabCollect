package com.wac.labcollect.ui.fragment.feature.shareTest

import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.wac.labcollect.MainApplication
import com.wac.labcollect.databinding.ShareTestDialogBinding
import com.wac.labcollect.utils.FileUtils.addImageToGallery
import com.wac.labcollect.utils.FileUtils.saveBitmapToInternalStorage
import com.wac.labcollect.utils.FileUtils.shareImage
import com.wac.labcollect.utils.FileUtils.shareUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShareTestDialog : DialogFragment() {
    var _binding: ShareTestDialogBinding? = null
    val binding: ShareTestDialogBinding
        get() = _binding!!

    private val viewModel: ShareTestDialogViewModel by viewModels {
        ShareTestDialogViewModelFactory((requireActivity().application as MainApplication).googleApiRepository)
    }

    private var spreadId = ""
    private var spreadName = ""
    private var spreadUniqueName = ""
    private var savedFileDir: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ShareTestDialogBinding.inflate(inflater)
        spreadId = arguments?.getString(SPREAD_ID_KEY, "") ?: ""
        spreadName = arguments?.getString(SPREAD_NAME_KEY, "") ?: ""
        spreadUniqueName = arguments?.getString(SPREAD_UNIQUE_NAME_KEY, "") ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewAction()
    }

    private fun initViewAction() {
        binding.saveQrCode.setOnClickListener {
            viewModel.qrImage.value?.let { bitmap ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val description = "Đây là mã QR của thí nghiệm: $spreadName"
                    requireContext().addImageToGallery(bitmap, spreadUniqueName, description)
                    withContext(Dispatchers.Main) {
                        Snackbar.make(requireView(), "Đã lưu QR Code.", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } ?: run {
                Snackbar.make(requireView(), "Không thể lưu QR Code", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.shareQrCode.setOnClickListener {
            viewModel.qrImage.value?.let { bitmap ->
                lifecycleScope.launch(Dispatchers.IO) { requireContext().shareImage(bitmap, spreadUniqueName) }
            } ?: run { Snackbar.make(requireView(), "Không thể chia sẻ QR Code", Snackbar.LENGTH_SHORT).show() }
        }
        binding.shareLink.setOnClickListener {
            viewModel.testUrl.value?.let {
                context?.shareUrl(it)
            } ?: run { Snackbar.make(requireView(), "Không thể chia sẻ liên kết thí nghiệm.", Snackbar.LENGTH_SHORT).show() }
        }
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
            savedFileDir = ContextWrapper(context).saveBitmapToInternalStorage(it, spreadUniqueName)
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

    companion object {

        const val SPREAD_ID_KEY = "SPREAD_ID_KEY"
        const val SPREAD_UNIQUE_NAME_KEY = "SPREAD_UNIQUE_NAME_KEY"
        const val SPREAD_NAME_KEY = "SPREAD_NAME_KEY"

        fun newInstance(spreadId: String, spreadName: String, uniqueName: String): ShareTestDialog {
            val fragment = ShareTestDialog()
            val bundle = Bundle()
            bundle.putString(SPREAD_ID_KEY, spreadId)
            bundle.putString(SPREAD_NAME_KEY, spreadName)
            bundle.putString(SPREAD_UNIQUE_NAME_KEY, uniqueName)
            fragment.arguments = bundle
            return fragment
        }
    }

}