package br.com.dio.app.repositories.data.services



import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.data.model.User
import com.google.gson.annotations.SerializedName

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class ResponseItems<T>(
    @SerializedName("items")
    val results: List<T>
)

interface GitHubService {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user:String?): List<Repo>

    @GET("search/users")
    suspend fun listUsers(@Query("q",encoded = true) user:String?,@Query("type") type:String): ResponseItems<User>

}