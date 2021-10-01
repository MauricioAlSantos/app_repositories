package br.com.dio.app.repositories.domain.di

import br.com.dio.app.repositories.domain.ListUserRepositoriesUseCase
import br.com.dio.app.repositories.domain.ListUsersUseCase
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule{
    fun load(){
        loadKoinModules(useCaseModule()+ usersUseCaseModule())
    }
    private fun useCaseModule():Module {
        return module {
            factory {
                ListUserRepositoriesUseCase(get())

            }
        }
    }
    private fun usersUseCaseModule():Module {
        return module {
            factory {
                ListUsersUseCase(get())
            }
        }
    }
}