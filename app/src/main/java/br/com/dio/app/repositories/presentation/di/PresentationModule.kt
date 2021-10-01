package br.com.dio.app.repositories.presentation.di

import br.com.dio.app.repositories.domain.ListUserRepositoriesUseCase
import br.com.dio.app.repositories.presentation.MainViewModel
import br.com.dio.app.repositories.presentation.UsersViewModel
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object PresentationModule {
    fun load(){
        loadKoinModules(viewModelModule()+usersViewModelModule())
    }
    private fun viewModelModule(): Module {
        return module {
            viewModel{
                MainViewModel(get())

            }
        }
    }
    private fun usersViewModelModule(): Module {
        return module {
            viewModel{
                UsersViewModel(get())

            }
        }
    }

}