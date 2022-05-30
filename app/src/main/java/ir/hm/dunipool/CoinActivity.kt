package ir.hm.dunipool

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.hm.dunipool.apiManager.BASE_URL_TWITTER
import ir.hm.dunipool.apiManager.model.CoinAboutItem
import ir.hm.dunipool.apiManager.model.CoinsData
import ir.hm.dunipool.databinding.ActivityCoinBinding


class CoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinBinding
    lateinit var dataCoinThis: CoinsData.Data
    lateinit var dataThisCoinAbout : CoinAboutItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fromIntent = intent.getBundleExtra("bundle")!!
        dataCoinThis = fromIntent.getParcelable<CoinsData.Data>("bundle1")!!
        dataThisCoinAbout = fromIntent.getParcelable<CoinAboutItem>("bundle2")!!

        binding.layoutCoinToolbar.toolbar.title = dataCoinThis.coinInfo.fullName

        initUi()

    }

    private fun initUi() {
        initAboutUi()
        initStatisticsUi()
        initChartUi()
    }

    private fun initChartUi() {

    }

    private fun initStatisticsUi() {

        binding.layoutCoinStasitics.tvOpenAmount.text = dataCoinThis.dISPLAY.uSD.oPEN24HOUR
        binding.layoutCoinStasitics.tvTodaysHighAmount.text = dataCoinThis.dISPLAY.uSD.hIGH24HOUR
        binding.layoutCoinStasitics.tvTodayLowAmount.text = dataCoinThis.dISPLAY.uSD.lOW24HOUR
        binding.layoutCoinStasitics.tvChangeTodayAmount.text = dataCoinThis.dISPLAY.uSD.cHANGE24HOUR
        binding.layoutCoinStasitics.tvAlgorithm.text = dataCoinThis.coinInfo.algorithm
        binding.layoutCoinStasitics.tvTotalVolumeAmount.text = dataCoinThis.dISPLAY.uSD.tOTALVOLUME24H
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