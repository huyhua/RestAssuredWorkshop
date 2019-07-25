package com.nvg.utils

import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class IS24Auth {
    private val dateFormatter =  SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
        .also { it.timeZone = TimeZone.getTimeZone("GMT") }
    private val secret = "jdai4*c52kl"

    val date = dateFormatter.format(Date())
    val UDID = "6AE67C79-DA46-4309-BCAB-DA6B7933A969"
    val apiKey = "6a81c2d9-743d-2a1c-62bf-2545382fe7e1"

    val token by lazy {
        val data = "$UDID-$apiKey-$secret"
        val mac = Mac.getInstance("HmacMD5")
        mac.init(SecretKeySpec(date.toByteArray(), "HmacMD5"))
        mac.update(data.toByteArray())
        Base64.getEncoder().encodeToString(mac.doFinal())
    }
}