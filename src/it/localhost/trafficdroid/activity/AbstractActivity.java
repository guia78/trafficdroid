package it.localhost.trafficdroid.activity;

import it.localhost.trafficdroid.common.Const;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public abstract class AbstractActivity extends Activity {
	private GoogleAnalyticsTracker tracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(Const.anlyticsId, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		tracker.trackPageView(this.getClass().getName());
	}

	@Override
	public void onPause() {
		super.onPause();
		tracker.dispatch();
	}

	@Override
	public void onStop() {
		super.onStop();
		tracker.stopSession();
	}

	public void trackEvent(String arg0, String arg1, String arg2) {
		tracker.trackEvent(arg0, arg1, arg2, 0);
	}

	public String versionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			return "NotFound";
		}
	}
}