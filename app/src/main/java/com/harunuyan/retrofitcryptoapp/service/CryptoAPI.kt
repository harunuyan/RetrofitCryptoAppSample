package com.harunuyan.retrofitcryptoapp.service

import com.harunuyan.retrofitcryptoapp.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    // GET, POST, UPDATE, DELETE

    // URL'nin base'i: https://rest.coinapi.io/v1/  retrofit objesinin içerisine yazılır.
    // EXTENSION: assets?apikey=21329B53-78C9-4825-872C-D96219D21A68   interface içerisine yazılır.


    @GET("assets?apikey=21329B53-78C9-4825-872C-D96219D21A68")

    // Verileri internetten indirirken UI bloklamadan arkaplanda yapılacak işlem fonksiyonu.
    // getData çağırıldığında call yapılacak ve geri dönen veri fazla olduğundan bize liste içerisinde verilecek.
fun getData(): Call<List<CryptoModel>>

}