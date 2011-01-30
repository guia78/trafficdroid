package it.localhost.trafficdroid.activity;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.common.Const;
import it.localhost.trafficdroid.common.TdException;
import it.localhost.trafficdroid.dao.CookieDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;

public class WebcamActivity extends Activity {
	private WebView mWebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWebView = new WebView(this);
		setContentView(mWebView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		String url = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.providerCamKey), getString(R.string.providerCamDefault));
		if (url.equals(getString(R.string.providerCamDefault)))
			new AlertDialog.Builder(this).setTitle(getString(R.string.warning)).setPositiveButton(getString(R.string.ok), null).setMessage(getString(R.string.badWebcamConf)).show();
		else {
			try {
				CookieDAO.setCookie(this, url);
			} catch (TdException e) {
				e.printStackTrace();
			}
			mWebView.loadUrl(Const.http + url + Const.popupTelecamera + getIntent().getStringExtra(Const.camId));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mWebView.clearHistory();
		mWebView.clearFormData();
		mWebView.clearCache(true);
	}
}