package com.harunuyan.retrofitcryptoapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harunuyan.retrofitcryptoapp.adapter.RecyclerViewAdapter
import com.harunuyan.retrofitcryptoapp.databinding.ActivityMainBinding
import com.harunuyan.retrofitcryptoapp.model.CryptoModel
import com.harunuyan.retrofitcryptoapp.service.CryptoAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {
    //Base URL
    private val BASE_URL = "https://rest.coinapi.io/v1/"

    // Gelen verileri alacağımız liste. Bu listeyi bir ArrayList ve ilk başta null olacak şekilde tanımladık.
    private var cryptoModels: ArrayList<CryptoModel>? = null

    //Adapter oluşturmak
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadData()

    }


    private fun loadData() {

        // Retrofit objesi tanımlanır.
        //BaseURL verilir.
        //GsonConverter verilir.
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // API ile retrofiti bağlayan bir service oluşturulur.
        val service = retrofit.create(CryptoAPI::class.java)

        // Service içerisinde Call dan extend alan fonksiyonumuzu çağırırız.
        val call = service.getData()

        // Senkronize bir şekilde verileri almak için
        // CallBack ister ve object olarak tanımlarız. Lambda.
        call.enqueue(object : Callback<List<CryptoModel>> {

            // Cevap geldiğinde:
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {

                // Gelen cevap başarılı mı diye kontrol yaparız.
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Eğer null gelmediyse yapılacaklar:
                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
                            binding.recyclerView.adapter = recyclerViewAdapter
                        }

                        /*for (cryptoModel: CryptoModel in cryptoModels!!) {
                            Log.e("DENEME", cryptoModel.name)
                            Log.e("DENEME", cryptoModel.asset_id)
                            cryptoModel.price_usd?.let { it1 -> Log.e("DENEME", it1) }
                        }*/
                    }
                }
            }

            // Hata varsa
            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this, "Clicked : ${cryptoModel.asset_id}", Toast.LENGTH_SHORT).show()
    }
}