package com.example.securitychat.signalR

import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class TrustAllCerts : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
    override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
}