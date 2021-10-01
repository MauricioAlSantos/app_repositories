package br.com.dio.app.repositories.data.repositories

import androidx.paging.PagingData
import androidx.paging.map
import br.com.dio.app.repositories.core.RemoteException
import br.com.dio.app.repositories.data.model.User
import br.com.dio.app.repositories.data.services.GitHubService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

class UserRepositoryImpl(private val service: GitHubService): UserRepository {
    override suspend fun listUsers(query: String)= flow {
        try {
            val list = service.listUsers("$query+in:login","Users&page=1&per_page=20").results
            emit(list)
        }catch (ex:HttpException){
            throw RemoteException(ex.message()?:"Não foi possível buscar no momento")
        }

    }
}