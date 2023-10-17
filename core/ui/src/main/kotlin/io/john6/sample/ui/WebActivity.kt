package io.john6.sample.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import io.john6.base.compose.JAppTheme
import io.john6.base.compose.spaceSmall

class WebActivity : ComponentActivity() {

    private lateinit var mUrl: String

    private var webView:WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        mUrl = intent?.getStringExtra("url") ?: ""
        if (mUrl.isEmpty()) {
            finish()
            return
        }

        setContent {
            WebScreen(
                initialUrl = mUrl,
                setWebView = {
                    webView = it
                },
                onBackPressed = this::onBackHandlePressed
            )
        }
    }

    private fun onBackHandlePressed(){
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        }else{
            finish()
        }
    }

    companion object{
        fun show(context:Context, url:String){
            val intent = Intent(context, WebActivity::class.java).apply {
                putExtra("url", url)
            }
            context.startActivity(intent)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebScreen(initialUrl: String,
                      setWebView:(WebView) -> Unit,
                      onBackPressed: () -> Unit) {
    JAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                factory = { context ->
                    WebView(context).apply {
                        setWebView(this)
                        settings.javaScriptEnabled = true
                        loadUrl(initialUrl)
                    }
                })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .padding(MaterialTheme.spaceSmall),
                contentAlignment = Alignment.Center
            ) {

                val text = buildAnnotatedString {
                    appendInlineContent("icon_lock", "[icon]")
                    append(" ")
                    append(Uri.parse(initialUrl).host ?: "")
                }

                Text(
                    color = MaterialTheme.colors.onBackground,
                    text = text,
                    style = MaterialTheme.typography.caption,
                    inlineContent = mapOf(
                        Pair(
                            "icon_lock",
                            InlineTextContent(
                                Placeholder(
                                    width = MaterialTheme.typography.caption.fontSize,
                                    height = MaterialTheme.typography.caption.fontSize,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                                )
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.onBackground
                                )
                            }
                        )
                    ),
                )
            }

            BackHandler(enabled = true, onBack = onBackPressed)
        }
    }
}