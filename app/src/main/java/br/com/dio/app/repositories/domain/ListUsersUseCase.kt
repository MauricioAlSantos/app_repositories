package br.com.dio.app.repositories.domain;


import br.com.dio.app.repositories.core.UseCase
import br.com.dio.app.repositories.data.model.User
import br.com.dio.app.repositories.data.repositories.UserRepository;
import kotlinx.coroutines.flow.Flow

class ListUsersUseCase(private val repository:UserRepository): UseCase<String, List<User>>() {
    override suspend fun execute(param: String): Flow<List<User>> {
        return repository.listUsers(param)

    }
}
