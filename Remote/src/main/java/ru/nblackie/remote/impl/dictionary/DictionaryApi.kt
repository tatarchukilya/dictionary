package ru.nblackie.remote.impl.dictionary

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.tatarchuk.personaldictionary.data.remote.rest.dictionary.model.NewWord
import ru.nblackie.remote.impl.dictionary.model.SearchResponse

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryApi {

    @GET("/dictionary/dictionary.php")
    suspend fun getTranslation(): String

    @POST("/dictionary/add.php")
    suspend fun addWord(@Body data: NewWord): String

    @GET("/dictionary/search.php")
    suspend fun search(
        @Query("input") input: String,
        @Query("limit") limit: Int
    ) : SearchResponse
}