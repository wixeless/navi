package com.marvel.stark.utils

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList

fun JSONObject.isNotNull(name: String): Boolean {
    return !isNull(name)
}

fun String.format(vararg args: Any?): String = java.lang.String.format(Locale.ENGLISH, this, *args)

inline fun <FRAGMENT : Fragment> FRAGMENT.putArgs(argsBuilder: Bundle.() -> Unit): FRAGMENT = this.apply { arguments = Bundle().apply(argsBuilder) }

inline fun <reified T> JSONArray.forEach(action: (T?) -> Unit) {
    (0 until length()).forEach { action(get(it) as? T) }
}

inline fun <reified T> JSONArray.last(): (T?) {
    return get(this.length() - 1) as? T
}

inline fun <reified T> JSONArray.forEachIndexed(action: (index: Int, T?) -> Unit) {
    (0 until length()).forEach { i -> action(i, get(i) as? T) }
}

fun JSONArray.isNotEmpty(): Boolean {
    return this.length() > 0
}

fun JSONArray.reverse(): JSONArray {
    val newArray = JSONArray()
    (this.length() - 1 downTo 0).forEach {
        newArray.put(this.get(it))
    }
    return newArray
}

fun JSONArray.isEmpty(): Boolean {
    return this.length() == 0
}

fun JSONObject.isEmpty(): Boolean {
    return this.length() == 0
}

fun JSONObject.isNotEmpty(): Boolean {
    return this.length() > 0
}

fun View.show() {
    this.visibility = VISIBLE
}

fun View.hide() {
    this.visibility = GONE
}

private const val cipherTransformation = "AES/CBC/PKCS5Padding"
private const val aesEncryptionAlgorithm = "AES"

fun String.encrypt(key: String): String {
    return try {
        var keyBytes = key.toByteArray(charset("UTF-8"))
        val md = MessageDigest.getInstance("SHA-256")
        md.update(keyBytes)
        keyBytes = md.digest()
        val newKey = SecretKeySpec(keyBytes, aesEncryptionAlgorithm)
        val cipher = Cipher.getInstance(cipherTransformation)
        val random = SecureRandom()
        val ivBytes = ByteArray(16)
        random.nextBytes(ivBytes)
        cipher.init(Cipher.ENCRYPT_MODE, newKey, random)
        val msg = this.toByteArray()
        val destination = ByteArray(ivBytes.size + msg.size)
        System.arraycopy(ivBytes, 0, destination, 0, ivBytes.size)
        System.arraycopy(msg, 0, destination, ivBytes.size, msg.size)
        val res = cipher.doFinal(destination)
        Base64.encodeToString(res, Base64.DEFAULT)
    } catch (ex: Exception) {
        Log.d("EXTENSIONS", "encrypt: ${ex.message}")
        ""
    }

}

fun String.decrypt(key: String): String {
    return try {
        val bytes = Base64.decode(this, 0)
        var keyBytes = key.toByteArray(charset("UTF-8"))
        val md = MessageDigest.getInstance("SHA-256")
        md.update(keyBytes)
        keyBytes = md.digest()
        val ivB = Arrays.copyOfRange(bytes, 0, 16)
        val codB = Arrays.copyOfRange(bytes, 16, bytes.size)
        val ivSpec = IvParameterSpec(ivB)
        val newKey = SecretKeySpec(keyBytes, aesEncryptionAlgorithm)
        val cipher = Cipher.getInstance(cipherTransformation)
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
        val res = cipher.doFinal(codB)
        String(res)
    } catch (ex: Exception) {
        Log.d("EXTENSIONS", "decrypt: ${ex.message}")
        ""
    }
}

//DON'T REMOVE
fun Context.getHash(sha1: String): String {
    return try {
        val hexArray = sha1.split(":")
        val bytesList = ArrayList<Byte>()
        hexArray.forEach {
            val s = it.toLong(radix = 16).toInt()
            bytesList.add(s.toByte())
        }
        val bytesArray = bytesList.toByteArray()
        String(Base64.encode(bytesArray, Base64.DEFAULT))

    } catch (ex: Exception) {
        Log.d("EXTENSION", "getHash: ${ex.message}")
        ""
    }
}
