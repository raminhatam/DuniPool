package ir.hm.dunipool.apiManager


import ir.hm.dunipool.apiManager.model.CoinsData
import ir.hm.dunipool.apiManager.model.NewsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://min-api.cryptocompare.com/data/"
const val BASE_URL_IMAGE = "https://www.cryptocompare.com"
const val API_KEY = "authorization: Apikey f6a97dc713370c5c306c22278af5b1aac74dac6b911e8c169ccf6451c3a75446"
const val APP_NAME = "Test App"

class ApiManager {
    private val apiService : ApiService

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getNews( apiCallBack: ApiCallBack<ArrayList<Pair<String, String>>> ){

        apiService.getTopNews().enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {

                val data = response.body()!!
                val dataToSend : ArrayList<Pair<String, String>> = arrayListOf()

                data.data.forEach {

                    dataToSend.add(Pair( it.title, it.url ))

                }
                apiCallBack.onSuccess(dataToSend)

            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {

                apiCallBack.onFailure(t.message!!)

            }
        } )
    }

    fun getCoinsList(apiCallBack: ApiCallBack<List<CoinsData.Data>>){

        apiService.getTopsCoins().enqueue( object : Callback<CoinsData>{
            override fun onResponse(call: Call<CoinsData>, response: Response<CoinsData>) {

                val data = response.body()!!
                apiCallBack.onSuccess(data.data)
            }

            override fun onFailure(call: Call<CoinsData>, t: Throwable) {
                apiCallBack.onFailure(t.message!!)
            }


        })

    }


    interface ApiCallBack<T>{
        fun onSuccess(data:T)
        fun onFailure(errorMessage:String)
    }

}