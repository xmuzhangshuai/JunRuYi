package com.junruyi.ui;

import com.smallrhino.junruyi.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weburl);
		webView = (WebView) findViewById(R.id.webview);
		webView.loadUrl("smallrhino.net");
	}
}
