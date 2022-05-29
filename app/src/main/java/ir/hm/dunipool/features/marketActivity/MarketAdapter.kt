package ir.hm.dunipool.features.marketActivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.hm.dunipool.R
import ir.hm.dunipool.apiManager.BASE_URL_IMAGE
import ir.hm.dunipool.apiManager.model.CoinsData
import ir.hm.dunipool.databinding.ItemRecyclerMarketBinding

class MarketAdapter(
    private val recyclerCallback: RecyclerCallback,
    private val data: ArrayList<CoinsData.Data>
) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    lateinit var binding: ItemRecyclerMarketBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindViews(dataCoin: CoinsData.Data) {


            binding.txtCoinName.text = dataCoin.coinInfo.fullName
            binding.txtPrice.text = dataCoin.dISPLAY.uSD.pRICE
            // binding.txtMarketCap.text = dataCoin.rAW.uSD.mKTCAP.toString()
            //   binding.txtTaghir.text = dataCoin.rAW.uSD.cHANGE24HOUR.toString()


            val taghir = dataCoin.rAW.uSD.cHANGEPCT24HOUR
            if (taghir > 0) {
                binding.txtTaghir.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorGain
                    )
                )
                binding.txtTaghir.text =
                    "+" + dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 4) + "%"
            } else if (taghir < 0) {
                binding.txtTaghir.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorLoss
                    )
                )
                binding.txtTaghir.text =
                    dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"
            } else {
                binding.txtTaghir.text = "0%"
            }

            val marketCap = dataCoin.rAW.uSD.mKTCAP / 1000000000
            val indexDot = marketCap.toString().indexOf('.')
            binding.txtMarketCap.text = "$" + marketCap.toString().substring(0, indexDot + 3) + " B"

            Glide
                .with(itemView)
                .load(BASE_URL_IMAGE + dataCoin.coinInfo.imageUrl)
                .into(binding.imgItem)


            itemView.setOnClickListener {
                recyclerCallback.onCoinClicked(dataCoin)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMarketBinding.inflate(inflater)
        return MarketViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {

        holder.bindViews(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RecyclerCallback {
        fun onCoinClicked(dataCoin: CoinsData.Data)
    }

}