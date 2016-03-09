package com.example.newstest.activity;

import com.example.newstest.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class BrowserNewsActivity extends Activity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser_news);

		init();
		initData();
	}

	private void initData() {
		webView.loadUrl(getIntent().getStringExtra("newsContentUrl"));
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}

	private void init() {
		webView = (WebView) this.findViewById(R.id.webView);
	}
}
