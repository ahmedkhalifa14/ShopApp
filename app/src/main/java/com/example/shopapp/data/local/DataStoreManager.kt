package com.example.shopapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopapp.models.UserInfo
import com.example.shopapp.utils.Constants.isFirstTimeLaunchKey
import com.example.shopapp.utils.Constants.userLatitudeKey
import com.example.shopapp.utils.Constants.userLongitudeKey
import com.example.shopapp.utils.Constants.userMailKey
import com.example.shopapp.utils.Constants.userPasswordKey
import com.example.shopapp.utils.Constants.userTokenKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

class DataStoreManager(context: Context) {
    private val Context.isFirstTimeLaunchDataStore:
            DataStore<Preferences> by preferencesDataStore("IS_FIRST_TIME_LAUNCH_KEY")
    private val isFirstTimeLaunchDataStore = context.isFirstTimeLaunchDataStore
    private val Context.userInfoDataStore:
            DataStore<Preferences> by preferencesDataStore(name = "USER_INFO_KEY")
    private val userInfoDataStore = context.userInfoDataStore

    private val _userToken = MutableLiveData<UserInfo>()
    val userToken: LiveData<UserInfo> get() = _userToken
    private val scope = CoroutineScope(Job() + Dispatchers.Main)


    companion object {

    }

    suspend fun setUserInfo(userInfo: UserInfo) {
        userInfoDataStore.edit { preferences ->
            preferences[userMailKey] = userInfo.email.toString()
            preferences[userPasswordKey] = userInfo.password.toString()
            preferences[userTokenKey] = userInfo.token.toString()
            preferences[userLatitudeKey] =userInfo.latitude.toString()
            preferences[userLongitudeKey] =userInfo.longitude.toString()

        }
    }

    fun getUserInfo(): Flow<UserInfo> {
        return userInfoDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                UserInfo(
                    preferences[userMailKey] ?: "",
                    preferences[userPasswordKey] ?: "",
                    preferences[userTokenKey] ?: "",
                    preferences[userLatitudeKey]?:"",
                    preferences[userLongitudeKey]?:""
                )
            }
    }

    suspend fun setFirstTimeLaunch(isFirstTimeLaunch: Boolean) {
        isFirstTimeLaunchDataStore.edit { preferences ->
            preferences[isFirstTimeLaunchKey] = isFirstTimeLaunch
        }
    }

    fun isFirstTimeLaunch(): Flow<Boolean> {
        return isFirstTimeLaunchDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val isFirstTimeLaunch = preferences[isFirstTimeLaunchKey] ?: false
                isFirstTimeLaunch
            }
    }

    init {
        getTokenUser()
    }
    private fun getTokenUser() {
        scope.launch {
            getUserInfo().collect { token ->
                _userToken.postValue(token)
            }
        }

    }

}