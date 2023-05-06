package com.example.shopapp.ui.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.shopapp.data.local.DataStoreManager
import com.example.shopapp.helper.Validation
import com.example.shopapp.models.*
import com.example.shopapp.repository.AuthRepositoryImpl
import com.example.shopapp.utils.Constants
import com.example.shopapp.utils.Event
import com.example.shopapp.utils.Resource
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    @ApplicationContext val context: Context,
    private val dataStoreManager: DataStoreManager

) : ViewModel() {

    private val _registerStatus =
        MutableStateFlow<Event<Resource<AuthResponse<RegisterResponse>>>>(Event(Resource.Init()))
    val registerStatus: MutableStateFlow<Event<Resource<AuthResponse<RegisterResponse>>>> = _registerStatus
    private val _loginUserStatus =
        MutableStateFlow<Event<Resource<AuthResponse<LoginResponse>>>>(Event(Resource.Init()))
    val loginUserStatus: MutableStateFlow<Event<Resource<AuthResponse<LoginResponse>>>> =
        _loginUserStatus


    private val _profileStatus =
        MutableStateFlow<Event<Resource<AuthResponse<Profile>>>>(Event(Resource.Init()))
    val profileStatus: MutableStateFlow<Event<Resource<AuthResponse<Profile>>>> =
        _profileStatus

     fun getUserProfileInfo(){
         viewModelScope.launch(Dispatchers.Main) {
             _profileStatus.emit(Event(Resource.Loading()))
             val result = authRepository.getUserProfileInfo()
             _profileStatus.emit(Event(result))
         }
     }
    fun registerUser(
        inputTextLayoutUserName: TextInputLayout,
        inputTextLayoutMobile: TextInputLayout,
        inputTextLayoutEmail: TextInputLayout,
        inputTextLayoutPassword: TextInputLayout,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val username = inputTextLayoutUserName.editText!!.text.toString()
            val mobile = inputTextLayoutMobile.editText!!.text.toString()
            val email = inputTextLayoutEmail.editText!!.text.toString()
            val password = inputTextLayoutPassword.editText!!.text.toString()
            when {
                username.isEmpty() -> {
                    _registerStatus.emit(Event(Resource.Error("Username is required")))
                    inputTextLayoutUserName.isHelperTextEnabled = true
                    inputTextLayoutUserName.helperText = "Required*"
                }

                email.isEmpty() -> {
                    _registerStatus.emit(Event(Resource.Error("Email is required")))
                    inputTextLayoutEmail.isHelperTextEnabled = true
                    inputTextLayoutEmail.helperText = "Required*"
                }

                !Validation.isValidEmail(email = email) -> {
                    _registerStatus.emit(Event(Resource.Error("Email is not valid")))
                    inputTextLayoutEmail.isHelperTextEnabled = true
                    inputTextLayoutEmail.helperText = "not valid"
                }
                mobile.isEmpty() -> {
                    _registerStatus.emit(Event(Resource.Error("Password is required")))
                    inputTextLayoutMobile.isHelperTextEnabled = true
                    inputTextLayoutMobile.helperText = "Required*"
                }
                !Validation.validateMobile(mobile) -> {
                    _registerStatus.emit(Event(Resource.Error("Phone Number is not valid")))
                    inputTextLayoutMobile.isHelperTextEnabled = true
                    inputTextLayoutMobile.helperText = "Mobile not valid"
                }

                password.isEmpty() -> {
                    _registerStatus.emit(Event(Resource.Error("Password is required")))
                    inputTextLayoutPassword.isHelperTextEnabled = true
                    inputTextLayoutPassword.helperText = "Required*"
                }
                !Validation.validatePass(context, inputTextLayoutPassword) -> {
                    _registerStatus.emit(Event(Resource.Error(inputTextLayoutPassword.helperText.toString())))
                }

                else -> {
                    _registerStatus.emit(Event(Resource.Loading()))
                    Log.d("viewModel",email)
                    val result = authRepository.registerUser(
                        username,
                        mobile,
                        email,
                        password,
                        Constants.IMAGE_URL,
                        )
                //  Log.d("viewModel", result.data!!.data!!.email)
                    _registerStatus.emit(Event(result))
                }
            }
        }


    }
    fun loginUser(
        inputTextLayoutEmail: TextInputLayout,
        inputTextLayoutPassword: TextInputLayout,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val email = inputTextLayoutEmail.editText!!.text.toString()
            val password = inputTextLayoutPassword.editText!!.text.toString()
            when {
                email.isEmpty() -> {
                    _loginUserStatus.emit(Event(Resource.Error("Email is require")))
                    inputTextLayoutEmail.isHelperTextEnabled = true
                    inputTextLayoutEmail.helperText = "Required*"
                }

                !Validation.isValidEmail(email = email) -> {
                    _loginUserStatus.emit(Event(Resource.Error("Email is not valid")))
                    inputTextLayoutEmail.isHelperTextEnabled = true
                    inputTextLayoutEmail.helperText = "not valid"
                }
                password.isEmpty() -> {
                    _loginUserStatus.emit(Event(Resource.Error("Password is require")))
                    inputTextLayoutPassword.isHelperTextEnabled = true
                    inputTextLayoutPassword.helperText = "Required*"
                }
                !Validation.validatePass(context, inputTextLayoutPassword) -> {
                    _loginUserStatus.emit(Event(Resource.Error(inputTextLayoutPassword.helperText.toString())))
                }
                else -> {
                    _loginUserStatus.emit(Event(Resource.Loading()))
                    val result = authRepository.loginUser(email, password)
                    _loginUserStatus.emit(Event(result))
                }
            }
        }
    }


    val isFirstTimeLaunch = dataStoreManager.isFirstTimeLaunch().asLiveData(Dispatchers.IO)
    val getUserInfo = dataStoreManager.getUserInfo().asLiveData(Dispatchers.IO)

    fun setFirstTimeLaunch(isFirstTimeLaunch: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            dataStoreManager.setFirstTimeLaunch(isFirstTimeLaunch)

        }
    }

    fun setUserINfo(userInfo: UserInfo) {
        GlobalScope.launch(Dispatchers.IO) {
            dataStoreManager.setUserInfo(userInfo)
        }
    }
}