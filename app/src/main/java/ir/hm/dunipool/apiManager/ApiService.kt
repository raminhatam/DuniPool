package ir.hm.dunipool.apiManager

import ir.hm.dunipool.apiManager.model.CoinsData
import ir.hm.dunipool.apiManager.model.NewsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers(API_KEY)
    @GET("v2/news/")
    fun getTopNews(
        @Query("sortOrder") sortOrder : String = "popular"
    ) : Call<NewsData>


    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun getTopsCoins(
        @Query( "tsym" ) to_symbol : String = "USD",
        @Query("limit") limit_data : Int = 20
    ): Call<CoinsData>

}