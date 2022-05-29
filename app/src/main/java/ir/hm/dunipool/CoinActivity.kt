package ir.hm.dunipool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.hm.dunipool.apiManager.model.CoinsData
import ir.hm.dunipool.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinBinding
    lateinit var dataCoinThis: CoinsData.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataCoinThis = intent.getParcelableExtra<CoinsData.Data>("dataToSend")!!
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

    }
}