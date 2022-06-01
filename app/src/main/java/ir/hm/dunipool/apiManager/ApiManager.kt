package ir.hm.dunipool.apiManager


import ir.dunijet.dunipool.apiManager.model.ChartData
import ir.hm.dunipool.apiManager.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://min-api.cryptocompare.com/data/"
const val BASE_URL_IMAGE = "https://www.cryptocompare.com"
const val API_KEY = "authorization: Apikey f6a97dc713370c5c306c22278af5b1aac74dac6b911e8c169ccf6451c3a75446"
const val BASE_URL_TWITTER = "https://twitter.com/"


class ApiManager {
    private val apiService: ApiService

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getNews(apiCallBack: ApiCallBack<ArrayList<Pair<String, String>>>) {

        apiService.getTopNews().enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {

                val data = response.body()!!
                val dataToSend: ArrayList<Pair<String, String>> = arrayListOf()

                data.data.forEach {

                    dataToSend.add(Pair(it.title, it.url))

                }
                apiCallBack.onSuccess(dataToSend)

            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {

                apiCallBack.onFailure(t.message!!)

            }
        })
    }

    fun getCoinsList(apiCallBack: ApiCallBack<List<CoinsData.Data>>) {

        apiService.getTopsCoins().enqueue(object : Callback<CoinsData> {
            override fun onResponse(call: Call<CoinsData>, response: Response<CoinsData>) {

                val data = response.body()!!
                apiCallBack.onSuccess(data.data)
            }

            override fun onFailure(call: Call<CoinsData>, t: Throwable) {
                apiCallBack.onFailure(t.message!!)
            }


        })

    }

    fun getChartData(
        symbol: String,
        period: String,
            apiCallback: ApiCallBack<Pair<List<ChartData.Data>, ChartData.Data?>>
    ) {

        var histoPeriod = ""
        var limit = 30
        var aggregate = 1

        when (period) {

            HOUR -> {
                histoPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 12
            }

            HOURS24 -> {
                histoPeriod = HISTO_HOUR
                limit = 24
            }

            MONTH -> {
                histoPeriod = HISTO_DAY
                limit = 30
            }

            MONTH3 -> {
                histoPeriod = HISTO_DAY
                limit = 90
            }

            WEEK -> {
                histoPeriod = HISTO_HOUR
                aggregate = 6
            }

            YEAR -> {
                histoPeriod = HISTO_DAY
                aggregate = 13
            }

            ALL -> {
                histoPeriod = HISTO_DAY
                aggregate = 30
                limit = 2000
            }

        }

        apiService.getChartData(histoPeriod, symbol, limit, aggregate)
            .enqueue(object : Callback<ChartData> {
                override fun onResponse(call: Call<ChartData>, response: Response<ChartData>) {

                    val dataFull = response.body()!!
                    val data1 = dataFull.data
                    val data2 = dataFull.data.maxByOrNull { it.close.toFloat() }
                    val returningData = Pair(data1, data2)

                    apiCallback.onSuccess(returningData)

                }

                override fun onFailure(call: Call<ChartData>, t: Throwable) {
                    apiCallback.onFailure(t.message!!)
                }

            })

    }


    interface ApiCallBack<T> {
        fun onSuccess(data: T)
        fun onFailure(errorMessage: String)
    }

}


