package ir.hm.dunipool.features.coinActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ir.dunijet.dunipool.apiManager.model.ChartData
import ir.hm.dunipool.R
import ir.hm.dunipool.apiManager.ApiManager
import ir.hm.dunipool.apiManager.BASE_URL_TWITTER
import ir.hm.dunipool.apiManager.model.*
import ir.hm.dunipool.databinding.ActivityCoinBinding


class CoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinBinding
    lateinit var dataCoinThis: CoinsData.Data
    lateinit var dataThisCoinAbout: CoinAboutItem
    val apiManager = ApiManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fromIntent = intent.getBundleExtra("bundle")!!
        dataCoinThis = fromIntent.getParcelable<CoinsData.Data>("bundle1")!!

        if (fromIntent.getParcelable<CoinAboutItem>("bundle2") != null) {
            dataThisCoinAbout = fromIntent.getParcelable<CoinAboutItem>("bundle2")!!
        } else {
            dataThisCoinAbout = CoinAboutItem()
        }

        binding.layoutCoinToolbar.toolbar.title = dataCoinThis.coinInfo.fullName

        initUi()

    }

    private fun initUi() {
        initAboutUi()
        initStatisticsUi()
        initChartUi()
    }

    @SuppressLint("SetTextI18n")
    private fun initChartUi() {

        binding.layoutCoinChart.txtChertPrice.text = dataCoinThis.dISPLAY.uSD.pRICE
        binding.layoutCoinChart.txtChange1.text = dataCoinThis.dISPLAY.uSD.cHANGE24HOUR

        val taghir = dataCoinThis.rAW.uSD.cHANGEPCT24HOUR
        if (taghir > 0) {
            binding.layoutCoinChart.txtChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )
            binding.layoutCoinChart.txtUpDown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )
            binding.layoutCoinChart.sparkMain.lineColor = ContextCompat.getColor(binding.root.context,R.color.colorGain)
            binding.layoutCoinChart.txtChange2.text = "+"+dataCoinThis.dISPLAY.uSD.cHANGEPCT24HOUR.substring(0, 4)+"%"
            binding.layoutCoinChart.txtUpDown.text = "▲"

        } else if (taghir < 0) {
            binding.layoutCoinChart.txtChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.layoutCoinChart.txtUpDown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )
            binding.layoutCoinChart.sparkMain.lineColor =
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            binding.layoutCoinChart.txtChange2.text = dataCoinThis.dISPLAY.uSD.cHANGEPCT24HOUR.substring(0, 5)+"%"
            binding.layoutCoinChart.txtUpDown.text = "▼"


        } else {
            binding.layoutCoinChart.txtUpDown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.tertiaryTextColor
                )
            )

            binding.layoutCoinChart.txtChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.tertiaryTextColor
                )
            )
            binding.layoutCoinChart.txtChange2.text = "0%"
        }

        binding.layoutCoinChart.sparkMain.setScrubListener {

            if (it === null){
                // show price kamel
                binding.layoutCoinChart.txtChertPrice.text = dataCoinThis.dISPLAY.uSD.pRICE
            }else{
                //show price this dot
                binding.layoutCoinChart.txtChertPrice.text = "$" + (it as ChartData.Data).close.toString()

            }

        }


        var period: String = HOUR
        requestAndShowChart(period)


        binding.layoutCoinChart.radioGroupMain.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.radio_12h -> {
                    period = HOUR
                }
                R.id.radio_1d -> {
                    period = HOURS24
                }
                R.id.radio_1w -> {
                    period = WEEK
                }
                R.id.radio_1m -> {
                    period = MONTH
                }
                R.id.radio_3m -> {
                    period = MONTH3
                }
                R.id.radio_1y -> {
                    period = YEAR
                }
                R.id.radio_all -> {
                    period = ALL
                }

            }
            requestAndShowChart(period)
        }
    }

    private fun requestAndShowChart(period: String) {

        apiManager.getChartData(
            dataCoinThis.coinInfo.name,
            period,
            object : ApiManager.ApiCallBack<Pair<List<ChartData.Data>, ChartData.Data?>> {
                override fun onSuccess(data: Pair<List<ChartData.Data>, ChartData.Data?>) {

                    val sparkAdapter = CoinAdapter(data.first, data.second?.open.toString())
                    binding.layoutCoinChart.sparkMain.adapter = sparkAdapter
                }

                override fun onFailure(errorMessage: String) {
                    Toast.makeText(this@CoinActivity, "error =>" + errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            })

    }

    private fun initStatisticsUi() {

        binding.layoutCoinStasitics.tvOpenAmount.text = dataCoinThis.dISPLAY.uSD.oPEN24HOUR
        binding.layoutCoinStasitics.tvTodaysHighAmount.text = dataCoinThis.dISPLAY.uSD.hIGH24HOUR
        binding.layoutCoinStasitics.tvTodayLowAmount.text = dataCoinThis.dISPLAY.uSD.lOW24HOUR
        binding.layoutCoinStasitics.tvChangeTodayAmount.text = dataCoinThis.dISPLAY.uSD.cHANGE24HOUR
        binding.layoutCoinStasitics.tvAlgorithm.text = dataCoinThis.coinInfo.algorithm
        binding.layoutCoinStasitics.tvTotalVolumeAmount.text =
            dataCoinThis.dISPLAY.uSD.tOTALVOLUME24H
        binding.layoutCoinStasitics.tvAvgMarketCapAmount.text = dataCoinThis.dISPLAY.uSD.mKTCAP
        binding.layoutCoinStasitics.tvSupplyNumber.text = dataCoinThis.dISPLAY.uSD.sUPPLY


    }

    private fun initAboutUi() {

        binding.layoutCoinAbout.txtWebsite.text = dataThisCoinAbout.coinWebsite
        binding.layoutCoinAbout.txtGithub.text = dataThisCoinAbout.coinGithub
        binding.layoutCoinAbout.txtTwitter.text = "@"+dataThisCoinAbout.coinTwitter
        binding.layoutCoinAbout.txtReddit.text = dataThisCoinAbout.coinReddit
        binding.layoutCoinAbout.txtAboutCoin.text = dataThisCoinAbout.coinDesc

        binding.layoutCoinAbout.txtWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataThisCoinAbout.coinWebsite))
            startActivity(intent)
        }

        binding.layoutCoinAbout.txtGithub.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataThisCoinAbout.coinGithub))
            startActivity(intent)
        }

        binding.layoutCoinAbout.txtReddit.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataThisCoinAbout.coinReddit))
            startActivity(intent)
        }

        binding.layoutCoinAbout.txtTwitter.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL_TWITTER+dataThisCoinAbout.coinReddit))
            startActivity(intent)
        }


    }
}