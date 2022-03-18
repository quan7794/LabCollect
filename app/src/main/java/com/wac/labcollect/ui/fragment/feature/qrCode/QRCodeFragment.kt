package com.wac.labcollect.ui.fragment.feature.qrCode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentQRCodeBinding
import com.wac.labcollect.ui.base.BaseFragment
import org.json.JSONException
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class QRCodeFragment : BaseFragment<FragmentQRCodeBinding>(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    var hide: Animation? = null
    var reveal: Animation? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            hide = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_out)
            reveal = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)

            tvText.startAnimation(reveal)
            cardView2.startAnimation(reveal)
            tvText.text = getString(R.string.scan_qr_code_here)
            cardView2.visibility = View.VISIBLE

            btnScan.setOnClickListener {
                tvText.startAnimation(reveal)
                cardView1.startAnimation(hide)
                cardView2.startAnimation(reveal)

                cardView2.visibility = View.VISIBLE
                cardView1.visibility = View.GONE
                tvText.text = getString(R.string.scan_qr_code_here)
            }

            cardView2.setOnClickListener {
                cameraTask()
            }

            btnEnter.setOnClickListener {
                if (edtCode.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter code", Toast.LENGTH_SHORT).show()
                } else {
                    val value = edtCode.text.toString()
                    Toast.makeText(requireContext(), value, Toast.LENGTH_SHORT).show()
                }
            }
            btnEnterCode.setOnClickListener {
                tvText.startAnimation(reveal)
                cardView1.startAnimation(reveal)
                cardView2.startAnimation(hide)
                cardView2.visibility = View.GONE
                cardView1.visibility = View.VISIBLE
                tvText.text = getString(R.string.enter_qrcode)
            }
        }
    }

    private fun hasCameraAccess(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), android.Manifest.permission.CAMERA)
    }

    private fun cameraTask() {
        if (hasCameraAccess()) {
            val qrScanner = IntentIntegrator.forSupportFragment(this)
            qrScanner.setPrompt("scan a QR code")
            qrScanner.setCameraId(0)
            qrScanner.setOrientationLocked(true)
            qrScanner.setBeepEnabled(true)
            qrScanner.captureActivity = CaptureActivity::class.java
            qrScanner.initiateScan()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your camera so you can take pictures.",
                123,
                android.Manifest.permission.CAMERA
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            binding.apply {
                if (result.contents == null) {
                    Toast.makeText(requireContext(), "Result Not Found", Toast.LENGTH_SHORT).show()
                    edtCode.setText("")
                } else {
                    try {
                        tvText.text = getString(R.string.enter_qrcode)
                        cardView1.startAnimation(reveal)
                        cardView2.startAnimation(hide)
                        cardView1.visibility = View.VISIBLE
                        cardView2.visibility = View.GONE
                        edtCode.setText(result.contents.toString())
                    } catch (exception: JSONException) {
                        Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
                        edtCode.setText("")
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRationaleDenied(requestCode: Int) {
    }

    override fun onRationaleAccepted(requestCode: Int) {
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.qr_code)
    }

}