package com.example.myapplication.ui.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import java.io.ByteArrayInputStream
import java.util.regex.Pattern

class WebViewFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var blockedDomains: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Загружаем список заблокированных доменов из файла
        blockedDomains = loadBlockedDomains()

        webView = view.findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                val urlHost = request?.url?.host ?: return super.shouldInterceptRequest(view, request)

                // Проверяем, содержит ли URL заблокированный домен
                if (blockedDomains.any { urlHost.contains(it) }) {
                    // Возвращаем пустой ответ для блокировки
                    return WebResourceResponse(
                        "text/plain", "utf-8",
                        ByteArrayInputStream("".toByteArray())
                    )
                }
                return super.shouldInterceptRequest(view, request)
            }
        }

        // Загружаем URL, переданный во фрагменте
        val url = arguments?.getString("url")
        if (url != null) {
            webView.loadUrl(url)
        }
    }

    // Функция для загрузки доменов из файла easylist.txt
    private fun loadBlockedDomains(): List<String> {
        val inputStream = requireContext().assets.open("easylist.txt")
        val domainPattern = Pattern.compile("^[|]*([a-zA-Z0-9.-]+)\\^?$") // Регулярное выражение для извлечения доменов

        return inputStream.bufferedReader().useLines { lines ->
            lines.mapNotNull { line ->
                if (line.isNotBlank() && !line.startsWith("!") && !line.startsWith("[")) {
                    val matcher = domainPattern.matcher(line.trim())
                    if (matcher.find()) {
                        matcher.group(1) // Извлекаем только домен
                    } else null
                } else null
            }.toList()
        }
    }
}
