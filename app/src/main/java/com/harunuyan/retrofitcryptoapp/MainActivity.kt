package com.harunuyan.retrofitcryptoapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harunuyan.retrofitcryptoapp.adapter.RecyclerViewAdapter
import com.harunuyan.retrofitcryptoapp.databinding.ActivityMainBinding
import com.harunuyan.retrofitcryptoapp.model.CryptoModel
import com.harunuyan.retrofitcryptoapp.service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {
    //Base URL
    private val BASE_URL = "https://rest.coinapi.io/v1/"

    // Gelen verileri alacağımız liste. Bu listeyi bir ArrayList ve ilk başta null olacak şekilde tanımladık.
    private var cryptoModels: ArrayList<CryptoModel>? = null

    //Adapter oluşturmak
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    // Disposable: Tek kullanımlık. Activity, Fragment kapatıldığında call'ları siler.
    private var compositeDisposable: CompositeDisposable? = null // Farklı disposable'ları buraya koyup hepsinden bir kere de kurtulmamızı sağlar.

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        compositeDisposable = CompositeDisposable()

        // RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager




        loadData()

    }


    private fun loadData() {

        // Retrofit objesi tanımlanır.
        // BaseURL verilir.
        // GsonConverter verilir.
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)

        compositeDisposable?.add(
            retrofit.getData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResponse)
        )


        // Service içerisinde Call dan extend alan fonksiyonumuzu çağırırız.
        /*
        // API ile retrofiti bağlayan bir service oluşturulur.
        val service = retrofit.create(CryptoAPI::class.java)
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

                        for (cryptoModel: CryptoModel in cryptoModels!!) {
                            Log.e("DENEME", cryptoModel.name)
                            Log.e("DENEME", cryptoModel.asset_id)
                            cryptoModel.price_usd?.let { it1 -> Log.e("DENEME", it1) }
                        }
                    }
                }
            }

            // Hata varsa
            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })*/

    }

    private fun handleResponse(cryptoList: List<CryptoModel>) {

        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
            binding.recyclerView.adapter = recyclerViewAdapter
        }

    }
    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this, "Clicked : ${cryptoModel.asset_id}", Toast.LENGTH_SHORT).show()
    }
}