package ir.hm.dunipool.features.marketActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ir.hm.dunipool.CoinActivity
import ir.hm.dunipool.R
import ir.hm.dunipool.apiManager.ApiManager
import ir.hm.dunipool.apiManager.model.CoinAboutData
import ir.hm.dunipool.apiManager.model.CoinAboutItem
import ir.hm.dunipool.apiManager.model.CoinsData
import ir.hm.dunipool.databinding.ActivityMarketBinding


class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallback {
    lateinit var binding: ActivityMarketBinding
    val apiManager = ApiManager()
    lateinit var adapter: MarketAdapter
    lateinit var dataNews: ArrayList<Pair<String, String>>
    lateinit var aboutDataMap : MutableMap<String,CoinAboutItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutToolbar.toolbar.title = "Duni Pool Market"
        
        binding.layoutWatchlist.btnShowMore.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.livecoinwatch.com/"))
            startActivity(intent)

        }
        binding.swipeRefreshMain.setOnRefreshListener {
            initUi()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshMain.isRefreshing = false
            }, 1500)

        }

        getAboutDataFromAsset()


    }
    override fun onResume() {
        super.onResume()
        initUi()
    }

    private fun initUi() {


        getNewsFromApi()
        getTopCoinsFromApi()

    }

    private fun getNewsFromApi() {

        apiManager.getNews(object : ApiManager.ApiCallBack<ArrayList<Pair<String, String>>> {
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                dataNews = data
                refreshNews()

            }

            override fun onFailure(errorMessage: String) {
                Toast.makeText(this@MarketActivity, "Error =>$errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }


        })

    }

    private fun refreshNews() {

        val randomAccess = (0..49).random()
        binding.moduleNews.txtNews.text = dataNews[randomAccess].first
        useXlmAnimation()

        binding.moduleNews.imgNews.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataNews[randomAccess].second))
            startActivity(intent)

        }

        binding.moduleNews.txtNews.setOnClickListener {
            refreshNews()
        }
    }

    private fun useXlmAnimation() {

        val anim = AnimationUtils.loadAnimation(this, R.anim.anim_alpha)
        anim.fillAfter = true
        anim.duration = 500

        binding.moduleNews.txtNews.startAnimation(anim)

    }

    private fun getTopCoinsFromApi() {
        apiManager.getCoinsList(object : ApiManager.ApiCallBack<List<CoinsData.Data>> {
            override fun onSuccess(data: List<CoinsData.Data>) {

                showDataInRecycler(data)

            }

            override fun onFailure(errorMessage: String) {

                Toast.makeText(this@MarketActivity, "Error =>$errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }


        })
    }
    private fun showDataInRecycler(data: List<CoinsData.Data>) {

        adapter = MarketAdapter(this, ArrayList(data))
        binding.layoutWatchlist.recyclerMain.adapter = adapter
        binding.layoutWatchlist.recyclerMain.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )


    }
    override fun onCoinClicked(dataCoin: CoinsData.Data) {
        val intent = Intent(this, CoinActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable("bundle1", dataCoin)
        bundle.putParcelable("bundle2", aboutDataMap[dataCoin.coinInfo.name]!!)
        intent.putExtra("bundle",bundle)
        startActivity(intent)
        //Toast.makeText(this, dataCoin.coinInfo.fullName + " clicked", Toast.LENGTH_SHORT).show()
    }

    private fun getAboutDataFromAsset() {

        val fileInString = applicationContext.assets
            .open("currencyinfo.json")
            .bufferedReader()
            .use { it.readText() }

        aboutDataMap = mutableMapOf<String,CoinAboutItem>()
        val gson = Gson()
        val dataAboutAll = gson.fromJson( fileInString, CoinAboutData::class.java )
        dataAboutAll.forEach {
            aboutDataMap[it.currencyName] = CoinAboutItem(
                it.info.web,
                it.info.github,
                it.info.twt,
                it.info.desc,
                it.info.reddit
            )


        }

    }


}