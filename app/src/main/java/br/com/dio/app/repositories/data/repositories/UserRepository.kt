package br.com.dio.app.repositories.data.repositories

import br.com.dio.app.repositories.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun listUsers(query:String):Flow<List<User>>
}