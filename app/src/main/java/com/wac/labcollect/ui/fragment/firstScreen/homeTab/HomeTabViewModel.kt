package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import androidx.lifecycle.*
import com.wac.labcollect.data.repository.googleApi.GoogleApiRepository
import com.wac.labcollect.data.repository.test.TestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeTabViewModel(val testRepository: TestRepository, val googleApiRepository: GoogleApiRepository) : ViewModel() {
    private var _spreads = MutableLiveData<List<Triple<String, String, List<String>>>>()
    val spreads : LiveData<List<Triple<String, String, List<String>>>>
        get() = _spreads

    fun getSpreads() {
        viewModelScope.launch(Dispatchers.IO) {
            val spreads = googleApiRepository.getFilesAtRoot()
            _spreads.postValue(spreads)
        }
    }
}

class HomeTabViewModelFactory(val repository: TestRepository, val googleApiRepository: GoogleApiRepository) : ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeTabViewModel::class.java))
            return HomeTabViewModel(repository, googleApiRepository) as T
        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
    }
}