package com.example.shopapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentRegisterBinding
import com.example.shopapp.utils.EventObserver
import com.example.shopapp.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputTextUserName.doAfterTextChanged {
            binding.inputTextLayoutUserName.isHelperTextEnabled = false
        }
        binding.inputTextEmail.doAfterTextChanged {
            binding.inputTextLayoutEmail.isHelperTextEnabled = false
        }
        binding.inputTextPhone.doAfterTextChanged {
            binding.inputTextLayoutPhone.isHelperTextEnabled = false
        }
        binding.inputTextPassword.doAfterTextChanged {
            binding.inputTextLayoutPassword.isHelperTextEnabled = false
        }

        binding.alreadyHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        binding.signUpBtn.setOnClickListener {
            registerUser()
        }


        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        lifecycleScope.launchWhenStarted {
            authViewModel.registerStatus.collect(
                EventObserver(onLoading = {
                    binding.spinKit.isVisible = true
                },
                    onSuccess = {authResponse ->
                        binding.spinKit.isVisible = false
                        snackbar((authResponse.data?.email) + "  success")
                        authResponse.data.let {authResponse ->
                            if (authResponse != null) {
                                Timber.tag("register").d(authResponse.email)
                            }
                            lifecycleScope.launch {
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            }
                        }
                    },
                    onError = {
                        binding.spinKit.isVisible = false
                        snackbar(it)
                        Timber.tag("register").d(it)
                    })
            )
        }
    }

    private fun registerUser() {
        authViewModel.registerUser(
            binding.inputTextLayoutUserName,
            binding.inputTextLayoutPhone,
            binding.inputTextLayoutEmail,
            binding.inputTextLayoutPassword,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}