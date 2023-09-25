package com.example.tmdbapp.repository

import com.example.tmdbapp.helper.ResponseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

open class BaseRepository {

     fun <T> executeAPI(executeAPITask: suspend () -> Response<T>): Flow<ResponseHelper<T>> = flow {
        emit(ResponseHelper.Loading())
        val res = executeAPITask.invoke()
        if (res.isSuccessful) {
            emit(ResponseHelper.Success(res.body()))

        } else {
            emit(ResponseHelper.Error(""))
        }
    }.flowOn(Dispatchers.IO).catch { emit(ResponseHelper.Error("Error occured")) }

}