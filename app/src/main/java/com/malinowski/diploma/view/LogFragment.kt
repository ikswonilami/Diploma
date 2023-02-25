package com.malinowski.diploma.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.malinowski.diploma.databinding.FragmentLogBinding
import com.malinowski.diploma.model.getComponent
import com.malinowski.diploma.viewmodel.WifiDirectState
import com.malinowski.diploma.viewmodel.WifiDirectViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: WifiDirectViewModel by activityViewModels { factory }

    private lateinit var binding: FragmentLogBinding

    private val logView: TextView by lazy {
        binding.logsText
    }
    private val searchDevicesBtn: Button by lazy {
        binding.searchDevicesBtn
    }
    private val clearLogs: Button by lazy {
        binding.clearLogs
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::update)
            }
        }

        searchDevicesBtn.setOnClickListener {
            if (viewModel.checkPermissions(requireContext())) {
                viewModel.searchForDevices()
            }
        }
        clearLogs.setOnClickListener {
            viewModel.clearLog()
        }

    }

    private fun update(state: WifiDirectState) {
        logView.text = state.logText
    }

    companion object {
        fun newInstance() = LogFragment()
    }
}