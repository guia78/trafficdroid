package it.localhost.trafficdroid.fragment;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.activity.MainActivity;
import it.localhost.trafficdroid.common.AdManager;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.android.gms.ads.AdView;

public class WebviewFragment extends Fragment {
	public static final String ALCOL_URL = "http://voti.kataweb.it/etilometro/index.php";
	private static final String URL_KEY = "URL_KEY";

	public static WebviewFragment newInstance(String url) {
		WebviewFragment instance = new WebviewFragment();
		Bundle args = new Bundle(1);
		args.putString(URL_KEY, url);
		instance.setArguments(args);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		WebView webView = (WebView) inflater.inflate(R.layout.webview, container, false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(getArguments().getString(URL_KEY));
		((MainActivity) getActivity()).setScreenName(5);
		new AdManager().load(getActivity(), ((AdView) webView.findViewById(R.id.adView)), true);
		return webView;
	}
}