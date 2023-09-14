package com.example.tmdbapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.helper.ResponseHelper
import com.example.tmdbapp.model.people.Cast
import com.example.tmdbapp.model.people.Person
import com.example.tmdbapp.repository.PersonRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonViewModel(id:Int) : ViewModel() {

    private val personRepository = PersonRepositoryImpl()

    private val _personDetail = MutableLiveData< ResponseHelper<Person>>()
    val personDetail: LiveData<ResponseHelper<Person>> get() = _personDetail

    private val _movieCredits = MutableLiveData< ResponseHelper<List<Cast>>>()
    val movieCredits: LiveData<ResponseHelper<List<Cast>>> get() = _movieCredits

    fun getPersonDetail(personId : Int) {
        viewModelScope.launch {
            _personDetail.postValue(ResponseHelper.Loading())
            val personDetailsResponse = withContext(Dispatchers.IO) {
                personRepository.getPersonDetail(personId)
            }
            _personDetail.postValue(personDetailsResponse)
        }
    }

    fun getMovieCredits(personId : Int) {
        viewModelScope.launch {
            _movieCredits.postValue(ResponseHelper.Loading())
            val movieCreditsResponse = withContext(Dispatchers.IO) {
                personRepository.getPersonMovieCredits(personId)
            }
            _movieCredits.postValue(movieCreditsResponse)
        }
    }

    init{
        getPersonDetail(id)
        getMovieCredits(id)
    }


}