package it.localhost.trafficdroid.common;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.activity.MainActivity;
import it.localhost.trafficdroid.dao.MainDAO;
import it.localhost.trafficdroid.dto.MainDTO;
import it.localhost.trafficdroid.parser.EventParser;
import it.localhost.trafficdroid.parser.TrafficParser;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TdIntentService extends IntentService {
	public TdIntentService() {
		super(Const.tdData);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!TdLock.getLock(this).isHeld())
			TdLock.getLock(this).acquire();
		super.onStartCommand(intent, flags, startId);
		return START_REDELIVER_INTENT;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		sendBroadcast(Const.beginUpdateIntent);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String trafficUrl = sharedPreferences.getString(getString(R.string.providerTrafficKey), getString(R.string.providerTrafficDefault));
		String eventUrl = sharedPreferences.getString(getString(R.string.providerEventKey), getString(R.string.providerEventDefault));
		try {
			//METTERE ! su event
			if (!trafficUrl.equals(getString(R.string.providerTrafficDefault)) && eventUrl.equals(getString(R.string.providerEventDefault))) {
				MainDTO mainDto = MainDAO.create(this);
				TrafficParser.parse(mainDto, trafficUrl);
				EventParser.parse(mainDto, eventUrl);
				MainDAO.store(mainDto, this);
				String congestedZones = mainDto.getCongestedZones();
				if (congestedZones != null && PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.notificationEnablerKey), Boolean.parseBoolean(getString(R.string.notificationEnablerDefault)))) {
					Notification notification = new Notification(R.drawable.notif_icon, getString(R.string.tickerText), System.currentTimeMillis());
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notification.defaults |= Notification.DEFAULT_ALL;
					notification.setLatestEventInfo(this, getString(R.string.notificationTitle), congestedZones, PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
					((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(Const.notificationId, notification);
				} else
					((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(Const.notificationId);
			}
		} catch (TdException e) {
			// come gestiamo la cosa?
		} finally {
			sendBroadcast(Const.endUpdateIntent);
			TdLock.getLock(this).release();
		}
	}
}