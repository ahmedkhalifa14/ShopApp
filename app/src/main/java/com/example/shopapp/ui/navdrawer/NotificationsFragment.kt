package com.example.shopapp.ui.navdrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.adapters.NotificationAdapter
import com.example.shopapp.databinding.FragmentNotificationsBinding
import com.example.shopapp.models.Notifications
import com.example.shopapp.ui.home.MainViewModel
import com.example.shopapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var notificationsList: ArrayList<Notifications>
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var notificationRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToObservers()

    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getNotifications()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.getNotificationsStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding.spinKit.isVisible = true
                        },
                        onSuccess = { data ->
                            binding.spinKit.isVisible = false
                            notificationAdapter.differ.submitList(data.data!!.data)
                        },
                        onError = {
                            binding.spinKit.isVisible = false
                        }
                    )
                )

            }

        }

    }

    private fun setupRecyclerView() {
        notificationRecyclerView = binding.notificationRV
        notificationsList = ArrayList()
        notificationAdapter = NotificationAdapter(notificationsList)
        notificationRecyclerView.layoutManager =
                // StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        notificationRecyclerView.adapter = notificationAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}