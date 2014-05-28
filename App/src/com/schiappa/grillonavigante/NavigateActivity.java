package com.schiappa.grillonavigante;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class NavigateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigate);
		
        WebView webView = (WebView)findViewById(R.id.webMapView);
        webView.getSettings().setJavaScriptEnabled(true);
      //  webView.addJavascriptInterface(new WebAppInterface(this), "Grillo");
      //  webView.addJavascriptInterface(new myJavaScriptInterface(this), "Grillo");
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/www/map.html");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigate, menu);
		return true;
	}

}
