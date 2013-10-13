package com.wheaton.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BonAppMenu extends Activity
{
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		WebView webView = new WebView(this);
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		setContentView(webView);

		setProgressBarIndeterminate(true);
		setProgressBarIndeterminateVisibility(true);

		webView.loadUrl(MainScreen.MENU_URL);
	}

	private class MyWebViewClient extends WebViewClient
	{
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url)
	    {
	        view.loadUrl(url);
	        return true;
	    }

	    @Override
	    public void onPageFinished(WebView view, String url)
	    {
	    	super.onPageFinished(view, url);
	    	setProgressBarIndeterminateVisibility(false);
	    }
	}
}
