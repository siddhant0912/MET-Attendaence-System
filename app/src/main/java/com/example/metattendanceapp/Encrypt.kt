package com.example.metattendanceapp


import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Encrypt {
    companion object encrypt{
        const val secretKey = "tK5UTui+DPh8lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ="
        const val salt = "QWlGNHNhMTJTQWZ2bGhpV3U="
        const val iv = "bVQzNFNhRkQ1Njc4UUFaWA=="

        @JvmStatic
        fun encrypt(spass:String): String? {

            try
            {
                val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, DEFAULT), 10000, 256)
                val tmp = factory.generateSecret(spec)
                val secretKey =  SecretKeySpec(tmp.encoded, "AES")

                val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
                return Base64.encodeToString(cipher.doFinal(spass.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)
            }
            catch (e: Exception)
            {
                println("Error while encrypting: $e")
            }
            return null
        }
    }
}