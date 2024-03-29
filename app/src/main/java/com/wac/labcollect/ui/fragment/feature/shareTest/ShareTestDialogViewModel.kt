package com.wac.labcollect.ui.fragment.feature.shareTest

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wac.labcollect.data.repository.googleApi.GoogleApiConstant.SPREAD_BASE_URL
import com.wac.labcollect.data.repository.googleApi.GoogleApiRepository
import com.wac.labcollect.utils.Constants.APP_CONSTANT
import net.glxn.qrgen.android.QRCode
import net.glxn.qrgen.core.image.ImageType

class ShareTestDialogViewModel(val googleApiRepository: GoogleApiRepository) : ViewModel() {
    val testUrl = MutableLiveData<String>()
    val qrImage = MutableLiveData<Bitmap>()

    fun init(spreadId: String) {
        testUrl.postValue(SPREAD_BASE_URL + spreadId)
        qrImage.postValue(QRCode.from(APP_CONSTANT + spreadId).to(ImageType.PNG).withSize(1000, 1000).bitmap())
    }

}

class ShareTestDialogViewModelFactory(val googleApiRepository: GoogleApiRepository) : ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShareTestDialogViewModel::class.java))
            return ShareTestDialogViewModel(googleApiRepository) as T
        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
    }
}