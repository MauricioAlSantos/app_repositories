package br.com.dio.app.repositories.data.di

import br.com.dio.app.repositories.data.repositories.RepoRepository
import br.com.dio.app.repositories.data.repositories.RepoRepositoryImpl
import br.com.dio.app.repositories.data.repositories.UserRepository
import br.com.dio.app.repositories.data.repositories.UserRepositoryImpl
import br.com.dio.app.repositories.data.services.GitHubService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {

    fun load(){
        loadKoinModules(networkModules()+ repositoriesModule())
    }
    private fun networkModules(): Module {
        return module {
            val interceptor=HttpLoggingInterceptor{
               // println(it)
        }
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            single {
                OkHttpClient.Builder().addInterceptor(interceptor).build()
            }
            single {
                GsonConverterFactory.create(GsonBuilder().create())
            }
            single {
                createService<GitHubService>(get(),get())
            }
        }
    }
    private fun repositoriesModule():Module{
        return module {
            single<RepoRepository> {
                RepoRepositoryImpl(get())
            }
            single<UserRepository>{
                UserRepositoryImpl(get())
            }
        }
    }
    private inline fun <reified T> createService(client: OkHttpClient,factory: GsonConverterFactory): T {

        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .client(client)
            .addConverterFactory(factory)
            .build()
            .create(T::class.java)
    }
}