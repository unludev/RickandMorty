package com.unludev.rickandmorty.data.repository.location

import com.unludev.rickandmorty.data.NetworkResponse
import com.unludev.rickandmorty.data.datasource.location.LocationRemoteDataSource
import com.unludev.rickandmorty.data.model.location.Location
import com.unludev.rickandmorty.data.model.location.LocationList
import com.unludev.rickandmorty.di.coroutine.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ILocationRepository @Inject constructor(
    private val locationRemoteDataSource: LocationRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : LocationRepository {
    override suspend fun getLocations(): Flow<NetworkResponse<LocationList>> {
        return flow {
            emit(NetworkResponse.Loading)
            val response = withContext(ioDispatcher) {
                try {
                    locationRemoteDataSource.getLocations()
                } catch (e: Exception) {
                    NetworkResponse.Error(e)
                }
            }
            when (response) {
                is NetworkResponse.Success -> emit(NetworkResponse.Success(response.result))
                is NetworkResponse.Error -> emit(NetworkResponse.Error(response.exception))
                NetworkResponse.Loading -> Unit
            }
        }
    }

    override suspend fun getLocation(id: Int): Flow<NetworkResponse<Location>> {
        return flow {
            emit(NetworkResponse.Loading)
            val response = withContext(ioDispatcher) {
                try {
                    locationRemoteDataSource.getLocation(id)
                } catch (e: Exception) {
                    NetworkResponse.Error(e)
                }
            }
            when (response) {
                is NetworkResponse.Success -> emit(NetworkResponse.Success(response.result))
                is NetworkResponse.Error -> emit(NetworkResponse.Error(response.exception))
                NetworkResponse.Loading -> Unit
            }
        }
    }
}
