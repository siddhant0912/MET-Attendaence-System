package com.example.metattendanceapp


import android.util.Base64
import android.util.Base64.DEFAULT
import com.example.metattendanceapp.Encrypt.encrypt.iv
import com.example.metattendanceapp.Encrypt.encrypt.salt
import com.example.metattendanceapp.Encrypt.encrypt.secretKey
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Decrypt {
   companion object decrypt{
       @JvmStatic
       fun decrypt(pass:String): String? {
           try
           {
               val ivParameterSpec =  IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))
               val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
               val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, DEFAULT), 10000, 256)
               val tmp = factory.generateSecret(spec);
               val secretKey =  SecretKeySpec(tmp.encoded, "AES")

               val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
               cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
               return  String(cipher.doFinal(Base64.decode(pass, Base64.DEFAULT)))
           }
           catch (e : Exception) {
               println("Error while decrypting: $e");
           }
           return null
       }

   }
}