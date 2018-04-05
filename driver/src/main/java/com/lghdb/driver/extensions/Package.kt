package com.lghdb.driver.extensions

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import java.security.MessageDigest
import java.util.*

/**
 * Created by lghdb on 2018/4/2.
 */

fun AppCompatActivity.sha1(): String {
    val info = this.packageManager.getPackageInfo(this.packageName,
            PackageManager.GET_SIGNATURES)
    val cert = info.signatures[0].toByteArray()
    val md = MessageDigest.getInstance("SHA1")
    val publicKey:ByteArray = md.digest(cert)
    var hexString:String = ""
    for(k in publicKey){
        var appendString = Integer.toHexString(0xFF and k.toInt())
                .toUpperCase(Locale.US)
        if (appendString.length == 1) hexString += "0"
        hexString += appendString + ":"
    }
    return hexString.substring(0, hexString.length - 1);
}