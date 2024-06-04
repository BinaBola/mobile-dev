package com.binabola.app.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    private val selectedRole = MutableLiveData<String>()
    private val userData = MutableLiveData<Map<String, String>>()
    private val credential = MutableLiveData<Map<String, String>>()

    /** Example data:
     * mapOf(
        "role" to "club | coach | player",
    ) */
    fun storeRole(role: String) {
        selectedRole.postValue(role)
    }

    fun getRole(): LiveData<String>{
        return selectedRole
    }

    /** Example data:
     * mapOf(
        "nama" to "Name",
        "gender" to "L | P",
        "dob" to "2003/07/03",
        "tinggi" to "170",
        "berat" to "68"
    ) */
    fun storeData(data: Map<String, String>) {
        userData.postValue(data)
    }

    fun getUserData(): LiveData<Map<String, String>> {
        return userData
    }

    /** Example data:
     * mapOf(
        "email" to "email@mail.com",
        "password" to "password"
    ) */
    fun storeCredential(data: Map<String, String>) {
        credential.postValue(data)
    }

    fun getCredential(): LiveData<Map<String, String>> {
        return credential
    }

}