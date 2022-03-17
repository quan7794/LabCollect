package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentHomeBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.firstScreen.FirstScreenFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class HomeFragment : BaseFragment<FragmentHomeBinding>(), TestListAdapter.OnTestClick {
    private val viewModel: HomeTabViewModel by viewModels { HomeTabViewModelFactory(testRepository) }
    private val testListAdapter: TestListAdapter by lazy { TestListAdapter(testClickCallback = this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchAction()
        initTestList()
//        createSheet()
    }

    private fun createSheet() {
        val scopes = listOf(SheetsScopes.SPREADSHEETS)
        val credentials = GoogleAccountCredential.usingOAuth2(context, scopes)
        credentials.selectedAccount = lastSignedAccount()?.account
        val service = Sheets.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance(), credentials)
            .setApplicationName("Lab Collect")
            .build()

        var spreadsheet = Spreadsheet().setProperties(SpreadsheetProperties().setTitle("Excel1"))

        lifecycleScope.launch(Dispatchers.IO) {
            spreadsheet = service.spreadsheets()
                .create(spreadsheet)
                .setFields("spreadsheetId")
                .setFields("spreadsheetUrl")
                .execute()
            Timber.e("Spreadsheet ID: " + spreadsheet.spreadsheetId + ", " + spreadsheet.spreadsheetUrl)
        }
        // Prints the new spreadsheet id
    }

    private fun initTestList() {
        binding.testList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = testListAdapter
        }
        viewModel.tests.observe(viewLifecycleOwner) { testList ->
            testListAdapter.dataSet = testList
        }
    }

    private fun initSearchAction() {
        binding.apply {
            shortcutSearch.setOnClickListener {
                navigate(FirstScreenFragmentDirections.actionFirstScreenFragmentToSearchFragment())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.home)
    }

    override fun onClick(uniqueName: String) {
        findNavController().navigate(FirstScreenFragmentDirections.actionFirstScreenFragmentToManageTestFragment(uniqueName))
    }

}