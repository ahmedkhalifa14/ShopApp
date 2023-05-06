package com.example.shopapp.ui.auth

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentLoginBinding
import com.example.shopapp.models.UserInfo
import com.example.shopapp.utils.EventObserver
import com.example.shopapp.utils.snackbar
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : Fragment(), EasyPermissions.PermissionCallbacks {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // private var latLng: LatLng?=null
    private lateinit var latLng: LatLng

    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    private val REQUEST_CHECK_SETTINGS = 2


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.apply {
//            authViewModel.getUserInfo.observe(requireActivity()) { userInfo ->
//                inputTextEmail.setText(userInfo.email)
//                inputTextPassword.setText(userInfo.password)
//            }
//        }
        binding.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.inputTextEmail.doAfterTextChanged {
            binding.inputTextLayoutEmail.isHelperTextEnabled = false
        }
        binding.inputTextPassword.doAfterTextChanged {
            binding.inputTextLayoutPassword.isHelperTextEnabled = false
        }
        binding.loginBtn.setOnClickListener {
            checkLocationSettings()
        }
        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        subscribeToObservables()

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkLocationSettings() {

        if (hasLocationPermission()) {
            val locationRequest =
                // LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            val builder =
                LocationSettingsRequest.Builder().addLocationRequest(locationRequest.build())
            val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                // Location settings are satisfied, get location
                getLocation()
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, show dialog to enable location
                    try {
                        exception.startResolutionForResult(
                            requireActivity(), REQUEST_CHECK_SETTINGS
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error
                    }
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latLng = LatLng(location.latitude, location.longitude)
                login()
            } else {
                // Location is null
            }
        }.addOnFailureListener { exception ->
            // Location request failed
            Timber.tag("fusedLocationClientException").d(exception)
        }
    }

    private fun subscribeToObservables() {
        lifecycleScope.launchWhenStarted {
            authViewModel.loginUserStatus.collect(EventObserver(onLoading = {
                binding.spinKit.isVisible = true
                Timber.tag("onLoading").d("onLoading")
            }, onError = { errorMassage ->
                binding.spinKit.isVisible = false
                Timber.tag("onError").d(errorMassage)
                snackbar(errorMassage)
            }, onSuccess = {
                binding.spinKit.isVisible = false
                Timber.tag("onSuccess").d(it.message)
                snackbar(it.message)
                it.data.let {
                    lifecycleScope.launch {
                        if (it != null) {
                            Timber.tag("loginToken").d(it.token)
                            saveDataAndNavigate(it.token)
                        }
                    }
                }

            }))


        }
    }

    private fun saveDataAndNavigate(userToken: String) {
        val userEmail = binding.inputTextEmail.text.toString()
        val userPassword = binding.inputTextPassword.text.toString()
        updateUserToken(userEmail, userPassword, userToken, latLng)

        if (binding.switchRememberMe.isChecked) {
            saveEmailAndPassword(userEmail, userPassword, userToken, latLng)
        }

        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)


    }

    private fun saveEmailAndPassword(
        userEmail: String,
        userPassword: String,
        token: String,
        latLng: LatLng
    ) {
        lifecycleScope.launch {
            authViewModel.setUserINfo(
                UserInfo(
                    userEmail,
                    userPassword,
                    token,
                    latLng.latitude.toString(),
                    latLng.longitude.toString()
                )
            )
        }
    }

    private fun updateUserToken(
        userEmail: String,
        userPassword: String,
        userToken: String,
        latLng: LatLng
    ) {
        authViewModel.setUserINfo(
            UserInfo(
                userEmail,
                userPassword,
                userToken,
                latLng.latitude.toString(),
                latLng.longitude.toString()
            )
        )
    }

    private fun login() {
        authViewModel.loginUser(
            binding.inputTextLayoutEmail,
            binding.inputTextLayoutPassword,
        )

    }


    private fun hasLocationPermission() = EasyPermissions.hasPermissions(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_WIFI_STATE
    )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application cannot work without Location Permission.",
            PERMISSION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            requestLocationPermission()
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(), "Permission Granted", Toast.LENGTH_SHORT
        ).show()
        checkLocationSettings()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}