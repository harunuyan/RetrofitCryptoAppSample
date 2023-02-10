package com.harunuyan.retrofitcryptoapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harunuyan.retrofitcryptoapp.R
import com.harunuyan.retrofitcryptoapp.databinding.RowLayoutBinding
import com.harunuyan.retrofitcryptoapp.model.CryptoModel

class RecyclerViewAdapter(
    private val cryptoList: ArrayList<CryptoModel>,
    private val listener: Listener
) :
    RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    // Background rengi tanımlıyoruz. Her itemde listedeki renkleri background alacak.
    val backGroundColor: Array<String> = arrayOf(
        "#471147",
        "#7b5375",
        "#751811",
        "#364236",
        "#cc0800",
        "#ff1a55",
        "#014c56",
        "#8B8B6F"
    )

    interface Listener {
        fun onItemClick(cryptoModel: CryptoModel)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            cryptoModel: CryptoModel,
            backGroundColor: Array<String>,
            position: Int,
            listener: Listener
        ) {
            val view = RowLayoutBinding.bind(itemView)
            view.linearLayout.setOnClickListener {
                listener.onItemClick(cryptoModel)
            }
            view.linearLayout.setBackgroundColor(Color.parseColor(backGroundColor[position % 8]))
            view.textAssetId.text = cryptoModel.asset_id
            view.textName.text = cryptoModel.name
            view.textPriceUsd.text = cryptoModel.price_usd
        }
    }

    //parent: ViewGroup'u kullanarak oluşturduğumuz row_layout'u bağlyıp bir RowHolder döndürürüz.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return RowHolder(view)
    }

    // Kaç row oluşturulacağı
    override fun getItemCount(): Int {
        return cryptoList.size
    }

    // Hangi itemin ne verisi göstereceğini yapıyoruz. RowHolder içerisine bir fun(bind) oluşturup bu fun içerisinde tüm işlemler yapılır.
    override fun onBindViewHolder(holder: RowHolder, position: Int) {

        holder.bind(cryptoList[position],backGroundColor,position,listener)
    }
}