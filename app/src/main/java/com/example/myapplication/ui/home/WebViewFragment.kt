package com.example.myapplication.ui.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class WebViewFragment : Fragment() {

    private lateinit var webView: WebView
    private var customView: View? = null
    private lateinit var customViewContainer: FrameLayout
    private lateinit var webChromeClient: WebChromeClient
    private var originalSystemUiVisibility: Int = 0
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация WebView и контейнера для полноэкранного режима
        webView = view.findViewById(R.id.webView)
        customViewContainer = view.findViewById(R.id.fullscreen_container)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Получаем нижнюю панель навигации (если она есть в activity)
        bottomNavigationView = activity?.findViewById(R.id.bottom_nav_menu)

        // Настройка WebChromeClient для полноэкранного режима
        webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                if (customView != null) {
                    callback.onCustomViewHidden()
                    return
                }

                // Сохраняем текущее состояние и переходим в полноэкранный режим
                customView = view
                customViewContainer.addView(customView)
                customViewContainer.visibility = View.VISIBLE
                webView.visibility = View.GONE

                // Сохраняем текущее состояние системного UI
                originalSystemUiVisibility = requireActivity().window.decorView.systemUiVisibility

                // Переход в полноэкранный режим и скрытие нижней панели навигации
                requireActivity().window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        )

                // Скрываем BottomNavigationView
                bottomNavigationView?.visibility = View.GONE
            }

            override fun onHideCustomView() {
                // Выход из полноэкранного режима, показываем WebView и нижнюю панель
                customViewContainer.removeView(customView)
                customView = null
                customViewContainer.visibility = View.GONE
                webView.visibility = View.VISIBLE

                // Восстанавливаем исходное состояние системного UI
                requireActivity().window.decorView.systemUiVisibility = originalSystemUiVisibility

                // Показываем BottomNavigationView обратно
                bottomNavigationView?.visibility = View.VISIBLE
            }
        }
        webView.webChromeClient = webChromeClient

        // Загружаем URL, переданный во фрагменте
        val url = arguments?.getString("url")
        if (url != null) {
            webView.loadUrl(url)
        }
    }
}
