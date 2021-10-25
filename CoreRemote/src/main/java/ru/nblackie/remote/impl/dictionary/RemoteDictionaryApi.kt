package ru.nblackie.remote.impl.dictionary

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.nblackie.remote.impl.dictionary.model.count.CountResponse
import ru.tatarchuk.personaldictionary.data.remote.rest.dictionary.model.NewWord
import ru.nblackie.remote.impl.dictionary.model.search.SearchResponse

/**
 * @author tatarchukilya@gmail.com
 */
interface RemoteDictionaryApi {

    @GET("/dictionary/dictionary.php")
    suspend fun getTranslation(): String

    @POST("/dictionary/add.php")
    suspend fun addWord(@Body data: NewWord): String

    @GET("/dictionary/search.php")
    suspend fun search(
        @Query("input") input: String,
        @Query("lang") lang: String,
        @Query("limit") limit: Int
    ): SearchResponse

    @POST("/dictionary/count.php")
    suspend fun count(
        @Query("lang") lang: String
    ): CountResponse
}